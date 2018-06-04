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
import javax.swing.JDialog;
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

        calculateData();
        getDisplayControl();

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

    String satelliteStartTimeStr = "2014-08-01 00:00:00.000";
    String satelliteEndTimeStr = "2014-08-01 01:00:00.000";
    String tle = "1 26619U 00075A   16293.73279326 -.00000071  00000-0 -49450-5 0  9993;2 26619  97.8636 328.0037 0010174 143.8408 216.3455 14.64311646848348";

    String shotStartTimeStr = "2014-08-01 00:15:00.000";
    String shotEndTimeStr = "2014-08-01 00:16:00.000";

    double maxSwingAngle = 20;
    double swingAngle = 0;
    double fov = 20;

    Position[] satellitePosArray;
    ArrayList<Position> leftPosList;
    ArrayList<Position> rightPosList;
    ArrayList<Position> leftMaxPosList;
    ArrayList<Position> rightMaxPosList;

    private void calculateData()
    {
        HashMap<String, String> tleMap = ProcessResource.ReadTle("src\\resource\\tle.txt");
        String satelliteXml = "zy02c.xml";
        SatelliteInput sli = ProcessResource.ReadSatellite("src\\resource\\satellite\\" + satelliteXml, tleMap);
        sli.satElement = tle;

        PositionVelocityOutput[] pvo = OneDayCoverage.calPosition(sli, new Time(satelliteStartTimeStr), new Time(satelliteEndTimeStr), 1);

        satellitePosArray = new Position[pvo.length];
        leftPosList = new ArrayList<Position>();
        rightPosList = new ArrayList<Position>();
        leftMaxPosList = new ArrayList<Position>();
        rightMaxPosList = new ArrayList<Position>();

        for (int i = 0; i < pvo.length; i++)
        {
            satellitePosArray[i] = Position.fromDegrees(pvo[i].Lat, pvo[i].Lon, pvo[i].Alt);
        }

        Time shotStartTime = new Time(shotStartTimeStr);
        Time shotEndTime = new Time(shotEndTimeStr);

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

    }

    private void getDisplayControl()
    {
        SatelliteElem satelliteElem;
        ShotElem shotElem;

        satelliteElem = new SatelliteElem();

        satelliteElem.startTime = new Time(satelliteStartTimeStr);
        satelliteElem.endTime = new Time(satelliteEndTimeStr);
        //passDisplay.ground = null;
        satelliteElem.satelliteName = "Satellite 01";
        satelliteElem.satellitePosArray = satellitePosArray;

        shotElem = new ShotElem();
        shotElem.startTime = new Time(shotStartTimeStr);
        shotElem.endTime = new Time(shotEndTimeStr);
        shotElem.leftMaxPosArray = (Position[]) leftMaxPosList.toArray(new Position[leftMaxPosList.size()]);
        shotElem.leftPosArray = (Position[]) leftPosList.toArray(new Position[leftPosList.size()]);
        shotElem.rightMaxPosArray = (Position[]) rightMaxPosList.toArray(new Position[rightMaxPosList.size()]);
        shotElem.rightPosArray = (Position[]) rightPosList.toArray(new Position[rightPosList.size()]);

        satelliteElem.shotElemList = new ArrayList<ShotElem>();
        satelliteElem.shotElemList.add(shotElem);

        displayControl = new DisplayController(displayLayer);
        displayControl.isShowSatelliteOrbit = true;
        displayControl.isShowSensor = true;
        displayControl.isShowCourageRange = true;
        displayControl.isShowSatellite = true;

        displayControl.satelliteElemArray = new SatelliteElem[1];
        displayControl.satelliteElemArray[0] = satelliteElem;
        displayControl.startTime = new Time(satelliteStartTimeStr);
        displayControl.endTime = new Time(satelliteEndTimeStr);

    }
}
