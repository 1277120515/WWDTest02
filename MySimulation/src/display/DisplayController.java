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
        //��ʾ���ǹ��
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
             //��ǰʱ��currentTimeδ�ﵽpassDisplay�Ŀ�ʼʱ�䣺����ʾ���
             if (isShowSatelliteOrbit == true)
                {

                }

            } else if (startTime.before(currentTime) && currentTime.before(endTime))
            {
                //��ǰʱ��currentTime��passDisplay����ֹʱ��֮�䣺��ʾ�������ǰ���������ǣ���ǰ����������󸲸�����

                



            } else
            {
                //��ǰʱ��currentTime����passDisplay�Ľ���ʱ�䣺��ʾ������Ѿ���������

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
