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
        
        double[] xyz=new double[]{0.1,7.9,0.3};
        double[][] mat=new double[3][];
        mat[0]=new double[3];
        mat[1]=new double[3];
        mat[2]=new double[3];
        
        Euler2Mat(xyz,mat);
        
        Mat2Euler(mat,xyz);
        
        System.out.println("End");
    }

    public static void Euler2Mat(double[] xyz, double[][] mat)
    {
        double x, y, z;
        double r11, r12, r13, r21, r22, r23, r31, r32, r33;
        x = xyz[0];
        y = xyz[1];
        z = xyz[2];
        
        r11 = Math.cos(y) * Math.cos(z);
        r12 = Math.sin(x) * Math.sin(y) * Math.cos(z) - Math.cos(x) * Math.sin(z);
        r13 = Math.cos(x) * Math.sin(y) * Math.cos(z) + Math.sin(x) * Math.sin(z);
        
        r21 = Math.cos(y) * Math.sin(z);
        r22 = Math.sin(x) * Math.sin(y) * Math.sin(z) + Math.cos(x) * Math.cos(z);
        r23 = Math.cos(x) * Math.sin(y) * Math.sin(z) - Math.sin(x) * Math.cos(z);

        r31 = -Math.sin(y);
        r32 = Math.sin(x) * Math.cos(y);
        r33 = Math.cos(x) * Math.cos(y);

        mat[0][0] = r11;
        mat[0][1] = r12;
        mat[0][2] = r13;
        mat[1][0] = r21;
        mat[1][1] = r22;
        mat[1][2] = r23;
        mat[2][0] = r31;
        mat[2][1] = r32;
        mat[2][2] = r33;
    }

    public static void Mat2Euler(double[][] mat, double[] xyz)
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

        x = Math.atan2(r32, r33);
        y = Math.atan2(-r31, Math.sqrt(r32 * r32 + r33 * r33));
        z = Math.atan2(r21, r11);

        xyz[0] = x;
        xyz[1] = y;
        xyz[2] = z;

    }

}
