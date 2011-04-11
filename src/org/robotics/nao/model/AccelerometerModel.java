package org.robotics.nao.model;

import org.robotics.nao.R;
import org.robotics.nao.view.Accelerometer;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.widget.TextView;

public class AccelerometerModel {
	 
	public static Accelerometer parent;
	
	public static  void onAccelerometerChanged(SensorEvent event){
		float x,y,z;
		x = event.values[0];
		y = event.values[1];
		z = event.values[2];
		parent.setAccelerometerFields(x,y,z);
	}
	
	public static  void onOrientationChanged(SensorEvent event){
		float azimuth,pitch,roll;
		azimuth = event.values[0];
		pitch   = event.values[1];
		roll    = event.values[2];
		parent.setOrientationFields(azimuth,pitch,roll);
	}


}
