package org.robotics.nao;

import java.util.ArrayList;

import org.robotics.nao.controller.AccelerometerController;
import org.robotics.nao.model.Accelerometer;
import org.robotics.nao.model.SpeechRecognition;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;
import android.widget.Toast;

public class Application extends Activity {
	private static final int VOICE_RECOGNITION = 1;
	private static final int ACCELEROMETER = 2;
	private boolean bSensorRunning=false;
	private SensorManager sensorMgr;
	private AccelerometerController accCtrl;
/**
 * Activity
 */
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main);
        sensorMgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        SpeechRecognition.parent=this;
        Accelerometer.parent=this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
		case VOICE_RECOGNITION:
			if (resultCode == Activity.RESULT_OK)
				extractSpeechRecognitionResults(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS));
			else
				Toast.makeText(getApplicationContext(),"Erreur", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
    }
/**
 * Option Menu
 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	SubMenu m;
    	m= menu.addSubMenu(0, VOICE_RECOGNITION, 0, "Speak");
    	m.setIcon(android.R.drawable.ic_btn_speak_now);
    	m = menu.addSubMenu(0, ACCELEROMETER, 0, "Walk");
    	m.setIcon(android.R.drawable.ic_menu_directions);
    	return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    		if(bSensorRunning)
        		menu.findItem(ACCELEROMETER).setTitle("Stop");
        	else
        		menu.findItem(ACCELEROMETER).setTitle("Walk");    	
    	return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case VOICE_RECOGNITION:
			startSpeechRecognitionActivity();
			break;
		case ACCELEROMETER:
			startAccelerometerActivity();
			break;
		default:
			break;
		}
       	return super.onContextItemSelected(item);
    }

/**
 * Speech Recognition
 */
    private void startSpeechRecognitionActivity(){
    	SpeechRecognition.onSpeechRecognitionClick();
    }
    private void extractSpeechRecognitionResults(ArrayList<String>results){
    	Toast.makeText(getApplicationContext(),results.get(0), Toast.LENGTH_LONG).show();
    }
    
/** 
 *	Accelerometer 
**/    
    private void startAccelerometerActivity() {
    	if(!bSensorRunning){
    		accCtrl=new AccelerometerController(this);
    		if (!runAccelerometer())
    			Toast.makeText(getApplicationContext(),"Accelerometer not supported!", Toast.LENGTH_LONG).show();
    	}
    	else
    		stopAccelerometer();
    }
    
    private  boolean runAccelerometer(){
		boolean accelSupported = sensorMgr.registerListener(accCtrl, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_UI);
		if(!accelSupported){
			sensorMgr.unregisterListener(accCtrl, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
			((TextView)this.findViewById(R.id.acc)).setText("Pas d'accelerometre");
			return false;
		}
		bSensorRunning=true;
		return true;
	}
    
    private void stopAccelerometer(){
    	sensorMgr.unregisterListener(accCtrl, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    	bSensorRunning=false;
    }
    
    public void setAccelerometerFields(float x,float y){
		((TextView)this.findViewById(R.id.axex)).setText("AxeX:"+x);
		((TextView)this.findViewById(R.id.axey)).setText("AxeY:"+y);
	}
 
}