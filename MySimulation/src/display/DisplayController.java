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

    private RenderableLayer displayLayer;

    public DisplayController(RenderableLayer layer)
    {
        displayLayer = layer;
    }



    public Time currentTime;

    void display()
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


        if (isShowSensor == true)
        {
            if (satelliteElemArray != null && satelliteElemArray.length > 0)
            {
                for (SatelliteElem satelliteElem : satelliteElemArray)
                {
                    satelliteElem.ShowSensor(displayLayer, currentTime);
                }
            }
            /*
             if (currentTime.before(startTime))
             {
             //当前时间currentTime未达到passDisplay的开始时间：仅显示轨道
             if (isShowSatelliteOrbit == true)
                {

                }

            } else if (startTime.before(currentTime) && currentTime.before(endTime))
            {
                //当前时间currentTime在passDisplay的起止时间之间：显示轨道，当前传感器三角，当前覆盖区域，最大覆盖区域

                



            } else
            {
                //当前时间currentTime超过passDisplay的结束时间：显示轨道，已经覆盖区域

            }
                    */

        }

        if (isShowMaxCourageRange == true)
        {

        }
        if (isShowCourageRange == true)
        {

        }



    }
}
