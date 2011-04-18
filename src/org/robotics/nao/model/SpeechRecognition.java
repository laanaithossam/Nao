package org.robotics.nao.model;

import java.util.ArrayList;
import java.util.Locale;

import org.robotics.nao.Application;
import org.robotics.nao.bo.Nao;

import android.content.Intent;
import android.speech.RecognizerIntent;

public class SpeechRecognition {
	private  Application parent;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;
	private Intent intent;

	public SpeechRecognition(Application application){
		this.parent=application;

		intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRANCE);
	}

	public void onSpeechRecognitionClick(){
		parent.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}
	
	public void extractSpeechRecognitionResults(ArrayList<String>results){
		Nao.getInstance().say(results.get(0));
	}
}
