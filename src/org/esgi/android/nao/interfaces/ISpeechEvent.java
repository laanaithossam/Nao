package org.esgi.android.nao.interfaces;

public interface ISpeechEvent 
{
	public void onSpeechResult(String speech);
	public void onSpeechCancel();
}
