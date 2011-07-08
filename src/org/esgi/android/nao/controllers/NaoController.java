package org.esgi.android.nao.controllers;

import org.esgi.android.nao.interfaces.INaoConnectionEvent;
import org.esgi.android.nao.interfaces.INaoListener;
import org.jivesoftware.smack.util.Base64;

import com.naoqi.remotecomm.ALBroker.MethodCallListener;
import com.naoqi.remotecomm.ALMethodCall;
import com.naoqi.remotecomm.ALProxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class NaoController
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
	
	public void setOutputVolume(int vol) {
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;
		ALProxy audiodev_proxy = connectionmanager.getProxy("ALAudioDevice");
		connectionmanager.connexion_postCall(audiodev_proxy, "setOutputVolume", vol);
	}
	
	public void requestOutputVolume(){
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;
		ALProxy audiodev_proxy = connectionmanager.getProxy("ALAudioDevice");
		connectionmanager.connexion_asyncCall(3000, new ALProxy.MethodResponseListener(){
				public void onResponse(Object result) {
					int vol = (Integer) result;
					Log.i( "NAO",  "volume :" + vol);
						nao_listener.ongetVolume(vol);
					}},audiodev_proxy, "getOutputVolume");
	}
	
	/**
	 * 
	 * don't use this method
	 */ 
	
	private void setStiffnesses(boolean enable) {
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
	
	public void requestPicture() {
		MethodCallListener picturelistener  = new MethodCallListener(){

			@Override
			public void onCall(ALMethodCall call) {			
				String message = (String) call.params[0];
				
				byte[] val = Base64.decode(message.getBytes(), 0, 0, 0);//Base64.DEFAULT);
			    Bitmap bmp = BitmapFactory.decodeByteArray(val, 0, val.length);
			    nao_listener.onpictureAvailable(bmp);
			}	
		};
		this.connectionmanager.registerListener("ImageViewer.fromData", picturelistener);
	}
	
	/**
	 * 
	 */
	public String[] getHardcodedBehaviors() {
		return this.installed_behaviors;
	}
	
	/**
	 * just works
	 */
	public void requestInstalledBehaviors()
	{
		if (connectionmanager.state != ConnectionManager.connexion_state.STATE_PRESENCE)
			return;
		ALProxy fBehaviorManagerProxy = connectionmanager.getProxy("ALBehaviorManager");
		connectionmanager.connexion_asyncCall(3000, new ALProxy.MethodResponseListener(){
			public void onResponse(Object result) {
				Object[] results = (Object[]) result;
				
				String[] behaviors = new String[results.length];

				for (int i=0; i<results.length; i++) {
					behaviors[i] = (String) results[i];
				}
				Log.i( "NAO",  "behaviors " + behaviors);
					nao_listener.ongetInstalledBehaviors(behaviors);
				}}, 
				fBehaviorManagerProxy, "getInstalledBehaviors" );
	}

}
