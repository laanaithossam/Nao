/**
 * 
 */
package org.robotics.nao.view;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

/**
 * @author Hossam
 * Class for voice recognition
 */
public class SpeechRecognition extends Activity {
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		startSpeechRecognitionActivity();
	}
	private void startSpeechRecognitionActivity(){

		Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRANCE);
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==VOICE_RECOGNITION_REQUEST_CODE  && resultCode==RESULT_OK){
			setResult(resultCode, data);
			this.finish();
		}
	}
	
	
}
