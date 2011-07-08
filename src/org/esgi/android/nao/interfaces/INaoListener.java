package org.esgi.android.nao.interfaces;

import android.graphics.Bitmap;

public interface INaoListener 
{
	void ongetInstalledBehaviors(String[] behaviors);
	void ongetVolume(int vol);
	void onpictureAvailable(Bitmap picture);
}
