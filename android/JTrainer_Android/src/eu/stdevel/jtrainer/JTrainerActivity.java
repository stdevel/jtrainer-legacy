package eu.stdevel.jtrainer;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class JTrainerActivity extends Activity {
	
	//Klassen-Tag generieren
	private static final String tag = JTrainerActivity.class.getSimpleName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Datenordner erstellen, falls nicht vorhanden
        File jtrainerDir = new File(Environment.getExternalStorageDirectory() + "/data/jtrainer");
        if(jtrainerDir.exists() == false)
        {
        	Log.d(tag, "Directory 'data/jtrainer' does not exists, try to create it.");
        	Boolean success = new File(Environment.getExternalStorageDirectory() + "/data/jtrainer").mkdirs();
        	if(success == false)
        	{
        		//Ordner konnte nicht angelegt werden, Fehlermeldung ausgeben
        		Log.d(tag, "Directory 'data/jtrainer' cannot be created. SD card locked/phone mounted as hard drive?");
				Builder builder = new Builder(this);
				builder.setTitle(getBaseContext().getResources().getString(R.string.tx_dataError).toString())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(getBaseContext().getResources().getString(R.string.tx_dataErrorExpl).toString())
				.setPositiveButton(R.string.tx_back, null);
				builder.show();
        	}
        	else { Log.d(tag, "Directory 'data/jtrainer' created."); }
        }
        
        //Version anzeigen, falls möglich
        Log.d(tag, "Try to determine version for displaying...");
        try {
			String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
	        TextView version = (TextView) findViewById(R.id.tx_Version);
	        version.setText(getBaseContext().getResources().getString(R.string.version).toString() + " " + versionName);
		}
        catch (NameNotFoundException e) { Log.e(tag, "Error while determining version of JTrainer: " + e.getMessage()); }
        
        
    }
    
    public void menueAktion(final View view) {
    	switch(view.getId()) {
    		case R.id.btn_mainStart:		//Starten
    										startActivity(new Intent (this, Open.class));
    										break;
    		
    		case R.id.btn_mainFavorites:	//Favoriten
											startActivity(new Intent (this, Favorites.class));
											break;
    									
    		case R.id.btn_mainSettings:		//Einstellungen
    										startActivity(new Intent (this, Settings.class));
    										break;
    		
    		case R.id.btn_mainExit:			//Beenden
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