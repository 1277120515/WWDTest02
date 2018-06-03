/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display.iostruct;

import com.vividsolutions.jts.geom.Geometry;
import coverage.util.Time;
import display.DisplayConfig;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author ZZL
 */
public class SatelliteElem
{

    public String satelliteName;
    //public Geometry ground;
    public Time startTime;
    public Time endTime;
    public Position[] satellitePosArray;

    public ArrayList<ShotElem> shotElemList;

    public void ShowOrbit(RenderableLayer layer, Time currentTime)
    {
        ShapeAttributes attrs = new BasicShapeAttributes();
        attrs.setDrawInterior(false);
        attrs.setInteriorMaterial(new Material(DisplayConfig.orbitColor));
        attrs.setInteriorOpacity(0.1);
        attrs.setDrawOutline(true);
        attrs.setOutlineMaterial(new Material(DisplayConfig.orbitColor));
        attrs.setOutlineWidth(DisplayConfig.orbitWidth);
        attrs.setOutlineOpacity(1);

        ArrayList<Position> pathPositions = new ArrayList<Position>();
        int index = 0;
        for (Time tempTime = startTime.clone(); tempTime.beforeOrEqual(currentTime) && tempTime.beforeOrEqual(endTime); tempTime.addSeconds(1))
        {
            pathPositions.add(satellitePosArray[index]);
            index++;
        }

        Path path = new Path(pathPositions);
        path.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        path.setHighlightAttributes(attrs);
        path.setAttributes(attrs);

        layer.addRenderable(path);
    }



}
