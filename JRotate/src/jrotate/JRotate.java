/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrotate;

/**
 *
 * @author ZZL
 */
public class JRotate
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // TODO code application logic here

        double x, y, z;
        x = MathUtils.deg2rad * 50;
        y = MathUtils.deg2rad * 50;
        z = MathUtils.deg2rad * 90;

        double[] xyz = new double[]{x, y, z};
        double[][] mat;// = new double[3][3];

        mat = Euler2Mat(xyz);
        xyz = Mat2Euler(mat);

        x = MathUtils.rad2deg * xyz[0];
        y = MathUtils.rad2deg * xyz[1];
        z = MathUtils.rad2deg * xyz[2];

        
        double[] vec=new double[]{1,1,1};
        vec=MathUtils.mult(mat, vec);
        

        System.out.println("End");
    }

    //Å·À­½Ç --> Ğı×ª¾ØÕó(¾ØÕóxÏòÁ¿)
    public static double[][] Euler2Mat(double[] xyz)
    {
        double x, y, z;
        
        x = xyz[0];
        y = xyz[1];
        z = xyz[2];

        double[][] Rx = {{1, 0, 0}, {0, Math.cos(x), -Math.sin(x)}, {0, Math.sin(x), Math.cos(x)}};
        double[][] Ry = {{Math.cos(y), 0, Math.sin(y)}, {0, 1, 0}, {-Math.sin(y), 0, Math.cos(y)}};
        double[][] Rz = {{Math.cos(z), -Math.sin(z), 0}, {Math.sin(z), Math.cos(z), 0}, {0, 0, 1}};
        
        double[][] mat=MathUtils.mult(MathUtils.mult(Rz, Ry),Rx);
        //mat=MathUtils.mult(Rz, Ry);

        return mat;
        //double r11, r12, r13, r21, r22, r23, r31, r32, r33;
        //r11 = Math.cos(y) * Math.cos(z);
        //r12 = Math.sin(x) * Math.sin(y) * Math.cos(z) - Math.cos(x) * Math.sin(z);
        //r13 = Math.cos(x) * Math.sin(y) * Math.cos(z) + Math.sin(x) * Math.sin(z);
        //r21 = Math.cos(y) * Math.sin(z);
        //r22 = Math.sin(x) * Math.sin(y) * Math.sin(z) + Math.cos(x) * Math.cos(z);
        //r23 = Math.cos(x) * Math.sin(y) * Math.sin(z) - Math.sin(x) * Math.cos(z);
        //r31 = -Math.sin(y);
        //r32 = Math.sin(x) * Math.cos(y);
        //r33 = Math.cos(x) * Math.cos(y);
        //mat[0][0] = r11;mat[0][1] = r12;mat[0][2] = r13;mat[1][0] = r21;mat[1][1] = r22;mat[1][2] = r23;mat[2][0] = r31;mat[2][1] = r32;mat[2][2] = r33;
    }

    //Ğı×ª¾ØÕó(¾ØÕóxÏòÁ¿) --> Å·À­½Ç
    public static double[] Mat2Euler(double[][] mat)
    {
        double x, y, z;
        double r11, r12, r13, r21, r22, r23, r31, r32, r33;

        r11 = mat[0][0];
        r12 = mat[0][1];
        r13 = mat[0][2];
        r21 = mat[1][0];
        r22 = mat[1][1];
        r23 = mat[1][2];
        r31 = mat[2][0];
        r32 = mat[2][1];
        r33 = mat[2][2];
        double sy=Math.sqrt(r11*r11+r21*r21);
        if (sy > 1e-6) {
            x = Math.atan2(r32, r33);
            y = Math.atan2(-r31, sy);
            z = Math.atan2(r21, r11);
        } else {
            x = Math.atan2(-r23, r22);
            y = Math.atan2(-r31, sy);
            z = 0;
        }
        return new double[]{x,y,z};
    }
}
