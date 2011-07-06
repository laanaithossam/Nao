package org.esgi.android.nao.controllers;

import org.esgi.android.nao.interfaces.INaoConnectionEvent;

import com.naoqi.remotecomm.ALProxy;

import android.util.Log;

public class NaoController 
{
	//-----------------------------------------------------------------------------------------------------------------
	// Private variables
	//-----------------------------------------------------------------------------------------------------------------
	private INaoConnectionEvent m_event = null;
	private String robotname = null;
	private String password = null;
	private ConnectionManager connectionmanager = null;
	//-----------------------------------------------------------------------------------------------------------------
	// Constructor
	//-----------------------------------------------------------------------------------------------------------------
	public NaoController(INaoConnectionEvent event) 
	{
		this.m_event = event;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public methods
	//-----------------------------------------------------------------------------------------------------------------
	public void connect(String robotname, String password)
	{
		this.robotname = robotname;
		this.password = password;
		connectionmanager = ConnectionManager.getInstance(this.robotname, this.password, this.m_event);
	}
	
	public void walkTo(float x,float y,float theta)
	{
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;

		Log.i("walkTo: "," X = " + x + " | Y = "+ y +" | O = " + theta);
		
		ALProxy motion_proxy = connectionmanager.getProxy("ALMotion");
		connectionmanager.connexion_postCall(motion_proxy, "walkTo", x,y,theta);
	}
	
	public void say(String message)
	{
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;

		Log.i("Say: ", message);
		ALProxy tts_proxy = connectionmanager.getProxy("ALTextToSpeech");
		connectionmanager.connexion_postCall(tts_proxy, "say", message);
	}
}
