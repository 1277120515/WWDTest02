/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import coverage.OneDayCoverage;
import coverage.iostruct.PositionVelocityOutput;
import coverage.iostruct.ProcessResource;
import coverage.iostruct.SatelliteInput;
import coverage.util.CoorTrans;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwindx.applications.worldwindow.core.Controller;
import java.awt.*;
import javax.swing.*;
import display.iostruct.*;
import coverage.util.Time;
import gov.nasa.worldwind.geom.Position;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.JButton;

/**
 *
 * @author ZZL
 */
public class MyTestDialog extends JDialog
{
    Controller controller;
    WorldWindow wwd;
    RenderableLayer displayLayer;

    DisplayController displayControl;

    public MyTestDialog(Controller c, Frame owner)
    {
        super(owner);
        this.controller = c;
        this.wwd = c.getWWd();

        displayLayer = (RenderableLayer) wwd.getModel().getLayers().getLayerByName("三维显示");

       fun();
       
        displayControl.Restart();

        //displayControl.currentTime = new Time("2014-08-01 00:15:10.000");
        //displayControl.display();
        Box box=Box.createVerticalBox();
        JButton btn = new JButton("+1s");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayControl.NextFrame();
                wwd.redraw();
            }
        });
        box.add(btn);

        btn = new JButton("+10s");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayControl.ChangeFrame(10);
                wwd.redraw();
            }
        });
        box.add(btn);
        
        btn = new JButton("-1s");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayControl.LastFrame();
                wwd.redraw();
            }
        });
        box.add(btn);
        
        btn = new JButton("-10s");
        btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayControl.ChangeFrame(-10);
                wwd.redraw();
            }
        });
        box.add(btn);
        
        this.add(box);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(300, 400));
        this.pack();
    }
    
    private ShotElem GetShotElem(PositionVelocityOutput[] pvo,Time shotStartTime,Time shotEndTime,double swingAngle , double fov,double maxSwingAngle)
    {
        ArrayList<Position> leftPosList = new ArrayList<Position>();
        ArrayList<Position> rightPosList = new ArrayList<Position>();
        ArrayList<Position> leftMaxPosList = new ArrayList<Position>();
       ArrayList<Position>  rightMaxPosList = new ArrayList<Position>();


        for (PositionVelocityOutput pvo1 : pvo)
        {
            if (pvo1.Time.afterOrEqual(shotStartTime) && pvo1.Time.beforeOrEqual(shotEndTime))
            {
                double[] r1;
                double[] v1;
                double[] LonLatLeft;
                double[] LonLatRight;

                r1 = new double[]
                {
                    pvo1.x_J2000C, pvo1.y_J2000C, pvo1.z_J2000C
                };
                v1 = new double[]
                {
                    pvo1.vx_J2000C, pvo1.vy_J2000C, pvo1.vz_J2000C
                };
                LonLatLeft = CoorTrans.getScanLatLon(r1, v1, swingAngle + fov / 2, 0);     //左侧 扫描边界与地球交点经纬度
                LonLatRight = CoorTrans.getScanLatLon(r1, v1, swingAngle - fov / 2, 0);    //右侧 扫描边界与地球交点经纬度

                leftPosList.add(Position.fromDegrees(LonLatLeft[1], LonLatLeft[0], 0));
                rightPosList.add(Position.fromDegrees(LonLatRight[1], LonLatRight[0], 0));

                LonLatLeft = CoorTrans.getScanLatLon(r1, v1, maxSwingAngle + fov / 2, 0);     //左侧 最大扫描边界与地球交点经纬度
                LonLatRight = CoorTrans.getScanLatLon(r1, v1, -maxSwingAngle - fov / 2, 0);   //右侧 最大扫描边界与地球交点经纬度

                leftMaxPosList.add(Position.fromDegrees(LonLatLeft[1], LonLatLeft[0], 0));
                rightMaxPosList.add(Position.fromDegrees(LonLatRight[1], LonLatRight[0], 0));
            }
        }
        
        ShotElem shotElem = new ShotElem();
        shotElem.startTime = shotStartTime.clone();
        shotElem.endTime =shotEndTime.clone();
        shotElem.leftMaxPosArray = (Position[]) leftMaxPosList.toArray(new Position[leftMaxPosList.size()]);
        shotElem.leftPosArray = (Position[]) leftPosList.toArray(new Position[leftPosList.size()]);
        shotElem.rightMaxPosArray = (Position[]) rightMaxPosList.toArray(new Position[rightMaxPosList.size()]);
        shotElem.rightPosArray = (Position[]) rightPosList.toArray(new Position[rightPosList.size()]);
        
        return shotElem;
    }
    
    private void fun()
    {
        Time simuStartTime = new Time("2014-08-01 00:00:00.000");
        Time simuEndTime = new Time("2014-08-01 00:10:00.000");

        displayControl = new DisplayController(displayLayer);

        displayControl.isShowSatellite = true;
        displayControl.isShowSatelliteOrbit = true;
        displayControl.isShowSensor = true;
        displayControl.isShowCourageRange = true;
        displayControl.isShowMaxCourageRange = true;
        displayControl.isShowGroundRegion = true;



        displayControl.satelliteElemList = new ArrayList<SatelliteElem>();
        //displayControl.satelliteElemArray[0] = satelliteElem;
        displayControl.startTime = simuStartTime.clone();
        displayControl.endTime = simuEndTime.clone();
        displayControl.ground=null;

        SatelliteElem satelliteElem;
        ShotElem shotElem;

        HashMap<String, String> tleMap = ProcessResource.ReadTle("src\\resource\\tle.txt");
        String satelliteXml;
        SatelliteInput sli;
        PositionVelocityOutput[] pvo;

        //第一颗卫星
        satelliteElem = new SatelliteElem();
        satelliteElem.satelliteName = "SAT 01";
        satelliteElem.startTime = displayControl.startTime.clone();
        satelliteElem.endTime = displayControl.endTime.clone();

        satelliteXml = "zy02c.xml";
        sli = ProcessResource.ReadSatellite("src\\resource\\satellite\\" + satelliteXml, tleMap);
        Time endTimePlus1s=displayControl.endTime.clone();
        endTimePlus1s.addSeconds(1);
        pvo = OneDayCoverage.calPosition(sli, displayControl.startTime.clone(), endTimePlus1s, 1);

        satelliteElem.satellitePosArray = new Position[pvo.length];
        for (int i = 0; i < pvo.length; i++)
        {
            satelliteElem.satellitePosArray[i] = Position.fromDegrees(pvo[i].Lat, pvo[i].Lon, pvo[i].Alt);
        }

        satelliteElem.shotElemList=new ArrayList<ShotElem>();
        
        shotElem = GetShotElem(pvo, new Time("2014-08-01 00:02:00.000"), new Time("2014-08-01 00:03:00.000"), -10, 8, 25);
        satelliteElem.shotElemList.add(shotElem);
        shotElem = GetShotElem(pvo, new Time("2014-08-01 00:02:00.000"), new Time("2014-08-01 00:03:00.000"), 10, 8,25);
        satelliteElem.shotElemList.add(shotElem);
        
        displayControl.satelliteElemList.add(satelliteElem);
        
        //第二颗卫星
        satelliteElem = new SatelliteElem();
        satelliteElem.satelliteName = "SAT 01";
        satelliteElem.startTime = displayControl.startTime.clone();
        satelliteElem.endTime = displayControl.endTime.clone();

        satelliteXml = "gf1.xml";
        sli = ProcessResource.ReadSatellite("src\\resource\\satellite\\" + satelliteXml, tleMap);
        endTimePlus1s=displayControl.endTime.clone();
        endTimePlus1s.addSeconds(1);
        pvo = OneDayCoverage.calPosition(sli, displayControl.startTime.clone(), endTimePlus1s, 1);

        satelliteElem.satellitePosArray = new Position[pvo.length];
        for (int i = 0; i < pvo.length; i++)
        {
            satelliteElem.satellitePosArray[i] = Position.fromDegrees(pvo[i].Lat, pvo[i].Lon, pvo[i].Alt);
        }

        satelliteElem.shotElemList=new ArrayList<ShotElem>();
        
        shotElem = GetShotElem(pvo, new Time("2014-08-01 00:02:00.000"), new Time("2014-08-01 00:03:00.000"), -10, 8, 25);
        satelliteElem.shotElemList.add(shotElem);
        shotElem = GetShotElem(pvo, new Time("2014-08-01 00:02:00.000"), new Time("2014-08-01 00:03:00.000"), 10, 8,25);
        satelliteElem.shotElemList.add(shotElem);
        
        displayControl.satelliteElemList.add(satelliteElem);
        
    }
 
}
