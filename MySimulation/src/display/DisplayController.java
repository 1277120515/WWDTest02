/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import coverage.util.Time;
import gov.nasa.worldwind.layers.RenderableLayer;
import display.iostruct.*;
import com.vividsolutions.jts.geom.Geometry;

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

    public SatelliteElem[] satelliteElemArray;
    public Geometry ground;

    public Time startTime;
    public Time endTime;

    private RenderableLayer displayLayer;

    public DisplayController(RenderableLayer layer)
    {
        displayLayer = layer;
    }

    public void Restart()
    {
        currentTime =// startTime.clone();
                new Time("2014-08-01 00:15:06.000");
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

    private Time currentTime;
    private void display()
    {
        displayLayer.removeAllRenderables();

        if (satelliteElemArray != null && satelliteElemArray.length > 0)
        {
            for (SatelliteElem satelliteElem : satelliteElemArray)
            {

                //遍历每一颗卫星
                
                //是否显示卫星轨迹
                if (isShowSatelliteOrbit == true)
                {
                    satelliteElem.ShowOrbit(displayLayer, currentTime);
                }

                //是否显示卫星和名称
                if (isShowSatellite == true)
                {
                    satelliteElem.ShowSatellite(displayLayer, currentTime);
                }

                //是否显示传感器三角
                if (isShowSensor == true)
                {
                    satelliteElem.ShowMaxSensorTriangle(displayLayer, currentTime);
                    satelliteElem.ShowSensorTriangle(displayLayer, currentTime);
                }

                //是否显示条带
                if (isShowCourageRange == true)
                {
                    satelliteElem.ShowCourageRange(displayLayer, currentTime);
                }

            }
        }

        if (isShowGroundRegion == true)
        {

        }
        /////////////////////////////////////////////////////////

        if (isShowMaxCourageRange == true)
        {

        }

    }
}
