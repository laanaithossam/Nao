package org.esgi.android.nao.controllers;


import com.naoqi.remotecomm.ALProxy;


public class Nao {
	private static Nao instance = null;
	private String robotname = null;
	private String password = null;
	ConnectionManager connectionmanager = null;

	private Nao(){}

	public final synchronized static Nao getInstance() {
        if (instance == null)
            instance = new Nao();
        return instance;
    }

	public void getInstance(String robotname, String password) {
		if (instance == null)
            instance = new Nao();
		this.robotname = robotname;
		this.password = password;
		connectionmanager = ConnectionManager.getInstance(this.robotname, this.password);
	}

	public void walkTo(float x,float y,float theta){
		ALProxy motion_proxy = connectionmanager.getProxy("ALMotion");

		connectionmanager.connexion_postCall(motion_proxy, "walkTo", x,y,theta);

		System.out.println("walkTo : X = " + x + " | Y = "+ y +" | O = " + theta);
	}

	public void say(String message){
		ALProxy tts_proxy = connectionmanager.getProxy("ALTextToSpeech");
		connectionmanager.connexion_postCall(tts_proxy, "say", message);
		System.out.println("say = " + message);
	}

}