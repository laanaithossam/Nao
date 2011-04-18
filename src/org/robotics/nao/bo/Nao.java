package org.robotics.nao.bo;


public class Nao {
	private static Nao instance = null;
	
	private Nao(){}
	
	public final synchronized static Nao getInstance() {
        if (instance == null) 
            instance = new Nao();
        return instance;
    }
	
	public void walkTo(float x,float y,float theta){
		System.out.println("walkTo : X = " + x + " | Y = "+ y +" | O = " + theta);
	}
	
	public void say(String message){
		System.out.println("say = " + message);
	}
}
