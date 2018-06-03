/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display.iostruct;

import coverage.util.Time;
import display.DisplayConfig;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;
import java.util.ArrayList;

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

    
}
