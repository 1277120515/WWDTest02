/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coverage;


import com.alibaba.fastjson.JSON;
import com.vividsolutions.jts.geom.*;
import coverage.iostruct.*;
import coverage.util.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
//import java.math.*;

/**
 *
 * @author ZZL
 */
public class Access {
    
    public Access() {
    }

    //���ȣ�70-140  50~160     γ�ȣ�15-55

    
    int[] cloud2times = new int[]{
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, //0-10
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, //11-20
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, //21-30
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, //31-40
        2, 2, 2, 2, 2, 3, 3, 3, 3, 3, //41-50
        4, 4, 4, 5, 5, 5, 5, 6, 6, 6, //51-60
        7, 7, 7, 7, 7, 8, 8, 8, 8, 8, //61-70
        99, 99, 99, 99, 99, 99, 99, 99, 99, 99, //71-80
        99, 99, 99, 99, 99, 99, 99, 99, 99, 99, //81-90
        99, 99, 99, 99, 99, 99, 99, 99, 99, 99 //91-100 
    };

    
    //����ӿں���
    public int[][] calGridCoverage(AccessInput ai, AccessOutput ao) {

        for (SatelliteInput sli : ai.satelliteInput) {
            //������ǿɲ�ڣ����������ɲ�ڣ�ת��Ϊ���ǲ��ɲ�ڣ��������ɲ��
            if (sli.leftAngle != 0 || sli.rightAngle != 0) {
                for (SensorInput ssi : sli.sensorInput) {
                    ssi.leftAngle = sli.leftAngle;
                    ssi.rightAngle = sli.rightAngle;
                }
                sli.leftAngle = 0;
                sli.rightAngle = 0;
            }
            //����������Ĳ�ڽǴ���30�����趨Ϊ30����ֹ����Ĳ�ڽ����ɨ���߳�������ķ�Χ
            for (SensorInput ssi : sli.sensorInput) {
                if (Math.abs(ssi.leftAngle) > 30 || Math.abs(ssi.rightAngle) > 30) {
                    ssi.leftAngle = -30;
                    ssi.rightAngle = 30;
                }
            }
        }
        int maxGridPerDay = 0;
        for (SatelliteInput sli : ai.satelliteInput) {
            //���㴫����������������
            for (SensorInput ssi : sli.sensorInput) {
                if ((int) (ssi.maxAreaPerDay) != 0) {
                    maxGridPerDay += (int) (ssi.maxAreaPerDay);
                } else {
                    maxGridPerDay = 10000000;
                    System.out.println("����������������");
                    break;
                }
                if (maxGridPerDay == 10000000) {
                    break;
                }
            }
        }
        maxGridPerDay /= 25;//���ת������ÿ����������25ƽ������

        //�洢�û��������ֹʱ��
        Time taskStartTime = new Time(ai.startTime);
        Time taskEndTime = new Time(ai.endTime);
        taskEndTime.add(Calendar.DAY_OF_MONTH, 1);//�ͻ��˴���Ľ���ʱ���ǵ����0��00����ʵ�ʵ��ȵĽ���ʱ���Ǵ��յ�0��00

        OneDayCoverage odc = new OneDayCoverage();//���Ǹ��Ǵ������㹤����
        CloudDataBase cloudDataBase = new CloudDataBase();//��ȡ�������ݿ⹤����
        DixingDataBase dixingDatabase = new DixingDataBase();//��ȡ�������ݿ⹤����
        odc.dixingGrid = dixingDatabase.DixingGrid;
        SnowDataBase snowDataBase = new SnowDataBase();//��ȡѩ�����ݿ⹤����
        odc.snowGrid = snowDataBase.SnowGrid;
        odc.isSnowConsidered = ai.isSnowConseidered;//�Ƿ���ѩ��Ӱ��
        odc.isDixingConsidered = ai.isDixingConseidered;//�Ƿ��ǵ���Ӱ��
        if (odc.isDixingConsidered == true) {
            odc.maxMountainAngle = ai.mountainSwingAngle;
            odc.maxPlainAngle = ai.plainSwingAngle;
        }

        int[] MonthXunStart = taskStartTime.getMonthXun();//��ȡ��ʼʱ�����ڵ��·ݺ�Ѯ
        int[] MonthXunEnd = taskEndTime.getMonthXun();//��ȡ����ʱ�����ڵ��·ݺ�Ѯ
        
        String path;
        int totalNode = 0;
        totalNode = Geometry2Grid(ai, odc);//�������Geometry���в��������ص�������������ܵ���
        ao.totalGrid = totalNode;
        System.out.println("��������");
        
        
        
//        try {
//            DataOutputStream dis = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("D:\\a.txt")));
//            for (int i = 0; i < 800; i++) {
//                for (int j = 0; j < 1400; j++) {
//                    dis.writeInt(odc.chinaGrid[i][j]);
//                }
//            }
//            dis.close();
//            for (int i = 0; i < 800; i++) {
//                for (int j = 0; j < 1400; j++) {
//                    if (odc.chinaGrid[i][j] == 0) {
//                        totalNode += 1;
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        //////////////////////////
        /*
        
         int totalNode = 0;
        
         ai.targetName="2015_p4";
         path = "cg.txt";
         path = "grid\\" + ai.targetName + ".txt";
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
            for (int i = 0; i < 800; i++) {
                for (int j = 0; j < 1400; j++) {
                    int n = dis.readInt();
                    odc.chinaGrid[i][j] = n;
                    odc.chinaOneDayGrid[i][j] = n;
                }
            }
            dis.close();
            for (int i = 0; i < 800; i++) {
                for (int j = 0; j < 1400; j++) {
                    if (odc.chinaGrid[i][j] == 0) {
                        totalNode += 1;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        ao.totalGrid= totalNode;
        
        /////////////////////////////
        */

//        taskFinalTime.add(Calendar.DAY_OF_MONTH, 180);
//        Time[] timeNodeArray = this.getTimeNode(ai.startTime, ai.endTime, taskFinalTime.toBJTime());
        Time[] timeNodeArray = this.getTimeNode(taskStartTime, taskEndTime);

        ao.timeNodeArray = new String[timeNodeArray.length];//ʱ��ڵ�
        ao.progressArray = new float[timeNodeArray.length];//�������
        ao.coverageTimesArray = new int[timeNodeArray.length][];//���Ǵ���
        ao.cloudArray = new int[timeNodeArray.length][];//�������

        float[][] effectiveTimesGrid = new float[800][1400];//�þ������ڴ����������Ч���Ǵ���������ʵ�ʸ��Ǵ���*(1/�����������������Ӧ�ĸ��Ǵ���)
        int[][] formerChinaGrid=new int[800][1400];//����������Ѯ���в�����Ϊ�õ���ǰѮ�ĸ��ǽ��ȣ���֪����һ��Ѯ�ĸ���������þ������ڴ���ϸ�Ѯ�ĸ������

        int timeNodeIndex;//ʱ��ڵ���������������ѮΪ���㵥λ���м���
        for (timeNodeIndex = 0; timeNodeIndex < timeNodeArray.length - 1; timeNodeIndex++) {
            
            Time xunStartTime = timeNodeArray[timeNodeIndex].clone();//��ǰѮ�Ŀ�ʼʱ��
            ao.timeNodeArray[timeNodeIndex] = xunStartTime.toBJTime();
            ao.coverageTimesArray[timeNodeIndex] = new int[20];
            ao.cloudArray[timeNodeIndex] = new int[20];
            int[] ms = xunStartTime.getMonthXun();
            snowDataBase.UpdateSnowGrid(ms[0]);//��ȡѮ���ڵ��·ݣ�����ѩ�����

            //�ڵ���Ѯ�ڣ�����Ϊʱ�䵥λ�������Ѯ�����Ǹ������
            Time DayStartTime = xunStartTime.clone();//ÿһ�����ʼʱ�䣬�ӵ�ǰѮ�ĵ�һ�쿪ʼ����
            while (DayStartTime.beforeOrEqual(timeNodeArray[timeNodeIndex + 1])) {
                Time DayEndTime = DayStartTime.clone();//ÿһ��Ľ���ʱ��
                DayEndTime.add(Calendar.DAY_OF_MONTH, 1);
                ai.startTime = DayStartTime.toBJTime();
                ai.endTime = DayEndTime.toBJTime();//ע�⣬���д���������odc����ʱ����Ϣ����ͬʱ���ai.endTime��ai.startTimeһֱ�ڱ�
                odc.calOneDayCoverage(ai);//����calOneDayCoverage��̰���㷨�����ݸ���������Ѿ����ǵĴ�����ѡ���ڽǣ��õ�����ĸ������
                ArrayList<XYindex> xyList = new ArrayList<XYindex>();
                XYindex.dataGrid = odc.chinaGrid;
                for (int i = 0; i < 800; i++) {
                    for (int j = 0; j < 1400; j++) {
                        //chinaGrid�У�-10����λ�ڵ������������ڲ�
                        //chinaOneDayGrid��ʾ����ĸ������
                        if (odc.chinaGrid[i][j] != -10 && odc.chinaOneDayGrid[i][j] >= 1) {//���[i][j]λ�ڵ������������ڲ����Ҹ��Ǵ�������1��
                            xyList.add(new XYindex(i, j));
//                            odc.chinaGrid[i][j] += 1;//һ���ڸ��Ƕ�ΰ���һ�μ���
                            odc.chinaOneDayGrid[i][j] = 0;//���Ǵ����������㣬������һ�μ���
                        }
                    }
                }
                Collections.sort(xyList);
                
                maxGridPerDay = maxGridPerDay > xyList.size() ? xyList.size() : maxGridPerDay;
                for (int i = 0; i < maxGridPerDay; i++) {
                    XYindex xy = xyList.get(i);
                    odc.chinaGrid[xy.x][xy.y] += 1;
                }
                DayStartTime.add(Calendar.DAY_OF_MONTH, 1);//����ʱ������һ��
            }

            //�������������ʵ�ʸ������������Ч���Ǵ���
            cloudDataBase.UpdateCloudGrid(ms[0], ms[1]);//�����������
            for (int i = 0; i < 800; i++) {
                for (int j = 0; j < 1400; j++) {
                    if (odc.chinaGrid[i][j] != -10) {//�����ж��Ƿ�λ�ڵ������������ڲ�
                        int coverageTimes = odc.chinaGrid[i][j];//��[i][j]�ĵ�ǰѮʵ�ʸ��Ǵ���
                        coverageTimes = coverageTimes >= 19 ? 19 : coverageTimes;//����19�Σ������
                        ao.coverageTimesArray[timeNodeIndex][coverageTimes] += 1;//���Ǵ�������AccessOutput�У�
                        int xunTimes = odc.chinaGrid[i][j] - formerChinaGrid[i][j];//���㵱ǰѮ��ʵ�ʸ��Ǵ���=��ǰѮ���ܸ��Ǵ���-��һѮ���ܸ��Ǵ���
                        int cloudIndex = (int) (cloudDataBase.CloudGrid[i][j] * 100);//����õ������İٷ���
                        cloudIndex = cloudIndex > 99 ? 99 : cloudIndex;//����100ʱ������99��
                        ao.cloudArray[timeNodeIndex][cloudIndex/5]++;//�����������AccessOutput�У�
                        effectiveTimesGrid[i][j] +=(float) xunTimes / cloud2times[cloudIndex];//������Ч���Ǵ���=ʵ�ʸ��Ǵ���*(1/�õ�����������Ӧ�ĸ��Ǵ���)
                        formerChinaGrid[i][j] = odc.chinaGrid[i][j];
                    }
                }
            }
            
            //���㸲�ǽ��ȣ���ǰѮ�ĸ��ǽ���=����������������е���Ч���Ǵ������ܺͣ�����1�ΰ���1���㣩/�ܵ���
            float progress = 0;
            for (int i = 0; i < 800; i++) {
                for (int j = 0; j < 1400; j++) {
                    if (effectiveTimesGrid[i][j] >= 1.0f) {
                        progress += 1.0f;
                    } else {
                        progress += effectiveTimesGrid[i][j];
                    }
                }
            }
            ao.progressArray[timeNodeIndex] = progress / totalNode;
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //                                         �����������ʱ��                                                   //
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
            if (timeNodeArray[timeNodeIndex + 1].equal(taskEndTime)) {
                ao.pngArray = Grid2png(odc.chinaGrid, effectiveTimesGrid);//����Ч���Ǵ�������ת��ΪPNG����Ϊ�������
                ao.difficultyDegree = new int[4];//��ȡ���ݵ����׳̶����飬[0][1][2][3]���δ������ڡ����ס����ѡ����ڣ���Ӧ����Ч���Ǵ�������Ϊ 0-0.5 0.5-1 1-1.5 1.5����
                for (int i = 0; i < 800; i++) {
                    for (int j = 0; j < 1400; j++) {
                        if (odc.chinaGrid[i][j] != -10) {
                            if (effectiveTimesGrid[i][j] < 0.5f) {
                                ao.difficultyDegree[3]++;
                            } else if (effectiveTimesGrid[i][j] < 1.0f) {
                                ao.difficultyDegree[2]++;
                            } else if (effectiveTimesGrid[i][j] < 1.5f) {
                                ao.difficultyDegree[1]++;
                            } else {
                                ao.difficultyDegree[0]++;
                            }
                        }
                    }
                }
                break;
            }
        }
        ao.timeNodeArray[timeNodeIndex+1]=timeNodeArray[timeNodeIndex+1].toBJTime();//�����һ��ʱ��ڵ����AccessOutput��
        ao.isSuccess=true;
        System.out.println("�������");
        
//        System.out.println(JSON.toJSONString(ao));
//        getResult(odc, cloudDataBase, MonthXunStart[0], MonthXunStart[1], MonthXunEnd[0], MonthXunEnd[1], totalNode);
        return odc.chinaGrid;
    }
    
    
    //���������ڼ����û�����ʱ���֮�������ʱ��ڵ㣬ÿ��1Ѯ��ȡһ��ʱ��ڵ㣬���ڼ����Ѯ���������
    //���磺�û�����������ֹʱ����2015-10-05 ~ 2015-11-17
    //����������Time���飺2015-10-05 2015-10-11 2015-10-21 2015-11-01 2015-11-11 2015-11-17
    private Time[] getTimeNode(Time startTimeNode, Time endTimeNode) {
        ArrayList<Time> timeList=new ArrayList<Time>();//ʱ��ڵ�����
        timeList.add(startTimeNode);
        Time newTimeNode = startTimeNode.clone();
        int days = newTimeNode.getBJ(Calendar.DAY_OF_MONTH);
        if (days <= 10) {
            newTimeNode.setBJ(Calendar.DAY_OF_MONTH, 11);
        } else if (days <= 20) {
            newTimeNode.setBJ(Calendar.DAY_OF_MONTH, 21);
        } else {
            newTimeNode.setBJ(Calendar.DAY_OF_MONTH, 1);
            int mon = newTimeNode.getBJ(Calendar.MONTH);
            int year = newTimeNode.getBJ(Calendar.YEAR);
            if (mon >= 11) {
                mon = 0;
                year += 1;
                newTimeNode.setBJ(Calendar.MONTH, mon);
                newTimeNode.setBJ(Calendar.YEAR, year);
            } else {
                mon += 1;
                newTimeNode.setBJ(Calendar.MONTH, mon);
            }
        }

        if (newTimeNode.before(endTimeNode)) {
            timeList.add(newTimeNode);
            while (true) {
                newTimeNode = newTimeNode.clone();
                days = newTimeNode.getBJ(Calendar.DAY_OF_MONTH);
                if (days <= 10) {
                    newTimeNode.setBJ(Calendar.DAY_OF_MONTH, 11);
                } else if (days <= 20) {
                    newTimeNode.setBJ(Calendar.DAY_OF_MONTH, 21);
                } else {
                    newTimeNode.setBJ(Calendar.DAY_OF_MONTH, 1);
                    int mon = newTimeNode.getBJ(Calendar.MONTH);
                    int year = newTimeNode.getBJ(Calendar.YEAR);
                    if (mon >= 11) {
                        mon = 0;
                        year += 1;
                        newTimeNode.setBJ(Calendar.MONTH, mon);
                        newTimeNode.setBJ(Calendar.YEAR, year);
                    } else {
                        mon += 1;
                        newTimeNode.setBJ(Calendar.MONTH, mon);
                    }
                }
                if (newTimeNode.before(endTimeNode)) {
                    timeList.add(newTimeNode);
                } else {      
                    break;
                }
            }
        }
        timeList.add(endTimeNode);
        return (Time[])timeList.toArray(new Time[timeList.size()]);
    }
    
    private Time[] getTimeNode(Time time1, Time time2, Time time3) {
        Time[] nodeArray1 = getTimeNode(time1, time2);
        Time[] nodeArray2 = getTimeNode(time2, time3);
        int len1 = nodeArray1.length;
        int len2 = nodeArray2.length;
        Time[] nodeArray = new Time[len1 + len2 - 1];
        for (int i = 0; i < len1; i++) {
            nodeArray[i] = nodeArray1[i];
        }
        for (int i = 1; i < len2; i++) {
            nodeArray[len1 + i - 1] = nodeArray2[i];
        }
        return nodeArray;
    }
    
    private void getResult(OneDayCoverage odc, CloudDataBase cloudDataBase, int m1, int x1, int m2, int x2, int totalNode) {
        //////////////////////      ͳһͳ����������     //////////////////////////////////
        float[] f = stat(odc.chinaGrid);
        cloudDataBase.UpdateCloudGrid(m1, x1, m2, x2);
        float[] cloudFreq = new float[100];
        for (int i = 0; i < 800; i++) {
            for (int j = 0; j < 1400; j++) {
                if (odc.chinaGrid[i][j] != -10) {
                    int cloud = (int) (100 * cloudDataBase.CloudGrid[i][j]);
                    cloud = cloud >= 100 ? 99 : cloud;
                    cloudFreq[cloud] += 1;
                }
            }
        }
        for (int i = 0; i < 100; i++) {
            cloudFreq[i] /= totalNode;
        }
        float t = 0;
        System.out.print("������ ");
        for (int i = 0; i < 20; i++) {
            float tt = cloudFreq[5 * i + 0] + cloudFreq[5 * i + 1] + cloudFreq[5 * i + 2] + cloudFreq[5 * i + 3] + cloudFreq[5 * i + 4];
            System.out.print(String.format("%.1f ", tt*100));
            t += tt;
        }
        System.out.println("");
        
        for (int i = 0; i < 100; i++) {
            cloudFreq[i] *= f[cloud2times[i] > 19 ? 19 : cloud2times[i]];
        }
         t = 0;
        System.out.print("��� ");
        for (int i = 0; i < 20; i++) {
            float tt = cloudFreq[5 * i + 0] + cloudFreq[5 * i + 1] + cloudFreq[5 * i + 2] + cloudFreq[5 * i + 3] + cloudFreq[5 * i + 4];
            System.out.print(String.format("%.1f ", tt*100));
            t += tt;
        }
        System.out.println("");
        
        
        System.out.println(t);
    }
    
    public float[] stat(int[][] chinaGrid) {
        int[] coverageTimes = new int[20];
        List<Integer> l = new ArrayList<Integer>(1000 * 1400);
        for (int i = 0; i < 800; i++) {
            for (int j = 0; j < 1400; j++) {
                if (chinaGrid[i][j] != -10) {
                    l.add(chinaGrid[i][j]);
                }
            }
        }
        for (int t : l) {
            t = t >= 19 ? 19 : t;
            coverageTimes[t] += 1;
        }
        float[] cumulationTimes = new float[20];
        int len = l.size();
        System.out.print("���Ǵ����� ");
        for (int i = 0; i < 20; i++) {
            cumulationTimes[i] = (float) coverageTimes[i] / len;
            System.out.print(" " + coverageTimes[i]);

        }
        System.out.println("�ۼӽ�� ");
        for (int i = 18; i >= 0; i--) {
            cumulationTimes[i] += cumulationTimes[i + 1];
        }
        for (int i = 0; i < 20; i++) {
            String str=String.format("%.3f ", cumulationTimes[i]);
            System.out.print(str);
        }
        System.out.println();
        return cumulationTimes;
    }
    
    //���ٲ�������
    private static int Geometry2Grid(AccessInput ai, OneDayCoverage odc) {
        MultiPolygon mp = (MultiPolygon) ai.target;
        GeometryFactory gf = new GeometryFactory();
        double[] LonLat = new double[2];
        int[] GridXY = new int[2];
        for (int i = 0; i < 800; i += 20) {
            for (int j = 0; j < 1400; j += 20) {
                // 1����������0.05��γ�ȣ���20������㣨1��γ�ȣ�Ϊһ���������ж����ڵ����������������������ཻ���
                Coordinate[] coor = new Coordinate[5];
                GridXY[0] = j;
                GridXY[1] = i;
                OverlapFun.GridXY2LonLat(GridXY, LonLat);
                coor[0] = new Coordinate(LonLat[0] * Math.PI / 180.0f, LonLat[1] * Math.PI / 180.0f);
                coor[4] = new Coordinate(LonLat[0] * Math.PI / 180.0f, LonLat[1] * Math.PI / 180.0f);
                GridXY[0] = j + 20;
                GridXY[1] = i;
                OverlapFun.GridXY2LonLat(GridXY, LonLat);
                coor[1] = new Coordinate(LonLat[0] * Math.PI / 180.0f, LonLat[1] * Math.PI / 180.0f);
                GridXY[0] = j + 20;
                GridXY[1] = i + 20;
                OverlapFun.GridXY2LonLat(GridXY, LonLat);
                coor[2] = new Coordinate(LonLat[0] * Math.PI / 180.0f, LonLat[1] * Math.PI / 180.0f);
                GridXY[0] = j;
                GridXY[1] = i + 20;
                OverlapFun.GridXY2LonLat(GridXY, LonLat);
                coor[3] = new Coordinate(LonLat[0] * Math.PI / 180.0f, LonLat[1] * Math.PI / 180.0f);
                Geometry poly = gf.createPolygon(coor);
                if (poly.within(mp)) {
                    //���������λ�ڵ������������ڲ������������ڵĵ��λ�ڵ������������ڲ�
                    for (int di = 0; di < 20; di++) {
                        for (int dj = 0; dj < 20; dj++) {
                            odc.chinaGrid[i + di][j + dj] = 0;
                            odc.chinaOneDayGrid[i + di][j + dj] = 0;
                        }
                    }
                } else if (poly.intersects(mp)) {
                    //�����������������������ཻ�����������ж��������ڲ��ĵ��������������Ĺ�ϵ
                    for (int di = 0; di < 20; di++) {
                        for (int dj = 0; dj < 20; dj++) {
                            GridXY[0] = j + dj;
                            GridXY[1] = i + di;
                            OverlapFun.GridXY2LonLat(GridXY, LonLat);
                            Coordinate coor1 = new Coordinate(LonLat[0] * Math.PI / 180.0, LonLat[1] * Math.PI / 180.0);
                            Point p = gf.createPoint(coor1);
                            if (mp.contains(p)) {
                                odc.chinaGrid[i + di][j + dj] = 0;
                                odc.chinaOneDayGrid[i + di][j + dj] = 0;
                            } else {
                                odc.chinaGrid[i + di][j + dj] = -10;
                                odc.chinaOneDayGrid[i + di][j + dj] = -10;
                            }
                        }
                    }
                } else {
                    //���������λ�ڵ������������ⲿ�����������ڵĵ��λ�ڵ������������ⲿ
                    for (int di = 0; di < 20; di++) {
                        for (int dj = 0; dj < 20; dj++) {
                            odc.chinaGrid[i + di][j + dj] = -10;
                            odc.chinaOneDayGrid[i + di][j + dj] = -10;                           
                        }
                    }
                }
            }
        }
        //ͳ�Ƶ��������������������
        int totalGrid = 0;
        for (int i = 0; i < 800; i++) {
            for (int j = 0; j < 1400; j++) {
                if (odc.chinaGrid[i][j] != -10) {
                    totalGrid++;
                }
            }
        }
        return totalGrid;
    }

    public byte[] Grid2png(int[][] chinaGrid, float[][] effectiveTimesGrid) {

        int width = 1400;
        int height = 800;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = image.createGraphics();

        Color color1 = new Color(0, 255, 0, 255);
        Color color2 = new Color(255, 255, 0, 255);
        Color color3 = new Color(0, 0, 255, 255);
        Color color4 = new Color(255, 0, 0, 255);

        for (int i = 0; i < 800; i++) {
            for (int j = 0; j < 1400; j++) {
                if (chinaGrid[i][j] != -10) {
                    if (effectiveTimesGrid[i][j] < 0.5f) {
                        g2d.setColor(color4);
                    } else if (effectiveTimesGrid[i][j] < 1.0f) {
                        g2d.setColor(color3);
                    } else if (effectiveTimesGrid[i][j] < 1.5f) {
                        g2d.setColor(color2);
                    } else {
                        g2d.setColor(color1);
                    }
                    g2d.fillRect(j, i, 1, 1);
                }
            }
        }
        byte[] bt = null;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bout);
            bt = bout.toByteArray();
            ByteArrayInputStream bin = new ByteArrayInputStream(bt);
            image = ImageIO.read(bin);
//            ImageIO.write(image, "png", new File("E:\\����\\a.png"));
//            System.out.println("");
        } catch (Exception ex) {
            System.out.println("����PNGͼƬʧ��");
        }
        g2d.dispose();
        return bt;
        
/*
        int width = 700;
        int height = 400;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = image.createGraphics();

        Color color1 = new Color(0, 255, 0, 128);
        Color color2 = new Color(255, 255, 0, 128);
        Color color3 = new Color(0, 0, 255, 128);
        Color color4 = new Color(255, 0, 0, 128);

        for (int i = 0; i < 800; i++) {
            for (int j = 0; j < 1400; j++) {
                if (i % 2 == 0 && j % 2 == 0 && chinaGrid[i][j] != -10) {
                    if (effectiveTimesGrid[i][j] < 0.5f) {
                        g2d.setColor(color4);
                    } else if (effectiveTimesGrid[i][j] < 1.0f) {
                        g2d.setColor(color3);
                    } else if (effectiveTimesGrid[i][j] < 1.5f) {
                        g2d.setColor(color2);
                    } else {
                        g2d.setColor(color1);
                    }
                    g2d.fillRect(j / 2, i / 2, 2, 2);
                }
            }
        }
        byte[] bt = null;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bout);
            bt = bout.toByteArray();
            ByteArrayInputStream bin = new ByteArrayInputStream(bt);
            image = ImageIO.read(bin);
//            ImageIO.write(image, "png", new File("E:\\����\\b.png"));
//            System.out.println("");
        } catch (Exception ex) {
            System.out.println("����PNGͼƬʧ��");
        }
        g2d.dispose();
        return bt;
        */
    }
}

class XYindex implements Comparable<XYindex> {
    int x;
    int y;
    static int dataGrid[][] = null;
    public XYindex(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public int compareTo(XYindex xy) {
        if (dataGrid[this.x][this.y] > dataGrid[xy.x][xy.y]) {
            return 1;
        } else if (dataGrid[this.x][this.y] == dataGrid[xy.x][xy.y]) {
            return 0;
        } else {
            return -1;
        }
    }
}











