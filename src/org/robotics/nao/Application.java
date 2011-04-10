package org.robotics.nao;

import org.robotics.nao.view.SpeechRecognition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

public class Application extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	SubMenu m = menu.addSubMenu(0, 1, 0, "Speak");
    	m.setIcon(android.R.drawable.ic_btn_speak_now);
    	return super.onCreateOptionsMenu(menu);
    }
    @Override
    //Onclick MenuItem
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent;
    	switch (item.getItemId()) {
		case 1:
			intent=new Intent(Application.this,SpeechRecognition.class);
	        startActivity(intent);
			break;
		default:
			break;
		}
       	return super.onContextItemSelected(item);
    }
}