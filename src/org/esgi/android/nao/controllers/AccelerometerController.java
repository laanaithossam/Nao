package org.esgi.android.nao.controllers;

import java.util.Timer;

import org.esgi.android.nao.controllers.items.AccelerometerTask;
import org.esgi.android.nao.interfaces.IAccelerometerEvent;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * {@link AccelerometerController} is used to get the android accelerometer
 * values.
 * You must use {@link AccelerometerController#startReading()} for start the reading,
 * and {@link AccelerometerController#stopReading()} for stop it.
 * All refresh values will be sent to the {@link IAccelerometerEvent}, specified in the
 * constructor.
 * 
 * @see IAccelerometerEvent
 */
public class AccelerometerController implements SensorEventListener
{
	//-----------------------------------------------------------------------------------------------------------------
	// Private variables
	//-----------------------------------------------------------------------------------------------------------------
	private SensorManager m_sensorManager = null;
	private IAccelerometerEvent m_event   = null;
	
	private Timer m_timer = null;
	private AccelerometerTask m_accTask = null;
	
	private boolean m_isRunning = false;
	
	private int m_refreshTime = 0;
	
	//-----------------------------------------------------------------------------------------------------------------
	// Constructor
	//-----------------------------------------------------------------------------------------------------------------
	/**
	 * Create {@link AccelerometerController} object
	 * 
	 * @param sensor      : the {@link SensorManager} to have access to the accelerometer sensor
	 * @param event       : the {@link IAccelerometerEvent} instance to return all events
	 * @param refreshTime : the interval between the all sensor refreshes
	 */
	public AccelerometerController(SensorManager sensor, IAccelerometerEvent event, int refreshTime) 
	{
		this.m_refreshTime = refreshTime;
		this.m_sensorManager = sensor;
		this.m_event = event;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public methods
	//-----------------------------------------------------------------------------------------------------------------
	/**
	 * Start the Accelerometer reading
	 */
	public void startReading()
	{
		// Test the running status
		if (this.m_isRunning)
			return;
		
		// Try to start the sensor reading
		boolean sensorSupported = this.m_sensorManager.registerListener(this, this.m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
		
		// If the sensor isn't supported 
		if (!sensorSupported)
		{
			this.m_sensorManager.unregisterListener(this, this.m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
			this.m_event.onAccelerometerNoSupported();
			this.m_isRunning = false;
			return;
		}
		
		// Start a new Timer
		this.m_timer   = new Timer();
		this.m_accTask = new AccelerometerTask(this.m_event);
		this.m_timer.schedule(this.m_accTask, 0, this.m_refreshTime);
		
		this.m_isRunning = true;
		this.m_event.onAccelerometerStart();
	}
	
	/**
	 * Stop the accelerometer reading
	 */
	public void stopReading()
	{
		// Test the running status
		if (!this.m_isRunning)
			return;
		
		// Stop the sensor reading, and the timer
		this.m_sensorManager.unregisterListener(this, this.m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		this.m_timer.cancel();
		
		// Free the memory
		this.m_timer = null;
		this.m_accTask = null;
		
		this.m_isRunning = false;
		this.m_event.onAccelerometerStop();
	}
	
	/**
	 * Put in pause the controller.
	 * this method don't stop the activity of the controller,
	 * but don't send the values to the event.
	 */
	public void pauseReading()
	{
		if (this.m_accTask != null)
			this.m_accTask.pause();
	}
	
	/**
	 * Resume the controller
	 */
	public void resumeReading()
	{
		if (this.m_accTask != null)
			this.m_accTask.resume();
	}
	
	/**
	 * Get if the Accelerometer reading is working
	 * 
	 * @return true if it work, else false.
	 */
	public boolean isRunning()
	{
		return this.m_isRunning;
	}
	
	/**
	 * Get if the Accelerometer reading is in pause mode.
	 * 
	 * @return true if it in pause mode, else false.
	 */
	public boolean isPaused()
	{
		if (this.m_accTask == null)
			return false;
		return this.m_accTask.isPaused();
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Implemented SensorEventListner methods 
	//-----------------------------------------------------------------------------------------------------------------
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * Called by the {@link SensorManager} when the value has been
	 * updated
	 */
	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		this.m_accTask.setValue(event.values[0], event.values[1], event.values[2]);
	}
}
