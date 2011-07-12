package org.esgi.android.nao;

import org.esgi.android.nao.controllers.AccelerometerController;
import org.esgi.android.nao.controllers.NaoController;
import org.esgi.android.nao.controllers.SpeechController;
import org.esgi.android.nao.interfaces.IAccelerometerEvent;
import org.esgi.android.nao.interfaces.INaoConnectionEvent;
import org.esgi.android.nao.interfaces.INaoListener;
import org.esgi.android.nao.interfaces.ISpeechEvent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 */
public class MainActivity extends Activity implements IAccelerometerEvent, 
													  ISpeechEvent, 
													  INaoConnectionEvent,
													  INaoListener,
													  Runnable
{
	//-----------------------------------------------------------------------------------------------------------------
	// Private variables
	//-----------------------------------------------------------------------------------------------------------------
	private AccelerometerController m_accelerometer = null;
	private SpeechController m_speech = null;
	private NaoController m_nao = null;
	private Bitmap mypicture = null;
	private AlertDialog behaviors_dialog = null;
	private boolean[] behavior_enabled = null;
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
        this.m_accelerometer = new AccelerometerController(sensorManager, this, 100);

        this.m_speech = new SpeechController(this, this);

        this.m_nao = new NaoController(this, this);
		final String[] items = m_nao.getHardcodedBehaviors();
		behavior_enabled = new boolean[items.length];

		for (int i = 0; i < items.length; i++)
			behavior_enabled[i] = false;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick a behavior");
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	if (behavior_enabled[item] == true) {
		    		Toast.makeText(getApplicationContext(), "stop: " + items[item], Toast.LENGTH_SHORT).show();
		    		m_nao.StopBehavior(items[item]);
		    		behavior_enabled[item] = false;
		    	}
		    	else {
		    		Toast.makeText(getApplicationContext(), "run: " + items[item], Toast.LENGTH_SHORT).show();
		    		m_nao.RunBehavior(items[item]);
		    		behavior_enabled[item] = true;
		    	}
		    }
		});
		behaviors_dialog = builder.create();
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
    			this.m_nao.StopWalk();
    			this.m_nao.requestPicture();
    		}
    		else
    		{
    			this.m_nao.StandUp();
    			this.m_accelerometer.startReading();
    		}
    		return true;
    	}
    	else if (item.getItemId() == R.id.item_speech)
    	{
    		behaviors_dialog.show();
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
		float moveX = (float) 0;
		float moveY = (float) 0;
		float moveTheta = (float) 0;
	
		if (x < -3.5)
		{
			moveX = (float) -((x+3.5)/(10.0-3.5));
		}
		else if (x > 3.5)
		{
			moveX = (float) -((x-3.5)/(10.0-3.5));			
		}
		else
		{
			// dead zone
			moveX = (float) 0.0;
		}
		moveY = (float) 0.0;
        
		if (y < -3.5)
		{
			moveTheta = (float) -((y+3.5)/(10.0-3.5));
		}
		else if (y > 3.5)
		{
			moveTheta = (float) -((y-3.5)/(10.0-3.5));			
		}
		else
		{
			// dead zone
			moveTheta = (float) 0.0;
		}
  		
		this.m_nao.walkTo(moveX, moveY, moveTheta);	
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
		Toast.makeText(this.getApplicationContext(), "Nao is now fully connected", Toast.LENGTH_LONG).show();
		this.m_nao.say("I'm connected to your Android device");
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
		
		ImageView picture = (ImageView) this.findViewById(R.id.picture);
		if (this.m_accelerometer.isRunning())
		{
			picture.setImageBitmap(this.mypicture);
		}
		else
		{
		}
	}


	@Override
	public void ongetInstalledBehaviors(String[] behaviors) {
		for (String behavior: behaviors)
			Log.e("behavior",behavior);
		
	}

	@Override
	public void ongetVolume(int vol) {
		Log.i("NAO", "volume: " + vol);
		
	}

	/**
	 * Just doesn't work..
	 */
	@Override
	public void onpictureAvailable(Bitmap picture) {
		this.mypicture = picture;
	}
}
