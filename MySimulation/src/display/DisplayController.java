/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package display;

import coverage.util.Time;
import gov.nasa.worldwind.layers.RenderableLayer;
import display.iostruct.*;

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
    
    private RenderableLayer displayLayer;

    public DisplayController(RenderableLayer layer)
    {
        displayLayer = layer;
    }

    

    public Time currentTime;
    void display()
    {
        if (satelliteElemArray != null && satelliteElemArray.length > 0)
        {
            for (SatelliteElem satelliteElem : satelliteElemArray)
            {
                //遍历所有的过境场景
                Time startTime = satelliteElem.startTime;
                Time endTime = satelliteElem.endTime;

                if (currentTime.before(startTime))
                {
                    //当前时间currentTime未达到passDisplay的开始时间：仅显示轨道
                    if (isShowSatelliteOrbit == true)
                    {

                    }

                } else if (startTime.before(currentTime) && currentTime.before(endTime))
                {
                    //当前时间currentTime在passDisplay的起止时间之间：显示轨道，当前传感器三角，当前覆盖区域，最大覆盖区域
                    if (isShowSatelliteOrbit == true)
                    {
                        satelliteElem.DisplayOrbit(displayLayer, currentTime);
                    }
                    if (isShowSensor == true)
                    {

                    }
                    if (isShowGroundRegion == true)
                    {

                    }
                    if (isShowMaxCourageRange == true)
                    {

                    }
                    if (isShowCourageRange == true)
                    {

                    }

                } else
                {
                    //当前时间currentTime超过passDisplay的结束时间：显示轨道，已经覆盖区域

                }



            }
        }
    }
}
