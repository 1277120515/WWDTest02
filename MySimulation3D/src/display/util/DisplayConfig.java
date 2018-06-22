/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display.util;

import java.awt.Color;

/**
 *
 * @author ZZL
 */
public class DisplayConfig
{
    public static Color orbitColor = Color.red;                 //����켣��ɫ
    public static double orbitWidth = 2;                        //����켣���

    public static Color sensorInnerColor = Color.green;         //�����������ڲ���ɫ
    public static double sensorInnerOpacity = 0.4;              //�����������ڲ�͸����
    public static Color sensorOutlineColor = Color.green;       //����������������ɫ
    public static double sensorOutlineOpacity = 1;              //��������������͸����
    public static Color scanLineColor = Color.RED;              //ɨ������ɫ
    public static double scanLineWidth = 4;                     //ɨ���߿��
    public static double scanLineOpacity = 1;                   //ɨ����͸����

    public static Color sensorMaxInnerColor = new Color(255, 97, 0);    //��������󸲸Ƿ�Χ�����ڲ���ɫ
    public static double sensorMaxInnerOpacity = 0.2;                   //��������󸲸Ƿ�Χ�����ڲ�͸����
    public static Color sensorMaxOutlineColor =  new Color(255, 97, 0); //��������󸲸Ƿ�Χ����������ɫ
    public static double sensorMaxOutlineOpacity = 0.5;                 //��������󸲸Ƿ�Χ��������͸����
    public static Color scanMaxLineColor =new Color(255, 97, 0);        //���Χɨ������ɫ
    public static double scanMaxLineWidth = 2;                          //���Χɨ���߿��
    public static double scanMaxLineOpacity = 0.5;                      //���Χɨ����͸����

    public static Color stripeInnerColor = Color.blue;      //�����ڲ���ɫ
    public static double stripeInnerOpacity = 0.4;          //�����ڲ�͸����
    public static Color stripeOutlineColor = Color.orange;  //����������ɫ
    public static double stripeOutlineOpacity = 1;          //��������͸����
    public static double stripeOutlineWidth = 2;            //�����������

}
