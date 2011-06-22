package org.esgi.android.nao.controllers;

import org.esgi.android.nao.interfaces.INaoEvent;

import android.util.Log;

public class NaoController 
{
	//-----------------------------------------------------------------------------------------------------------------
	// Private variables
	//-----------------------------------------------------------------------------------------------------------------
	private INaoEvent m_event = null;
	
	//-----------------------------------------------------------------------------------------------------------------
	// Constructor
	//-----------------------------------------------------------------------------------------------------------------
	public NaoController(INaoEvent event) 
	{
		this.m_event = event;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public methods
	//-----------------------------------------------------------------------------------------------------------------
	public void connect()
	{
		this.m_event.onConnected();
	}
	
	public void walk()
	{
		
	}
	
	public void speak(String message)
	{
		Log.i("AndroTest", "Nao must say : " + message );
	}
}
