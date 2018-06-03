/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display.iostruct;

import coverage.util.Time;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;

/**
 *
 * @author ZZL
 */
public class ShotElem
{
    public Time startTime;
    public Time endTime;
    public Position[] leftMaxPosArray;
    public Position[] rightMaxPosArray;
    public Position[] leftPosArray;
    public Position[] rightPosArray;

    public void ShowSensor(RenderableLayer layer, Time currentTime)
    {

    }

    public void ShowCourageRange(RenderableLayer layer, Time currentTime)
    {

    }

    public void ShowMaxCourageRange(RenderableLayer layer, Time currentTime)
    {

    }
}
