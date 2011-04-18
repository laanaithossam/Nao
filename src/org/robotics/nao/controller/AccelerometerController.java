package org.robotics.nao.controller;

import org.robotics.nao.model.Accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AccelerometerController implements SensorEventListener {
	Accelerometer parent;
	public AccelerometerController(Accelerometer parent){
		this.parent=parent;
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()){
		case Sensor.TYPE_ACCELEROMETER:
			parent.onAccelerometerChanged(event);
			break;
		}	
	}

}
