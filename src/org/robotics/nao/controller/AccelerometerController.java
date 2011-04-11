package org.robotics.nao.controller;

import org.robotics.nao.model.AccelerometerModel;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AccelerometerController implements SensorEventListener {
	
	Activity parent;
	public AccelerometerController(Activity parent){
		this.parent=parent;
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()){
		case Sensor.TYPE_ACCELEROMETER:
			AccelerometerModel.onAccelerometerChanged(event);
			break;
		case Sensor.TYPE_ORIENTATION:
			AccelerometerModel.onOrientationChanged(event);
			break;
		}	
	}
	
	
}
