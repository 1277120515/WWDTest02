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
                //�������еĹ�������
                Time startTime = satelliteElem.startTime;
                Time endTime = satelliteElem.endTime;

                if (currentTime.before(startTime))
                {
                    //��ǰʱ��currentTimeδ�ﵽpassDisplay�Ŀ�ʼʱ�䣺����ʾ���
                    if (isShowSatelliteOrbit == true)
                    {

                    }

                } else if (startTime.before(currentTime) && currentTime.before(endTime))
                {
                    //��ǰʱ��currentTime��passDisplay����ֹʱ��֮�䣺��ʾ�������ǰ���������ǣ���ǰ����������󸲸�����
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
                    //��ǰʱ��currentTime����passDisplay�Ľ���ʱ�䣺��ʾ������Ѿ���������

                }



            }
        }
    }
}
