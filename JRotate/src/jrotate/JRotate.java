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

    //欧拉角 --> 旋转矩阵(矩阵x向量)
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
        return mat;
    }

    //旋转矩阵(矩阵x向量) --> 欧拉角
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


    //欧拉角 --> 四元数
    public static double[] Euler2Qua(double[] xyz)
    {
        double x, y, z;
        x = xyz[0];
        y = xyz[1];
        z = xyz[2];
        double qw, qx, qy, qz;
        qw = Math.cos(x / 2) * Math.cos(y / 2) * Math.cos(z / 2) + Math.sin(x / 2) * Math.sin(y / 2) * Math.sin(z / 2);
        qx = Math.sin(x / 2) * Math.cos(y / 2) * Math.cos(z / 2) - Math.cos(x / 2) * Math.sin(y / 2) * Math.sin(z / 2);
        qy = Math.cos(x / 2) * Math.sin(y / 2) * Math.cos(z / 2) + Math.sin(x / 2) * Math.cos(y / 2) * Math.sin(z / 2);
        qz = Math.cos(x / 2) * Math.cos(y / 2) * Math.sin(z / 2) - Math.sin(x / 2) * Math.sin(y / 2) * Math.cos(z / 2);
        return new double[]{qw, qx, qy, qz};
    }
    
    //四元数 --> 欧拉角
    public static double[] Qua2Euler(double[] wxyz)
    {
        double qw, qx, qy, qz;
        qw = wxyz[0];
        qx = wxyz[1];
        qy = wxyz[2];
        qz = wxyz[3];

        double x, y, z;
        x = Math.atan2(2 * (qw * qx + qy * qz), 1 - 2 * (qx * qx + qy * qy));
        y = Math.asin(2 * (qw * qy - qx * qz));
        z = Math.atan2(2 * (qw * qz + qx * qy), 1 - 2 * (qz * qz + qy * qy));

        return new double[]{x, y, z};
    }
    
    //四元数 --> 旋转矩阵(矩阵x向量)
    public static double[][] Qua2Mat(double[] wxyz)
    {
        double w, x, y, z;
        w = wxyz[0];
        x = wxyz[1];
        y = wxyz[2];
        z = wxyz[3];
        double[][] mat = {
            {w * w + x * x - y * y - z * z, 2 * (x * y - w * z), 2 * (w * y + x * z)},
            {2 * (x * y + w * z), w * w - x * x + y * y - z * z, 2 * (y * z - w * x)},
            {2 * (x * z - w * y), 2 * (y * z + w * x), w * w - x * x - y * y + z * z}};
        return mat;
    }
    
    //旋转矩阵(矩阵x向量) --> 四元数
    public static double[] Mat2Qua(double[][] mat)
    {
        double w,x, y, z;
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

        w = 0.5 * Math.sqrt(1 + r11 + r22 + r33);
        x = Math.signum(r32 - r23) * 0.5 * Math.sqrt(1 + r11 - r22 - r33);
        y = Math.signum(r13 - r31) * 0.5 * Math.sqrt(1 - r11 + r22 - r33);
        z = Math.signum(r21 - r12) * 0.5 * Math.sqrt(1 - r11 - r22 + r33);

        return new double[]{w, x, y, z};
    }

}
