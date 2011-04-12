package org.robotics.nao.model;

import java.util.Locale;

import org.robotics.nao.Application;

import android.content.Intent;
import android.speech.RecognizerIntent;


public class SpeechRecognition {
	
	public static Application parent;
	public static final int VOICE_RECOGNITION_REQUEST_CODE = 1;
	
	public static void onSpeechRecognitionClick(){
		Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRANCE);
		parent.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}
}
