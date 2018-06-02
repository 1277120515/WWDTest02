/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package display;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwindx.applications.worldwindow.core.Controller;
import java.awt.Frame;
import javax.swing.JDialog;
import display.iostruct.*;
import coverage.util.Time;
import gov.nasa.worldwind.geom.Position;



/**
 *
 * @author ZZL
 */
public class MyTestDialog extends JDialog
{
    Controller controller;
    WorldWindow wwd;
    RenderableLayer displayLayer;

    public MyTestDialog(Controller c, Frame owner)
    {
        super(owner);
        this.controller = c;
        this.wwd = c.getWWd();

        displayLayer = (RenderableLayer) wwd.getModel().getLayers().getLayerByName("»˝Œ¨œ‘ æ");
        
        
        
        
        calculateData();
        


    }
    
    
    private void calculateData()
    {
        PassDisplay passDisplay;
        SensorDisplay sensorDisplay;
        
        DisplayControl displayControl=new DisplayControl(displayLayer);
        displayControl.isShowSatelliteOrbit=true;
        displayControl.passDisplayArray=new PassDisplay[2];
        
        passDisplay=new PassDisplay();
        
        passDisplay.startTime=new Time("2018-06-02 10:00:00");
        passDisplay.endTime=new Time("2018-06-02 10:01:00");
        passDisplay.ground=null;
        passDisplay.satelliteName="Satellite 01";
        passDisplay.satellitePosArray=new Position[10];
        passDisplay.sensorArray=new SensorDisplay[2];
    }
}
