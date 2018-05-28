/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wwdtest02;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.FPSAnimator;


/**
 *
 * @author ZZL
 */
public class OGTest implements GLEventListener {
    
    private GLU glu=new GLU();

    @Override
    public void init(GLAutoDrawable glad) {
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
    }

    @Override
    public void display(GLAutoDrawable glad) {

        GL2 gl = glad.getGL().getGL2();
        
        gl.glTranslatef(0.25f,0,0);

        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(0.5f, -0.5f, 0);
        gl.glVertex3f(-0.5f, 0.5f, 0);
        gl.glEnd();

        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3f(-0.75f, 0f, 3f);
        gl.glVertex3f(0f, -0.75f, 3f);
        gl.glEnd();

    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int y, int width, int height) {
        GL2 gl=glad.getGL().getGL2();
        gl.glViewport(0,0,width,height);
        gl.glMatrixMode((GL2.GL_PROJECTION));
        gl.glLoadIdentity();
        //gl.
    }
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        GLProfile profile=GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities=new GLCapabilities(profile);
        GLCanvas glcanvas=new GLCanvas(capabilities);
        
        OGTest b=new OGTest();
        glcanvas.addGLEventListener(b);;
        glcanvas.setSize(400,400);
        JFrame frame=new JFrame("Basic Frame");
        frame.add(glcanvas);
        frame.setSize(frame.getPreferredSize());
        frame.setVisible(true);
    }
    
    
    
}
