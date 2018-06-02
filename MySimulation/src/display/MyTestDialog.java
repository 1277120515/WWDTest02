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
import java.awt.Frame;
import javax.swing.JDialog;
import display.iostruct.*;
import coverage.util.Time;
import gov.nasa.worldwind.geom.Position;
import java.util.HashMap;



/**
 *
 * @author ZZL
 */
public class MyTestDialog extends JDialog
{
    Controller controller;
    WorldWindow wwd;
    RenderableLayer displayLayer;

    DisplayControl displayControl;
    public MyTestDialog(Controller c, Frame owner)
    {
        super(owner);
        this.controller = c;
        this.wwd = c.getWWd();

        displayLayer = (RenderableLayer) wwd.getModel().getLayers().getLayerByName("三维显示");



        calculateData();
        
        this.getDisplayControl();
        
        displayControl.currentTime=new Time("2014-08-01 00:01:00.000");
        
displayControl.display();


    }



    String startTimeStr = "2014-08-01 00:00:00.000";
    String endTimeStr = "2014-08-01 00:05:00.000";
    String tle = "1 26619U 00075A   16293.73279326 -.00000071  00000-0 -49450-5 0  9993;2 26619  97.8636 328.0037 0010174 143.8408 216.3455 14.64311646848348";
    double maxSwingAngle = 20;
    double swingAngle = 0;
    double fov = 5;

    Position[] satellitePosArray;
    Position[] leftPosArray;
    Position[] rightPosArray;
    Position[] leftMaxPosArray;
    Position[] rightMaxPosArray;

    private void calculateData()
    {
        HashMap<String, String> tleMap = ProcessResource.ReadTle("src\\resource\\tle.txt");
        String satelliteXml = "zy02c.xml";
        SatelliteInput sli = ProcessResource.ReadSatellite("src\\resource\\satellite\\" + satelliteXml, tleMap);
        sli.satElement = tle;

        PositionVelocityOutput[] pvo = OneDayCoverage.calPosition(sli, new Time(startTimeStr), new Time(endTimeStr), 1);

        satellitePosArray = new Position[pvo.length];
        leftPosArray = new Position[pvo.length];
        rightPosArray = new Position[pvo.length];
        leftMaxPosArray = new Position[pvo.length];
        rightMaxPosArray = new Position[pvo.length];

        for (int i = 0; i < pvo.length; i++)
        {
            double[] r1;
            double[] v1;
            double[] LonLatLeft;
            double[] LonLatRight;
            

            r1 = new double[]
            {
                pvo[i].x_J2000C, pvo[i].y_J2000C, pvo[i].z_J2000C
            };
            v1 = new double[]
            {
                pvo[i].vx_J2000C, pvo[i].vy_J2000C, pvo[i].vz_J2000C
            };
            LonLatLeft = CoorTrans.getScanLatLon(r1, v1, swingAngle + fov / 2, 0);     //左侧 扫描边界与地球交点经纬度
            LonLatRight = CoorTrans.getScanLatLon(r1, v1, swingAngle - fov / 2, 0);    //右侧 扫描边界与地球交点经纬度

            satellitePosArray[i] = Position.fromDegrees(pvo[i].Lat, pvo[i].Lon, pvo[i].Alt);
            leftPosArray[i] = Position.fromDegrees(LonLatLeft[1], LonLatLeft[0], 0);
            rightPosArray[i] = Position.fromDegrees(LonLatRight[1], LonLatRight[0], 0);

            LonLatLeft = CoorTrans.getScanLatLon(r1, v1, maxSwingAngle + fov / 2, 0);     //左侧 最大扫描边界与地球交点经纬度
            LonLatRight = CoorTrans.getScanLatLon(r1, v1, -maxSwingAngle - fov / 2, 0);   //右侧 最大扫描边界与地球交点经纬度

            leftMaxPosArray[i] = Position.fromDegrees(LonLatLeft[1], LonLatLeft[0], 0);
            rightMaxPosArray[i] = Position.fromDegrees(LonLatRight[1], LonLatRight[0], 0);


        }



    }

    private void getDisplayControl()
    {
        PassDisplay passDisplay;
        SensorDisplay sensorDisplay;

        passDisplay = new PassDisplay();

        passDisplay.startTime = new Time(startTimeStr);
        passDisplay.endTime = new Time(endTimeStr);
        passDisplay.ground = null;
        passDisplay.satelliteName = "Satellite 01";
        passDisplay.satellitePosArray = satellitePosArray;

        sensorDisplay = new SensorDisplay();
        sensorDisplay.leftMaxPosArray = leftMaxPosArray;
        sensorDisplay.leftPosArray = leftPosArray;
        sensorDisplay.rightMaxPosArray = rightMaxPosArray;
        sensorDisplay.rightPosArray = rightPosArray;

        passDisplay.sensorArray = new SensorDisplay[1];
        passDisplay.sensorArray[0] = sensorDisplay;
        
        
        displayControl = new DisplayControl(displayLayer);
        displayControl.isShowSatelliteOrbit = true;
        displayControl.passDisplayArray = new PassDisplay[1];
        displayControl.passDisplayArray[0]=passDisplay;
        
    }
}
