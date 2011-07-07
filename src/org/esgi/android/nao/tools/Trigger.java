package org.esgi.android.nao.tools;

public class Trigger 
{
	//-----------------------------------------------------------------------------------------------------------------
	// Private variables
	//-----------------------------------------------------------------------------------------------------------------
	private float m_minThreshold = 0;
	private float m_maxThreshold = 0;
	private float m_currentValue = 0;
	private boolean m_isTrigged  = false;
	
	//-----------------------------------------------------------------------------------------------------------------
	// Getters and Setters
	//-----------------------------------------------------------------------------------------------------------------
	public float getCurrentValue() {return this.m_currentValue;}
	
	public float getMinThreshold() {return this.m_minThreshold;}
	
	public float getMaxThreshold() {return this.m_maxThreshold;}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Constructor
	//-----------------------------------------------------------------------------------------------------------------
	public Trigger() 
	{
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public methods
	//-----------------------------------------------------------------------------------------------------------------
	public boolean isTrigged()
	{
		return this.m_isTrigged;
	}
	
	public void changeValue(float value)
	{
		this.m_currentValue = value;
		
		if ((this.isTrigged()) && (value < this.m_minThreshold))
			this.m_isTrigged = false;
		
		if ((! this.isTrigged()) && (value > this.m_maxThreshold))
			this.m_isTrigged = true;
	}
	
	public void changeThreshold(float min, float max)
	{
		if (max < min)
			return;
		
		this.m_minThreshold = min;
		this.m_maxThreshold = max;
		
		this.m_currentValue = min - 1;
		this.m_isTrigged = false;
	}
}
