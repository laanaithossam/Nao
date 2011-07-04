package org.robotics.nao.bo;



public class Nao {
	private static Nao instance = null;

	ConnectionManager connectionmanager = null;

	private Nao(String robotname, String password){
		connectionmanager = ConnectionManager.getInstance(robotname, password);
	}

	public final synchronized static Nao getInstance(String robotname, String password) {
        if (instance == null)
            instance = new Nao(robotname, password);
        return instance;
    }

	public void walkTo(float x,float y,float theta){
		System.out.println("walkTo : X = " + x + " | Y = "+ y +" | O = " + theta);
	}

	public void say(String message){
		System.out.println("say = " + message);
	}

}
