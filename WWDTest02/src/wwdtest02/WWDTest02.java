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
import gov.nasa.worldwind.render.Box;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import static gov.nasa.worldwindx.examples.ApplicationTemplate.insertBeforeCompass;
import gov.nasa.worldwindx.examples.Boxes;

/**
 *
 * @author ZZL
 */
public class WWDTest02 extends ApplicationTemplate {

    public static class AppFrame extends ApplicationTemplate.AppFrame {

        public AppFrame() {
            RenderableLayer layer = new RenderableLayer();

            MyRender mr=new MyRender();
            layer.addRenderable(mr);
            
            // Add the layer to the model.
            insertBeforeCompass(getWwd(), layer);
        }
    }

    public static void main(String[] args) {
        ApplicationTemplate.start("WorldWind Boxes", WWDTest02.AppFrame.class);
    }

}
