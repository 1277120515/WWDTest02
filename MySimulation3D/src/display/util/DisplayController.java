/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display.util;


import coverage.util.Time;
import gov.nasa.worldwind.layers.RenderableLayer;
import com.vividsolutions.jts.geom.MultiPolygon;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.ScreenRelativeAnnotation;
import gov.nasa.worldwind.render.SurfacePolygon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.*;
import javax.swing.JOptionPane;
import simulationpanel.RegionManagerClass;

/**
 *
 * @author ZZL
 */
public class DisplayController
{
    public boolean isShowSatelliteOrbit = false;    //�Ƿ���ʾ���ǹ��
    public boolean isShowSensor = false;            //�Ƿ���ʾ����������
    public boolean isShowGroundRegion = false;      //�Ƿ���ʾ������������
    public boolean isShowMaxCourageRange = false;   //�Ƿ���ʾ������㷶Χ
    public boolean isShowCourageRange = false;      //�Ƿ���ʾ��ǰ���������Ƿ�Χ
    public boolean isShowSatellite = false;         //�Ƿ���ʾ����

    public ArrayList<SatelliteElem> satelliteElemList;  //�����б�
    public MultiPolygon ground;                         //������������

    /**
     * ���㳡����ʼʱ��
     */
    public Time startTime;

    /**
     *���㳡������ʱ��
     */
    public Time endTime;

    private RenderableLayer displayLayer;//WWD 3D����ͼ��
    WorldWindow wwd;

    /**
     *
     * @param wwd WorldWind
     * @param layer WWD 3D����ͼ��
     */
    public DisplayController(WorldWindow wwd, RenderableLayer layer)
    {
        displayLayer = layer;
        this.wwd = wwd;

        InitTimer();//��ʼ����ʱ��
    }

    /**
     * ��ǰʱ�� 
     */
    public Time currentTime;

    private boolean isRun = false;//�Ƿ��ڷ������״̬ true�����ڽ��з��� false����ͣ
    private int speed = 1; //�����ٶ�  

    //��ʱ����ʼ����ÿ��ִ��һ��
    private void InitTimer()
    {
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                //�ú���ÿ��ִ��һ��
                if (isRun == true)          //ͨ����ѯisRun��ֵ���ж��Ƿ��л�֡
                {
                    ChangeFrame(speed);     //�л�֡
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);      //��ʱ���1��
    }

    /**
     *
     * @param s �����ٶ�
     */
    public void SetSpeed(int s)
    {
        speed = s;
    }

    /**
     *
     * @return �����ٶ�
     */
    public int GetSpeed()
    {
        return speed;
    }

    /**
     * ���濪ʼ
     */
    public void Start()
    {
        isRun = true;
    }

    /**
     * ������ͣ
     */
    public void Suspend()
    {
        isRun = !isRun;
    }

    /**
     * �������á����س�ʼ״̬
     */
    public void Reset()
    {
        isRun = false;
        speed = 1;
        currentTime = startTime.clone();
        display();
    }

    /**
     *
     * @param second
     */
    public void ChangeFrame(int second)
    {
        if (second >= 0)
        {
            currentTime.addSeconds(second);
            if (currentTime.after(endTime))
            {
                currentTime.addSeconds(-second);    //��ǰʱ�����second��
            }
        } else
        {
            currentTime.addSeconds(second);
            if (currentTime.before(startTime))
            {
                currentTime.addSeconds(-second);//��ǰʱ������second��
            }
        }
        display();
    }

    /**
     * ��һ֡
     */
    public void NextFrame()
    {
        ChangeFrame(1);
    }

    /**
     * ��һ֡
     */
    public void LastFrame()
    {
        ChangeFrame(-1);
    }

     /**
     * ����startTime currentTime endTime�Ƿ�Ϸ���Ӧ���㣺
     * DisplayController.startTime == SatelliteElem.startTime <= DisplayController.currentTime <= DisplayController.endTime == SatelliteElem.EndTime
     */
    private boolean CheckTime()
    {
        if (startTime.afterOrEqual(endTime))
        {
            JOptionPane.showMessageDialog(null, "DisplayController.startTimeӦ����DisplayController.endTime��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (currentTime.after(endTime) || currentTime.before(startTime))
        {
            JOptionPane.showMessageDialog(null, "currentTime��Ӧ����DisplayController.startTime����Ӧ����DisplayController.endTime��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        for (SatelliteElem satelliteElem : satelliteElemList)
        {
            if (!(satelliteElem.startTime.equal(this.startTime) && satelliteElem.endTime.equal(this.endTime)))
            {
                JOptionPane.showMessageDialog(null, "satelliteElem����ֹʱ��Ӧ��DisplayController����ֹʱ����ͬ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            for (ShotUnit shotUnit : satelliteElem.shotUnitList)
            {
                if (shotUnit.startTime.before(satelliteElem.startTime) || shotUnit.endTime.after(satelliteElem.endTime))
                {
                    JOptionPane.showMessageDialog(null, "shotUnit.startTime��Ӧ����satelliteElem.startTime��shotUnit.endTime��Ӧ����satelliteElem.endTime��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
                if (shotUnit.startTime.afterOrEqual(shotUnit.endTime))
                {
                    JOptionPane.showMessageDialog(null, "shotUnit.startTimeӦ����satelliteElem.endTime��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }

     /**
     * ��ʾ����
     */
    private void display()
    {
        if (CheckTime() == false)//�жϼ���ʱ���Ƿ�Ϸ�
        {
            return;
        }
        displayLayer.removeAllRenderables();//ȥ����һ֡����������

        //��ʾ������������
        if (isShowGroundRegion == true)
        {
            SurfacePolygon[] spArray = RegionManagerClass.MultiPolygon2SurfacePolygon(ground);
            for (int i = 0; i < spArray.length; i++)
            {
                displayLayer.addRenderable(spArray[i]);
            }
        }

        if (satelliteElemList != null && satelliteElemList.size() > 0)
        {
            for (SatelliteElem satelliteElem : satelliteElemList)
            {

                //����ÿһ������
                //�Ƿ���ʾ���ǹ켣
                if (isShowSatelliteOrbit == true)
                {
                    satelliteElem.ShowOrbit(displayLayer, currentTime);
                }

                //�Ƿ���ʾ���Ǻ�����
                if (isShowSatellite == true)
                {
                    satelliteElem.ShowSatellite(displayLayer, currentTime);
                }

                //�Ƿ���ʾ��������󸲸Ƿ�Χ
                if (isShowMaxCourageRange == true)
                {
                    satelliteElem.ShowMaxCourageRange(displayLayer, currentTime);
                }

                //�Ƿ���ʾ����
                if (isShowCourageRange == true)
                {
                    satelliteElem.ShowCourageRange(displayLayer, currentTime);
                }
                //�Ƿ���ʾ����������
                if (isShowSensor == true)
                {
                    satelliteElem.ShowMaxSensorTriangle(displayLayer, currentTime);
                    satelliteElem.ShowSensorTriangle(displayLayer, currentTime);
                }

            }
        }
        //�ػ�
        wwd.redraw();
    }
}
