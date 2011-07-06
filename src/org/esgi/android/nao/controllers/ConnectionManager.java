package org.esgi.android.nao.controllers;

import java.util.HashMap;

import org.xmlrpc.android.XMLRPCException;

import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;

import com.naoqi.remotecomm.ALBroker;
import com.naoqi.remotecomm.ALProxy;

public class ConnectionManager implements Callback {
	ALBroker fBroker = null;

	public final String[] almodules = {
			"ALTextToSpeech",
			"ALAudioDevice",
			"ALMotion",
			"ALLeds",
			"ALLogger",
			"ALRobotPose",
			"ALBehaviorManager",
			"ALSubscribe",
			"ALMemory",
			"AWPreferences"
	};

	private final HashMap<String, ALProxy> proxies = new HashMap<String, ALProxy>();

	public enum connexion_state {
		STATE_OFFLINE,
		STATE_CONNECTING,
		STATE_CONNECTED,
		STATE_NAO_PRESENCE,
		STATE_NAO_RESPONDING
	};

	public enum connexion_event {
		EVENT_CONNECTING,
		EVENT_CONNECT,
		EVENT_DISCONNECT,
		EVENT_NAO_PRESENCE,
		EVENT_NAO_RESPONSE;
	}

	public connexion_state state = connexion_state.STATE_OFFLINE;

	private static ConnectionManager instance = null;

	private ConnectionManager(String robotname, String password){
		init_proxies(robotname, password);
	}

	public final synchronized static ConnectionManager getInstance(String robotname, String password) {
        if (instance == null)
            instance = new ConnectionManager(robotname, password);
        return instance;
    }

	private void init_proxies(String robotname, String password) {
		fBroker = new ALBroker(robotname, password, this);
		ALProxy proxy = null;

		for (String module_name : almodules) {
		try {
			proxy = new ALProxy(module_name, fBroker);
			proxies.put(module_name, proxy);
		}
		catch (XMLRPCException e) {
			e.printStackTrace();
			}
		}

		proxy = proxies.get("AWPreferences");
		if (null != proxy)
			proxy.setDestinationJabberId( "_preferences@xmpp.aldebaran-robotics.com" );
	}

	/*
	 * @return Return a proxy for a given module name
	 */
	public ALProxy getProxy(String module_name) {
		return proxies.get(module_name);
	}

	public void connexion_postCall( ALProxy proxy, String method, Object ... params ){

	    	if (fBroker==null) return;

	    	StringBuilder text = new StringBuilder();
	    	text.append(method);
	    	for (Object object : params){
	    		text.append(" ");
	    		text.append(object.toString());
	    	}

		    try {
		    	proxy.postCall(method, params);
			} catch (Exception e) {
				connexion_exception(e);
			}
	    }

	public void connexion_asyncCall( long timeoutMillis, final ALProxy.MethodResponseListener listener, ALProxy proxy, final String method, Object ... params  ){

	    	if (fBroker==null)
	    		return;

	    	StringBuilder text = new StringBuilder();
	    	text.append(method);
		    	if (params.length>0){
		    		text.append(" ");
		    		text.append(params[0].toString());
		    	}

	    	try {
	    		proxy.asyncCall(
	    				timeoutMillis,
	    				new ALProxy.MethodResponseListener(){
	    					@Override
	    					public void onResponse(Object result  ) {
	    						if (listener!=null)
	    							listener.onResponse(result);
	    					}
	    				}
	    				,method, params);
	    	} catch (XMLRPCException e) {
	    		connexion_exception(e);
	    	}
	    }

	 public void connexion_exception( Exception e ){
			/// disconnect in case of problem
			connexion_event(connexion_event.EVENT_DISCONNECT);
		}

	public void connexion_event( connexion_event event ) {
	    	Log.i("connexion event", String.format("state: %s event: %s", state2string(state), event2string(event)));
	    	// all states
	    	if (event == connexion_event.EVENT_DISCONNECT
	    			&& state != connexion_state.STATE_OFFLINE ){
	    			state = connexion_state.STATE_OFFLINE;

	    		if (fBroker !=null) {
	    			fBroker.disconnect();
	    			fBroker = null;
	    		}
			}

	    	// state machine
	    	switch( state ){
	    	case STATE_OFFLINE:
	    		if (event == connexion_event.EVENT_CONNECTING)
	    			state = connexion_state.STATE_CONNECTING;
	    		break;
	    	case STATE_CONNECTING:
	    		if (event == connexion_event.EVENT_CONNECT)
	    			state = connexion_state.STATE_CONNECTED;
	    		break;
	    	case STATE_CONNECTED:
	    		if (event == connexion_event.EVENT_NAO_PRESENCE) {
	    				state = connexion_state.STATE_NAO_PRESENCE;
	    				isResponding();
	    		}
	    		break;
	    	case STATE_NAO_PRESENCE:
	    		if (event == connexion_event.EVENT_NAO_RESPONSE)
	    			state = connexion_state.STATE_NAO_RESPONDING;
	    		break;
	    	case STATE_NAO_RESPONDING:
	    		state = connexion_state.STATE_NAO_RESPONDING;
	    		break;
    	}
	}

	public void isResponding() {
		ALProxy fTextToSpeechProxy = proxies.get("ALTextToSpeech");
		connexion_asyncCall( 10000,
				new ALProxy.MethodResponseListener(){
					public void onResponse(Object result) {
						if (result!=null) // timeout
							connexion_event(connexion_event.EVENT_NAO_RESPONSE);
						return;
					}
				}
				, fTextToSpeechProxy, "version" );
	}

	/*
	 * to string method for connexion_event
	 * @ return a string for a connexion_event
	 */
	public String event2string(connexion_event event) {
		switch (event) {
		case EVENT_CONNECT:
			return "EVENT_CONNECT";
		case EVENT_CONNECTING:
			return "EVENT_CONNECTING";
		case EVENT_DISCONNECT:
			return "EVENT_DISCONNECT";
		case EVENT_NAO_PRESENCE:
			return "EVENT_NAO_PRESENCE";
		case EVENT_NAO_RESPONSE:
			return "EVENT_NAO_RESPONSE";
		}
		return "UNKNOWN EVENT";
	}

	/*
	 * to string method for connexion_state
	 * @ return a string for a connexion_state
	 */
	public String state2string(connexion_state state) {
		switch (state) {
		case STATE_OFFLINE:
			return "STATE_OFFLINE";
		case STATE_CONNECTING:
			return "STATE_CONNECTING";
		case STATE_CONNECTED:
			return "STATE_CONNECTED";
		case STATE_NAO_PRESENCE:
			return "STATE_NAO_PRESENCE";
		case STATE_NAO_RESPONDING:
			return "STATE_NAO_RESPONDING";
		}
		return "UNKNOWN_STATE";
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
