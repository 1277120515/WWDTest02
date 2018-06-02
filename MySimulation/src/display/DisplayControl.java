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
public class DisplayControl
{
    

    public boolean isShowSatelliteOrbit = false;
    public boolean isShowSensor = false;
    public boolean isShowGroundRegion = false;
    public boolean isShowMaxCourageRange = false;
    public boolean isShowCourageRange = false;
    
    public PassDisplay[] passDisplayArray;
    
    private RenderableLayer displayLayer;

    public DisplayControl(RenderableLayer layer)
    {
        displayLayer = layer;
    }

    

    public Time currentTime;
    void display()
    {
        if (passDisplayArray != null && passDisplayArray.length > 0)
        {
            for (PassDisplay passDisplay : passDisplayArray)
            {
                //�������еĹ�������
                Time startTime = passDisplay.startTime;
                Time endTime = passDisplay.endTime;

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
