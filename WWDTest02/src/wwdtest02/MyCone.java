/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wwdtest02;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Cone;
import gov.nasa.worldwind.render.Ellipsoid;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import static gov.nasa.worldwindx.examples.ApplicationTemplate.insertBeforeCompass;


/**
 *
 * @author ZZL
 */
public class MyCone extends ApplicationTemplate
{
     public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            // Add detail hint slider panel
            //this.getLayerPanel().add(makeDetailHintControlPanel(), BorderLayout.SOUTH);

            RenderableLayer layer = new RenderableLayer();

            // Create and set an attribute bundle.
            ShapeAttributes attrs = new BasicShapeAttributes();
            attrs.setInteriorMaterial(Material.GREEN);
            attrs.setInteriorOpacity(0.5);
            attrs.setEnableLighting(true);
            //attrs.setOutlineMaterial(Material.RED);
            //attrs.setOutlineWidth(2d);
            attrs.setDrawInterior(true);
            attrs.setDrawOutline(false);

ShapeAttributes attrs2 = new BasicShapeAttributes();
            attrs2.setInteriorMaterial(Material.PINK);
            attrs2.setInteriorOpacity(1);
            attrs2.setEnableLighting(true);
            attrs2.setOutlineMaterial(Material.WHITE);
            attrs2.setOutlineWidth(2d);
            attrs2.setDrawOutline(false);

            double groundPosLon = 120;
            double groundPosLat = 40;
            double groundPosHeight = 1000e3;

            double groundX = 0;
            double groundY = 0;
            double groundZ = 0;

            double satX = 0;
            double satY = 0;
            double satZ = 0;

            double tilt = Math.atan2(satY, satZ) - Math.atan2(groundY, groundZ);
            double roll = Math.atan2(satX, Math.sqrt(satZ * satZ + satY * satY)) - Math.atan2(satX, Math.sqrt(groundZ * groundZ + groundY * groundY));

            Ellipsoid ellipsoid3 = new Ellipsoid(Position.fromDegrees(groundPosLat, groundPosLon, groundPosHeight), 50000, 50000, 50000);
            ellipsoid3.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            ellipsoid3.setAttributes(attrs2);
            ellipsoid3.setVisible(true);
            //ellipsoid3.setValue(AVKey.DISPLAY_NAME, "Ellipsoid with equal axes, ABSOLUTE altitude mode");
            layer.addRenderable(ellipsoid3);

            // Scaled Cone with a pre-set orientation
            Cone cone2 = new Cone(Position.fromDegrees(groundPosLat, groundPosLon, groundPosHeight), 1000000, 400000, 2000000,
                Angle.fromDegrees(0), Angle.fromDegrees(0), Angle.fromDegrees(0));
//            public Cone(Position centerPosition, double northSouthRadius, double verticalRadius, double eastWestRadius,
//                Angle heading, Angle tilt, Angle roll)
            cone2.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            cone2.setAttributes(attrs);
            cone2.setHighlightAttributes(attrs);
            //cone2.setValue(AVKey.DISPLAY_NAME, "Scaled Cone with a pre-set orientation");
            cone2.setVisible(true);
            
            
            layer.addRenderable(cone2);



            // Scaled Cone with a pre-set orientation
//            Cone cone7 = new Cone(Position.fromDegrees(60, 30, 750000), 1000000, 500000, 100000,
//                Angle.fromDegrees(90), Angle.fromDegrees(45), Angle.fromDegrees(30));
//            cone7.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
//            cone7.setAttributes(attrs2);
//            cone7.setVisible(true);
//            cone7.setValue(AVKey.DISPLAY_NAME, "Scaled Cone with a pre-set orientation");
//            layer.addRenderable(cone7);



            // Add the layer to the model.
            insertBeforeCompass(getWwd(), layer);
            // Update layer panel.
            //this.getLayerPanel().update(this.getWwd());
        }
     }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("World Wind Cones", AppFrame.class);
    }
}
