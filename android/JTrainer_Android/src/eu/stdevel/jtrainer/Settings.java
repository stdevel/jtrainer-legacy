package eu.stdevel.jtrainer;

import android.os.Bundle;
import android.preference.PreferenceActivity;

//public class Settings extends PreferenceActivity {
public class Settings extends PreferenceActivity {

	//Klassen-Tag generieren
	//private static final String tag = Settings.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
