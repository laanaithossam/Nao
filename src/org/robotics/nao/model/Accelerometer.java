package org.robotics.nao.model;

import org.robotics.nao.Application;

import android.hardware.SensorEvent;

public class Accelerometer {
	 
	public static Application parent;
	
	public static void onAccelerometerChanged(SensorEvent event){
		float x,y;
		x = event.values[0];
		y = event.values[1];
		parent.setAccelerometerFields(x,y);
	}
	
}
