package org.robotics.nao.view;

import org.robotics.nao.R;
import org.robotics.nao.controller.AccelerometerController;
import org.robotics.nao.model.AccelerometerModel;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class Accelerometer extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accelerometer);
		AccelerometerModel.parent=this;
		SensorManager sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

		boolean accelSupported = sensorMgr.registerListener(new AccelerometerController(this), sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
		if(!accelSupported){
			sensorMgr.unregisterListener(new AccelerometerController(this), sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
			((TextView)findViewById(R.id.acc)).setText("Pas d'accelerometre");
		}

		boolean OrientSupported = sensorMgr.registerListener(new AccelerometerController(this),sensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_UI);
		if (!OrientSupported) {
			sensorMgr.unregisterListener(new AccelerometerController(this),sensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION));
			((TextView)this.findViewById(R.id.ori)).setText("Pas d'orientation");
		}

	}
	public void setAccelerometerFields(float x,float y,float z){
		((TextView)this.findViewById(R.id.axex)).setText("AxeX:"+x);
		((TextView)this.findViewById(R.id.axey)).setText("AxeY:"+y);
		((TextView)this.findViewById(R.id.axez)).setText("AxeZ:"+z);
	}
	public void setOrientationFields(float azimuth,float pitch,float roll){
		((TextView)this.findViewById(R.id.azimuth)).setText("Azimuth:"+azimuth);
		((TextView)this.findViewById(R.id.pitch)).setText("Pitch:"+pitch);
		((TextView)this.findViewById(R.id.roll)).setText("Roll:"+roll);
	}
}
