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
    public boolean isShowSatelliteOrbit = false;
    public boolean isShowSensor = false;
    public boolean isShowGroundRegion = false;
    public boolean isShowMaxCourageRange = false;
    public boolean isShowCourageRange = false;
    public boolean isShowSatellite = false;

    public ArrayList<SatelliteElem> satelliteElemList;
    public MultiPolygon ground;

    public Time startTime;
    public Time endTime;

    private RenderableLayer displayLayer;
    WorldWindow wwd;

    public DisplayController(WorldWindow wwd, RenderableLayer layer)
    {
        displayLayer = layer;
        this.wwd = wwd;

        InitTimer();
    }

    public Time currentTime;

    private boolean isRun = false;
    private int speed = 1;

    private void InitTimer()
    {
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                if (isRun == true)
                {
                    ChangeFrame(speed);
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    public void SetSpeed(int s)
    {
        speed = s;
    }

    public int GetSpeed()
    {
        return speed;
    }

    public void Start()
    {
        isRun = true;
    }

    public void Suspend()
    {
        isRun = !isRun;
    }

    public void Reset()
    {
        isRun = false;
        speed = 1;
        currentTime = startTime.clone();
        display();
    }

    public void ChangeFrame(int second)
    {
        if (second >= 0)
        {
            currentTime.addSeconds(second);
            if (currentTime.after(endTime))
            {
                currentTime.addSeconds(-second);
            }
        } else
        {
            currentTime.addSeconds(second);
            if (currentTime.before(startTime))
            {
                currentTime.addSeconds(-second);
            }
        }
        display();
    }

    public void NextFrame()
    {
        ChangeFrame(1);
    }

    public void LastFrame()
    {
        ChangeFrame(-1);
    }

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

    private void display()
    {
        if (CheckTime() == false)
        {
            return;
        }
        displayLayer.removeAllRenderables();

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

        /////////////////////////////////////////////////////////
        ShowInfoBoard();

        wwd.redraw();
    }

    private void ShowInfoBoard()
    {
        String infoString = "��ǰʱ�� : " + currentTime.toBJTime();
        infoString+=String.format("\nCurrent Speed: %d", speed);
        
        AnnotationAttributes attr = new AnnotationAttributes();
        attr.setBackgroundColor(new Color(0.2f, 0.2f, 0.2f, 0f));
        attr.setTextColor(Color.YELLOW);
        attr.setLeaderGapWidth(0);
        attr.setLeader(AVKey.SHAPE_NONE);
        attr.setCornerRadius(0);
        attr.setSize(new Dimension(300, 0));
        attr.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT); // use strict dimension width - 200
        attr.setFont(new Font("����", Font.BOLD, 18));
        attr.setBorderWidth(0);
        attr.setHighlightScale(1);             // No highlighting either

        ScreenRelativeAnnotation infoAnnotation = new ScreenRelativeAnnotation(infoString, 0.99, 0.01);
        infoAnnotation.setKeepFullyVisible(true);
        infoAnnotation.setXMargin(5);
        infoAnnotation.setYMargin(5);
        infoAnnotation.setAttributes(attr);

        displayLayer.addRenderable(infoAnnotation);
    }
}
