/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display.util;

import coverage.util.Time;
import gov.nasa.worldwind.geom.Position;

//����������ÿһ�����㣬���������Ὺ�ػ�һ�Σ����ػ�֮��������㽫�γ�һ��������

/**
 *
 * @author ZZL
 */

public class ShotUnit
{

    /**
     * ����������ʱ��
     */
    public Time startTime;            

    /**
     * �������ػ�ʱ��
     */
    public Time endTime;   

    /**
     *����������㷶Χ����߽羭γ��
     * ���� = startTime��endTime�����������
     */
    public Position[] leftMaxPosArray; 

    /**
     *����������㷶Χ���ұ߽羭γ��
     * ���� = startTime��endTime�����������
     */
    public Position[] rightMaxPosArray; 

    /**
     *���ǰ����ض���ڽǽ������㣬������߽羭γ�ȡ�
     * ���� = startTime��endTime���������
     */
    public Position[] leftPosArray;    

    /**
     *���ǰ����ض���ڽǽ������㣬�����ұ߽羭γ�ȡ�
     * ���� = startTime��endTime���������
     */
    public Position[] rightPosArray;   
}
//˵����leftMaxPosArray  rightMaxPosArray  leftPosArray rightPosArray�⼸������ĳ��� = startTime��endTime���������

