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
import java.awt.Color;
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

    public void ShowSensor(RenderableLayer layer, Time currentTime)
    {
        //显示传感器三角
        if (currentTime.afterOrEqual(startTime) && currentTime.beforeOrEqual(endTime))
        {
            int satelliteIndex = 0;

            for (Time tempTime = startTime.clone(); tempTime.beforeOrEqual(currentTime) && tempTime.beforeOrEqual(endTime); tempTime.addSeconds(1))
            {
                if (tempTime.after(currentTime))
                {
                    break;
                }
                satelliteIndex++;
            }
            satelliteIndex--;

            for (ShotElem shotElem : shotElemList)
            {
                if (currentTime.afterOrEqual(shotElem.startTime) && currentTime.beforeOrEqual(shotElem.endTime))
                {
                    int shotIndex = 0;
                    for (Time tempTime = shotElem.startTime.clone(); tempTime.beforeOrEqual(currentTime) && tempTime.beforeOrEqual(shotElem.endTime); tempTime.addSeconds(1))
                    {
                        if (tempTime.after(currentTime))
                        {
                            break;
                        }
                        shotIndex++;
                    }
                    shotIndex--;

                    Polygon pg = new Polygon();
                    pg.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);

                    ShapeAttributes attrs = new BasicShapeAttributes();
                    attrs.setDrawInterior(true);
                    attrs.setInteriorMaterial(new Material(DisplayConfig.sensorInnerColor));
                    attrs.setInteriorOpacity(DisplayConfig.sensorInnerOpacity);
                    attrs.setDrawOutline(true);
                    attrs.setOutlineMaterial(new Material(DisplayConfig.sensorOutlineColor));
                    attrs.setOutlineOpacity(DisplayConfig.sensorOutlineOpacity);
                    pg.setAttributes(attrs);
                    pg.setHighlightAttributes(attrs);

                    ArrayList<Position> posList = new ArrayList<Position>();
                    posList.add(satellitePosArray[satelliteIndex]);
                    posList.add(shotElem.leftPosArray[shotIndex]);
                    posList.add(shotElem.rightPosArray[shotIndex]);
                    pg.setOuterBoundary(posList);
                    layer.addRenderable(pg);

                    //显示扫描线
                    SurfacePolyline sp = new SurfacePolyline();
                    ShapeAttributes attrs2 = new BasicShapeAttributes();
                    attrs2.setDrawOutline(true);
                    attrs2.setOutlineMaterial(new Material(DisplayConfig.scanLineColor));
                    attrs2.setOutlineWidth(DisplayConfig.scanLineWidth);
                    attrs2.setOutlineOpacity(1);
                    sp.setAttributes(attrs2);
                    sp.setHighlightAttributes(attrs2);

                    posList.remove(0);
                    sp.setLocations(posList);
                    layer.addRenderable(sp);
                }
            }

        }

    }

    public void ShowCourageRange(RenderableLayer layer, Time currentTime)
    {

    }

    public void ShowMaxCourageRange(RenderableLayer layer, Time currentTime)
    {

    }

}
