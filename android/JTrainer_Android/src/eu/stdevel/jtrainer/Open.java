package eu.stdevel.jtrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Open extends Activity {
	
	//Klassen-Tag generieren
	private static final String tag = Open.class.getSimpleName();
	private static final int ACTIVITY_FRAGEN = 1000;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open);
    }
    
    public void menueAktion(final View view) {
    	switch(view.getId()) {
    		case R.id.btn_openOpenFile:		//Datei öffnen
    									
    										//Versuche Katalog zu laden
    										EditText et = (EditText) findViewById(R.id.tx_openFileTxt);
    										String target = et.getText().toString();
    										Log.d(tag, "Try to load catalog (" + target + ")");
    										Intent frage = new Intent(this, Question.class);
    										frage.putExtra("filename", target);
    										
    										//Activity starten
    										startActivityForResult(frage, ACTIVITY_FRAGEN);
    									
    									break;
    									
    		case R.id.btn_openBack:		//Zurück
    										this.finish();
    									break;
    	
    	}
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.mnu_help:			//Hilfe
										startActivity(new Intent (this, Browser.class));
										break;
										
			case R.id.mnu_license:		//Lizenz
										Toast.makeText(this, getBaseContext().getResources().getString(R.string.tx_settings_license), Toast.LENGTH_LONG).show();
										break;
		}
		return super.onOptionsItemSelected(item);
	}
    
}