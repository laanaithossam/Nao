package org.robotics.nao;

import java.util.ArrayList;

import org.robotics.nao.view.Accelerometer;
import org.robotics.nao.view.SpeechRecognition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

public class Application extends Activity {
	private static final int VOICE_RECOGNITION = 1;
	private static final int ACCELEROMETER = 2;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	SubMenu m;
    	m= menu.addSubMenu(0, VOICE_RECOGNITION, 0, "Speak");
    	m.setIcon(android.R.drawable.ic_btn_speak_now);
    	m = menu.addSubMenu(0, ACCELEROMETER, 0, "Sensor");
    	m.setIcon(android.R.drawable.ic_menu_directions);
    	return super.onCreateOptionsMenu(menu);
    }
    @Override
    //Onclick MenuItem
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
    private void startSpeechRecognitionActivity(){
    	Intent intent=new Intent(Application.this,SpeechRecognition.class);
        startActivityForResult(intent, VOICE_RECOGNITION);
    }
    private void extractSpeechRecognitionResults(ArrayList<String>results){
    	Toast.makeText(getApplicationContext(),results.get(0), Toast.LENGTH_LONG).show();
    }
    private void startAccelerometerActivity() {
    	Intent intent=new Intent(Application.this,Accelerometer.class);
        startActivityForResult(intent, ACCELEROMETER);
	}
}