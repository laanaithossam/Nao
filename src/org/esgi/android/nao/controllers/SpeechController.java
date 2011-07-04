package org.esgi.android.nao.controllers;

import java.util.ArrayList;

import org.esgi.android.nao.interfaces.ISpeechEvent;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;

public class SpeechController 
{
	//-----------------------------------------------------------------------------------------------------------------
	// Private static variables
	//-----------------------------------------------------------------------------------------------------------------
	private final static int VOICE_RECOGNITION_REQUEST_CODE = 0xF00FAB;
	
	//-----------------------------------------------------------------------------------------------------------------
	// Private variables
	//-----------------------------------------------------------------------------------------------------------------
	private ISpeechEvent m_event = null;
	private Activity m_activity  = null;
	
	//-----------------------------------------------------------------------------------------------------------------
	// Constructor
	//-----------------------------------------------------------------------------------------------------------------
	public SpeechController(Activity activity, ISpeechEvent event) 
	{
		this.m_activity = activity;
		this.m_event    = event;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public methods
	//-----------------------------------------------------------------------------------------------------------------
	public void startSpeech()
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
		this.m_activity.startActivityForResult(intent, SpeechController.VOICE_RECOGNITION_REQUEST_CODE);
	}
	
	public void setActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode != SpeechController.VOICE_RECOGNITION_REQUEST_CODE)
			return;
		
		if (resultCode == Activity.RESULT_OK)
		{
			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			this.m_event.onSpeechResult(matches.get(0));
		}
		
		else if (resultCode == Activity.RESULT_CANCELED)
		{
			this.m_event.onSpeechCancel();
		}
	}
}
