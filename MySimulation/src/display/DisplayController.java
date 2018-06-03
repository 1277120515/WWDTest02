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

    public SatelliteElem[] satelliteElemArray;
    public Geometry ground;
    
    public Time startTime;
    public Time endTime;

    private RenderableLayer displayLayer;

    public DisplayController(RenderableLayer layer)
    {
        displayLayer = layer;
        
        currentTime=startTime.clone();
    }

    public void Restart()
    {
        currentTime = startTime.clone();
        display();
    }

    public void NextFrame()
    {
        currentTime.addSeconds(1);
        if (currentTime.after(endTime))
        {
            currentTime.addSeconds(-1);
        }
        display();
    }

    public void LastFrame()
    {
        currentTime.addSeconds(-1);
        if (currentTime.before(startTime))
        {
            currentTime.addSeconds(1);
        }
        display();
    }



    private Time currentTime;
    private void display()
    {
        //显示卫星轨道
        if (isShowSatelliteOrbit == true)
        {
            if (satelliteElemArray != null && satelliteElemArray.length > 0)
            {
                for (SatelliteElem satelliteElem : satelliteElemArray)
                {
                    satelliteElem.ShowOrbit(displayLayer, currentTime);
                }
            }
        }

        if (isShowGroundRegion == true)
        {

        }
        /////////////////////////////////////////////////////////


        //显示传感器三角
        if (isShowSensor == true)
        {
            if (satelliteElemArray != null && satelliteElemArray.length > 0)
            {
                for (SatelliteElem satelliteElem : satelliteElemArray)
                {
                    satelliteElem.ShowMaxSensorTriangle(displayLayer, currentTime);
                    satelliteElem.ShowSensorTriangle(displayLayer, currentTime);

                }
            }
        }

        if (isShowMaxCourageRange == true)
        {
            
        }
        if (isShowCourageRange == true)
        {
            if (satelliteElemArray != null && satelliteElemArray.length > 0)
            {
                for (SatelliteElem satelliteElem : satelliteElemArray)
                {
                    satelliteElem.ShowCourageRange(displayLayer, currentTime);
                    
                }
            }
        }
        

    }
}
