/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import display.util.DisplayController;
import display.util.SatelliteElem;
import display.util.ShotUnit;
import coverage.OneDayCoverage;
import coverage.iostruct.PositionVelocityOutput;
import coverage.iostruct.ProcessResource;
import coverage.iostruct.SatelliteInput;
import coverage.util.CoorTrans;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwindx.applications.worldwindow.core.Controller;
import java.awt.*;
import javax.swing.*;
import coverage.util.Time;
import gov.nasa.worldwind.geom.Position;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.JButton;
import simulationpanel.RegionManagerClass;

/**
 *
 * @author ZZL
 */
public class MyTestDialog extends JDialog
{
    Controller controller;
    WorldWindow wwd;
    RenderableLayer displayLayer;

    DisplayController displayController;

    public MyTestDialog(Controller c, Frame owner)
    {
        super(owner);
        this.controller = c;
        this.wwd = c.getWWd();

        displayLayer = (RenderableLayer) wwd.getModel().getLayers().getLayerByName("三维显示");

        displayController = this.CalDisplayController();
        displayController.Reset();

        Box box = Box.createVerticalBox();
        JButton btn;

        btn = new JButton("开始");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayController.Start();
            }
        });
        box.add(btn);

        btn = new JButton("暂停");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayController.Suspend();
            }
        });
        box.add(btn);

        btn = new JButton("重置");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayController.Reset();
            }
        });
        box.add(btn);

        btn = new JButton("加速");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int speed = displayController.GetSpeed();
                if (speed <= 8)
                {
                    displayController.SetSpeed(speed * 2);
                }
            }
        });
        box.add(btn);

        btn = new JButton("减速");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int speed = displayController.GetSpeed();
                if (speed > 1)
                {
                    displayController.SetSpeed(speed / 2);
                }
            }
        });
        box.add(btn);

        btn = new JButton("+ 1s");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayController.NextFrame();
            }
        });
        box.add(btn);

        btn = new JButton("+10s");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayController.ChangeFrame(10);
                //wwd.redraw();
            }
        });
        box.add(btn);

        btn = new JButton("- 1s");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayController.LastFrame();
                //wwd.redraw();
            }
        });
        box.add(btn);

        btn = new JButton("-10s");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayController.ChangeFrame(-10);
                //wwd.redraw();
            }
        });
        box.add(btn);

        this.add(box);

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(300, 400));
        this.pack();
    }

    private ShotUnit GetShotElem(PositionVelocityOutput[] pvo, Time shotStartTime, Time shotEndTime, double swingAngle, double fov, double maxSwingAngle, int type)
    {
        ArrayList<Position> leftPosList = new ArrayList<Position>();
        ArrayList<Position> rightPosList = new ArrayList<Position>();
        ArrayList<Position> leftMaxPosList = new ArrayList<Position>();
        ArrayList<Position> rightMaxPosList = new ArrayList<Position>();

        int type_index = 0;
        double swing_type2[] = new double[]
        {
            0.0, 2.1, 4.1, 5.9, 7.4, 8.7, 9.5, 9.9, 9.9, 9.5, 8.7, 7.4, 5.9, 4.1, 2.1, 0.0, -2.1, -4.1, -5.9, -7.4, -8.7, -9.5, -9.9, -9.9, -9.5, -8.7, -7.4, -5.9, -4.1, -2.1
        };
        double swing_type3[] = new double[]
        {
            -10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -10, -9, -8, -7, -6, -5, -4, -3, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, - 2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2
        };

        for (PositionVelocityOutput pvo1 : pvo)
        {
            if (pvo1.Time.afterOrEqual(shotStartTime) && pvo1.Time.beforeOrEqual(shotEndTime))
            {
                double[] r1;
                double[] v1;
                double[] LonLatLeft = new double[2];
                double[] LonLatRight = new double[2];

                type_index++;
                r1 = new double[]
                {
                    pvo1.x_J2000C, pvo1.y_J2000C, pvo1.z_J2000C
                };
                v1 = new double[]
                {
                    pvo1.vx_J2000C, pvo1.vy_J2000C, pvo1.vz_J2000C
                };
                if (type == 1)
                {
                    LonLatLeft = CoorTrans.getScanLatLon(r1, v1, swingAngle + fov / 2, 0);     //左侧 扫描边界与地球交点经纬度
                    LonLatRight = CoorTrans.getScanLatLon(r1, v1, swingAngle - fov / 2, 0);    //右侧 扫描边界与地球交点经纬度
                } else if (type == 2)
                {
                    swingAngle = swing_type2[type_index % swing_type2.length];
                    LonLatLeft = CoorTrans.getScanLatLon(r1, v1, swingAngle + fov / 2, 0);     //左侧 扫描边界与地球交点经纬度
                    LonLatRight = CoorTrans.getScanLatLon(r1, v1, swingAngle - fov / 2, 0);    //右侧 扫描边界与地球交点经纬度
                } else//type=3;
                {
                    swingAngle = swing_type3[type_index % swing_type3.length];
                    LonLatLeft = CoorTrans.getScanLatLon(r1, v1, swingAngle + fov / 2, 0);     //左侧 扫描边界与地球交点经纬度
                    LonLatRight = CoorTrans.getScanLatLon(r1, v1, swingAngle - fov / 2, 0);    //右侧 扫描边界与地球交点经纬度
                }
                leftPosList.add(Position.fromDegrees(LonLatLeft[1], LonLatLeft[0], 0));
                rightPosList.add(Position.fromDegrees(LonLatRight[1], LonLatRight[0], 0));

                LonLatLeft = CoorTrans.getScanLatLon(r1, v1, maxSwingAngle + fov / 2, 0);     //左侧 最大扫描边界与地球交点经纬度
                LonLatRight = CoorTrans.getScanLatLon(r1, v1, -maxSwingAngle - fov / 2, 0);   //右侧 最大扫描边界与地球交点经纬度

                leftMaxPosList.add(Position.fromDegrees(LonLatLeft[1], LonLatLeft[0], 0));
                rightMaxPosList.add(Position.fromDegrees(LonLatRight[1], LonLatRight[0], 0));
            }
        }

        ShotUnit shotElem = new ShotUnit();
        shotElem.startTime = shotStartTime.clone();
        shotElem.endTime = shotEndTime.clone();
        shotElem.leftMaxPosArray = (Position[]) leftMaxPosList.toArray(new Position[leftMaxPosList.size()]);
        shotElem.leftPosArray = (Position[]) leftPosList.toArray(new Position[leftPosList.size()]);
        shotElem.rightMaxPosArray = (Position[]) rightMaxPosList.toArray(new Position[rightMaxPosList.size()]);
        shotElem.rightPosArray = (Position[]) rightPosList.toArray(new Position[rightPosList.size()]);

        return shotElem;
    }

    private DisplayController CalDisplayController()
    {
        Time simuStartTime = new Time("2014-08-09 09:15:00.000");
        Time simuEndTime = new Time("2014-08-09 09:25:00.000");

        DisplayController dc = new DisplayController(wwd, displayLayer);

        dc.isShowSatellite = true;
        dc.isShowSatelliteOrbit = true;
        dc.isShowSensor = true;
        dc.isShowCourageRange = true;
        dc.isShowMaxCourageRange = true;
        dc.isShowGroundRegion = true;

        dc.satelliteElemList = new ArrayList<SatelliteElem>();
        //displayControl.satelliteElemArray[0] = satelliteElem;
        dc.startTime = simuStartTime.clone();
        dc.endTime = simuEndTime.clone();
        dc.ground = RegionManagerClass.Shape2MultiPolygon("file\\squ3.shp");

        SatelliteElem satelliteElem;
        ShotUnit shotUnit;

        HashMap<String, String> tleMap = ProcessResource.ReadTle("src\\resource\\tle.txt");
        String satelliteXml;
        SatelliteInput sli;
        PositionVelocityOutput[] pvo;

        //第一颗卫星
        satelliteElem = new SatelliteElem();
        satelliteElem.satelliteName = "Satellite 01";
        satelliteElem.startTime = dc.startTime.clone();
        satelliteElem.endTime = dc.endTime.clone();

        satelliteXml = "zy02c.xml";
        sli = ProcessResource.ReadSatellite("src\\resource\\satellite\\" + satelliteXml, tleMap);
        Time endTimePlus1s = dc.endTime.clone();
        endTimePlus1s.addSeconds(1);
        pvo = OneDayCoverage.calPosition(sli, dc.startTime.clone(), endTimePlus1s, 1);

        satelliteElem.satellitePosArray = new Position[pvo.length];
        for (int i = 0; i < pvo.length; i++)
        {
            satelliteElem.satellitePosArray[i] = Position.fromDegrees(pvo[i].Lat, pvo[i].Lon, pvo[i].Alt);
        }

        satelliteElem.shotUnitList = new ArrayList<ShotUnit>();

        shotUnit = GetShotElem(pvo, new Time("2014-08-09 09:15:05.000"), new Time("2014-08-09 09:16:00.000"), 10, 8, 25, 1);
        satelliteElem.shotUnitList.add(shotUnit);
        shotUnit = GetShotElem(pvo, new Time("2014-08-09 09:15:05.000"), new Time("2014-08-09 09:16:00.000"), 10, 8, 25, 3);
        satelliteElem.shotUnitList.add(shotUnit);

        dc.satelliteElemList.add(satelliteElem);

        //第二颗卫星
        satelliteElem = new SatelliteElem();
        satelliteElem.satelliteName = "Satellite 2";
        satelliteElem.startTime = dc.startTime.clone();
        satelliteElem.endTime = dc.endTime.clone();

        satelliteXml = "worldview2.xml";
        sli = ProcessResource.ReadSatellite("src\\resource\\satellite\\" + satelliteXml, tleMap);
        endTimePlus1s = dc.endTime.clone();
        endTimePlus1s.addSeconds(1);
        pvo = OneDayCoverage.calPosition(sli, dc.startTime.clone(), endTimePlus1s, 1);

        satelliteElem.satellitePosArray = new Position[pvo.length];
        for (int i = 0; i < pvo.length; i++)
        {
            satelliteElem.satellitePosArray[i] = Position.fromDegrees(pvo[i].Lat, pvo[i].Lon, pvo[i].Alt);
        }

        satelliteElem.shotUnitList = new ArrayList<ShotUnit>();

        shotUnit = GetShotElem(pvo, new Time("2014-08-09 09:15:20.000"), new Time("2014-08-09 09:17:00.000"), 8, 20, 30, 2);

        satelliteElem.shotUnitList.add(shotUnit);
        

        dc.satelliteElemList.add(satelliteElem);

        //第三颗卫星
        satelliteElem = new SatelliteElem();
        satelliteElem.satelliteName = "Satellite 03";
        satelliteElem.startTime = dc.startTime.clone();
        satelliteElem.endTime = dc.endTime.clone();

        satelliteXml = "spot7.xml";
        sli = ProcessResource.ReadSatellite("src\\resource\\satellite\\" + satelliteXml, tleMap);
        endTimePlus1s = dc.endTime.clone();
        endTimePlus1s.addSeconds(1);
        pvo = OneDayCoverage.calPosition(sli, dc.startTime.clone(), endTimePlus1s, 1);

        satelliteElem.satellitePosArray = new Position[pvo.length];
        for (int i = 0; i < pvo.length; i++)
        {
            satelliteElem.satellitePosArray[i] = Position.fromDegrees(pvo[i].Lat, pvo[i].Lon, pvo[i].Alt);
        }

        satelliteElem.shotUnitList = new ArrayList<ShotUnit>();

        shotUnit = GetShotElem(pvo, new Time("2014-08-09 09:15:02.000"), new Time("2014-08-09 09:16:07.000"), 8, 20, 30, 3);
        satelliteElem.shotUnitList.add(shotUnit);

        dc.satelliteElemList.add(satelliteElem);

        return dc;

    }

}
