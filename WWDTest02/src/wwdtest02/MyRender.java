/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wwdtest02;

import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import javax.media.opengl.GL2;

/**
 *
 * @author yf
 */
public class MyRender implements Renderable {

    @Override
    public void render(DrawContext dc) {

        GL2 gl = dc.getGL().getGL2();
        gl.glTranslatef(0.25f, 0, 0);

        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(0.5f, -0.5f, 0);
        gl.glVertex3f(-0.5f, 0.5f, 0);
        gl.glEnd();

        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(-0.75f, 0f, 3f);
        gl.glVertex3f(0f, -0.75f, 3f);
        gl.glEnd();
    }

}
