package org.esgi.android.nao.controllers.items;

import java.util.TimerTask;

import org.esgi.android.nao.controllers.AccelerometerController;
import org.esgi.android.nao.interfaces.IAccelerometerEvent;

/**
 * {@link AccelerometerTask} is used by the {@link AccelerometerController}
 * to schedule its job.
 */
public class AccelerometerTask extends TimerTask 
{
	//-----------------------------------------------------------------------------------------------------------------
	// Private variables
	//-----------------------------------------------------------------------------------------------------------------
	private IAccelerometerEvent m_event = null;
	private boolean m_isPaused = false;
	private float m_x = 0, m_y = 0, m_z = 0;
	
	//-----------------------------------------------------------------------------------------------------------------
	// Constructor
	//-----------------------------------------------------------------------------------------------------------------
	/**
	 * Create the {@link AccelerometerTask} object
	 * 
	 * @param event : the instance of a {@link IAccelerometerEvent} object
	 */
	public AccelerometerTask(IAccelerometerEvent event) 
	{
		this.m_event = event;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public methods
	//-----------------------------------------------------------------------------------------------------------------
	/**
	 * update the value to send to the {@link IAccelerometerEvent}
	 */
	public void setValue(float x, float y, float z)
	{
		this.m_x = x;
		this.m_y = y;
		this.m_z = z;
	}
	
	public void pause()
	{
		this.m_isPaused = true;
	}
	
	public void resume()
	{
		this.m_isPaused = false;
	}
	
	public boolean isPaused()
	{
		return this.m_isPaused;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Implemented method of TimerTask
	//-----------------------------------------------------------------------------------------------------------------
	/**
	 * Send 
	 */
	@Override
	public void run()
	{
		if (! this.m_isPaused)
			this.m_event.onAccelerometerUpdate(this.m_x, this.m_y, this.m_z);
	}

}
