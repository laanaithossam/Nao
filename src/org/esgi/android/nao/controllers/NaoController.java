package org.esgi.android.nao.controllers;

import org.esgi.android.nao.interfaces.INaoConnectionEvent;
import org.esgi.android.nao.interfaces.INaoListener;

import com.naoqi.remotecomm.ALProxy;

import android.util.Log;

public class NaoController implements ALProxy.MethodResponseListener
{
	//-----------------------------------------------------------------------------------------------------------------
	// Private variables
	//-----------------------------------------------------------------------------------------------------------------
	private INaoConnectionEvent connection_event = null;
	private INaoListener nao_listener = null;
	private String robotname = null;
	private String password = null;
	private ConnectionManager connectionmanager = null;
	private boolean stiffness = false;
	private final String[] installed_behaviors = {"BallTracker", "PresentationNao", "DanceCaravanPalace", "DanceTaichii", 
			"DanceThriller", "DanceVangelis", "Starwars"};
	//-----------------------------------------------------------------------------------------------------------------
	// Constructor
	//-----------------------------------------------------------------------------------------------------------------
	public NaoController(INaoListener nao_listener,INaoConnectionEvent event) 
	{
		this.nao_listener = nao_listener;
		this.connection_event = event;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public methods
	//-----------------------------------------------------------------------------------------------------------------
	public void connect(String robotname, String password)
	{
		this.robotname = robotname;
		this.password = password;
		connectionmanager = ConnectionManager.getInstance(this.connection_event);
		connectionmanager.connect(this.robotname, this.password);
	}
	/*
	 * just works
	 */
	public void walkTo(float x,float y,float theta)
	{
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;
		if (!this.stiffness)
			setStiffnesses(true);

		Log.i("walkTo: "," X = " + x + " | Y = "+ y +" | O = " + theta);
		
		ALProxy motion_proxy = connectionmanager.getProxy("ALMotion");
		connectionmanager.connexion_postCall(motion_proxy, "setWalkTargetVelocity", x, y, theta, (float)1.0);
	}
	
	/**
	 * 
	 * don't use this method
	 */ 
	
	public void setStiffnesses(boolean enable) {
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;

		Log.i("Stiffnesses", "" + enable);
		
		ALProxy motion_proxy = connectionmanager.getProxy("ALMotion");
		connectionmanager.connexion_postCall(motion_proxy, "setStiffnesses", 1.0);
		this.stiffness = enable;
	}
	/**
	 * Works Well
	 * 
	 */
	
	public void say(String message)
	{
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;
		Log.i("Say: ", message);
		ALProxy tts_proxy = connectionmanager.getProxy("ALTextToSpeech");
		connectionmanager.connexion_postCall(tts_proxy, "say", message);
	}
	
	/**
	 * Works
	 */
	public void StandUp()
	{
		this.RunBehavior("StandUp");
	}
	
	/**
	 * Works
	 */
	public void StopWalk()
	{
		this.RunBehavior("StopWalk");
	}
	
	/**
	 * 
	 * @param behavior
	 */
	public void RunBehavior(String name)
	{
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;
		this.say("Starting Behavior : " + name);
		ALProxy fBehaviorManagerProxy = connectionmanager.getProxy("ALBehaviorManager");
		connectionmanager.connexion_postCall(fBehaviorManagerProxy, "runBehavior",
				name);
		
	}
	
	/**
	 * 
	 */
	public void StopBehavior(String name)
	{
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;
		this.say("Stopping Behavior : " + name);
		ALProxy fBehaviorManagerProxy = connectionmanager.getProxy("ALBehaviorManager");
		connectionmanager.connexion_postCall( fBehaviorManagerProxy, "stopBehavior", name);
		this.StopWalk();
	}
	
	/**
	 * 
	 */
	public String[] getInstalledBehaviors() {
		return this.installed_behaviors;
	}
	
	/**
	 * just doesn't work don't know why the callback is never called ..
	 */
	public void requestInstalledBehaviors()
	{
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;
		ALProxy fBehaviorManagerProxy = connectionmanager.getProxy("ALBehaviorManager");
		connectionmanager.connexion_asyncCall(300000, this, 
				fBehaviorManagerProxy, "getInstalledBehaviors" );
	}
	
	//FIXME: perhaps create a specific class
	// but for now there are only one method which call onResponse so ..
	// doesn't work just use the hardcoded behaviors list ..
	@Override
	public void onResponse(Object result) {
		String[] behaviors = (String[]) result;
		if (result == null)
			return;
		this.nao_listener.ongetInstalledBehaviors(behaviors);
	}
}
