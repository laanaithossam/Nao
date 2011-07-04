package org.esgi.android.nao;

import org.esgi.android.nao.controllers.AccelerometerController;
import org.esgi.android.nao.controllers.NaoController;
import org.esgi.android.nao.controllers.SpeechController;
import org.esgi.android.nao.interfaces.IAccelerometerEvent;
import org.esgi.android.nao.interfaces.INaoEvent;
import org.esgi.android.nao.interfaces.ISpeechEvent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 */
public class MainActivity extends Activity implements IAccelerometerEvent, ISpeechEvent, INaoEvent, Runnable
{
	//-----------------------------------------------------------------------------------------------------------------
	// Private variables
	//-----------------------------------------------------------------------------------------------------------------
	private AccelerometerController m_accelerometer = null;
	private SpeechController m_speech = null;
	private NaoController m_nao = null;
	
	private float acc_x, acc_y, acc_z;
	
	//-----------------------------------------------------------------------------------------------------------------
	// Override methods
	//-----------------------------------------------------------------------------------------------------------------
	/**
	 *  onCreate methods is called when the application is first created.
	 *  It Create all elements need, and put in place them.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        
        SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        this.m_accelerometer = new AccelerometerController(sensorManager, this, 500);
        
        this.m_speech = new SpeechController(this, this);
        
        this.m_nao = new NaoController(this);
        this.m_nao.connect();
    }
    
    /**
     * onResume method is called when the application is resumed.
     * Here we resume all item need to be resumed.
     */
    @Override
    protected void onResume() 
    {
    	this.m_accelerometer.resumeReading();
    	super.onResume();
    }
    
    /**
     * onPause method is called when the application is put in pause.
     * He we put in pause all item need to be put in pause.
     */
    @Override
    protected void onPause() 
    {
    	super.onPause();
    	
    	this.m_accelerometer.pauseReading();
    }
    
    /**
     * onStop method is called when the application is stopped.
     * Here, we stop all controllers.
     */
    @Override
    protected void onStop()
    {
    	super.onStop();
    	
    	this.m_accelerometer.stopReading();
    }
    
    /**
     * onCreateOptionMenu method is called when the application need
     * know if the {@link MainActivity} need create a menu.
     * Here we have a menu for choose the application actions.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	MenuInflater inflater = this.getMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
    	return true;
    }
    
    /**
     * onActivityResult is called when an other activity need to transfer 
     * parameter to this activity.
     * Here we get parameter of the speech.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	this.m_speech.setActivityResult(requestCode, resultCode, data);
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * onPrepareOptionsMenu method is called before to show the
     * menu.
     * here we change the text of the {@link MenuItem} if it's needed
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) 
    {
    	MenuItem walkMenu = menu.findItem(R.id.item_walk);
    	
    	if (this.m_accelerometer.isRunning())
    		walkMenu.setTitle(R.string.menu_walk_off);
    	else
    		walkMenu.setTitle(R.string.menu_walk_on);
    	
    	return true;
    }
    
    /**
     * onContextItemSelected method is called when the user have
     * clicked on a item of the menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	if (item.getItemId() == R.id.item_leave)
    	{
    		this.finish();
    		return true;
    	}
    	else if (item.getItemId() == R.id.item_walk)
    	{
    		
    		if (this.m_accelerometer.isRunning())
    		{
    			this.m_accelerometer.stopReading();
    		}
    		else
    		{
    			this.m_accelerometer.startReading();
    		}
    		
    		return true;
    	}
    	else if (item.getItemId() == R.id.item_speech)
    	{
    		this.m_speech.startSpeech();
    		return true;
    	}
    	else if (item.getItemId() == R.id.item_cam)
    	{
    		return false;
    	}
    	
    	return false;
    }
    
    //-----------------------------------------------------------------------------------------------------------------
    // Implemented IAccelerometerEvent methods
    //-----------------------------------------------------------------------------------------------------------------
    /**
     * Called when the {@link AccelerometerController} can't start the
     * accelerometer.
     */
	@Override
	public void onAccelerometerNoSupported() 
	{
		Toast.makeText(this.getApplicationContext(), R.string.notify_walk_error, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Called when the {@link AccelerometerController} have refresh the
	 * accelerometer value.
	 */
	@Override
	public void onAccelerometerUpdate(float x, float y, float z) 
	{
		this.acc_x = x;
		this.acc_y = y;
		this.acc_z = z;
		
		this.runOnUiThread(this);
	}
	
	/**
	 * Called when the {@link AccelerometerController} is started.
	 */
	@Override
	public void onAccelerometerStart() 
	{
		Toast.makeText(this.getApplicationContext(), R.string.notify_walk_on, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Called when the {@link AccelerometerController} is stopped
	 */
	@Override
	public void onAccelerometerStop() 
	{
		Toast.makeText(this.getApplicationContext(), R.string.notify_walk_off, Toast.LENGTH_SHORT).show();
		this.runOnUiThread(this);
	}
	
	//-----------------------------------------------------------------------------------------------------------------
    // Implemented ISpeechEvent methods
    //-----------------------------------------------------------------------------------------------------------------
	/**
	 * Called when the speech return a new data
	 * 
	 * @param speech : the message receive by the speech controller
	 */
	@Override
	public void onSpeechResult(String speech) 
	{
		Toast.makeText(this.getApplicationContext(), speech, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Called when the speech has been canceled.
	 */
	@Override
	public void onSpeechCancel() 
	{
		Log.i("AndroTest", "Speech canceled");
	}
	
	//-----------------------------------------------------------------------------------------------------------------
    // Implemented INaoEvent methods
    //-----------------------------------------------------------------------------------------------------------------
	
	@Override
	public void onConnected() 
	{
		
	}

	@Override
	public void onErrorConnection() 
	{
		Toast.makeText(this.getApplicationContext(), "Can't connect to NAO ! Application exited", Toast.LENGTH_LONG).show();
		this.finish();
	}

	
	//-----------------------------------------------------------------------------------------------------------------
    // Implemented Runnable methods
    //-----------------------------------------------------------------------------------------------------------------
	/**
	 * Called when the View need to be refreshed.
	 */
	@Override
	public void run() 
	{
		TextView txt_x = (TextView) this.findViewById(R.id.axisx);
		TextView txt_y = (TextView) this.findViewById(R.id.axisy);
		TextView txt_z = (TextView) this.findViewById(R.id.axisz);
		
		if (this.m_accelerometer.isRunning())
		{
			txt_x.setText("The axis x : " + this.acc_x);
			txt_y.setText("The axis y : " + this.acc_y);
			txt_z.setText("The axis z : " + this.acc_z);
		}
		else
		{
			txt_x.setText("waiting");
			txt_y.setText("waiting");
			txt_z.setText("waiting");
		}
	}
}