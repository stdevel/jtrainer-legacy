package eu.stdevel.jtrainer;

import java.io.File;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

//public class Favorites extends Activity {
public class Favorites extends ListActivity {
	
	//Klassen-Tag generieren
	private static final String tag = Favorites.class.getSimpleName();
	private static final int ACTIVITY_FRAGEN = 1000;
	
	//Datenbankdeklaration
	private SQLiteDatabase mDatabase;
	private DatabaseManager mHelper;
	private int rowId;
	private String filename;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Layout setzen und Datenbank instanzieren
        setContentView(R.layout.favorites);
        mHelper = new DatabaseManager(this);
        
        //ListView um Listener erweitern
        final ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setOnItemClickListener(new OnItemClickListener() {
              public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
              {
            	  //Cursor und Adapter instanzieren
            	  final SimpleCursorAdapter adp = (SimpleCursorAdapter) myAdapter.getAdapter();
            	  final Cursor cursor = adp.getCursor();
            	  int colIndex = cursor.getColumnIndex("_id");
            	  
            	  //Dateiname beziehen
            	  colIndex = cursor.getColumnIndex("filename");
            	  filename = cursor.getString(colIndex);
            	  
            	  //Dialog zur Aktionauswahl anzeigen
            	  Builder builder = new Builder(Favorites.this);
            	  builder.setTitle(getBaseContext().getResources().getString(R.string.tx_chooseAction))
            	  .setItems(R.array.favActions, new OnClickListener() {
            		  
            		  @Override
            		  public void onClick(DialogInterface dialog, int which)
            		  {
            			  //Je nach Modus Aktion ausführen
            			  if(getResources().getStringArray(R.array.favActionsValues)[which].equals("delete"))
            			  {
            				  //Aus Favoriten entfernen und Datei löschen
            				  Log.d(tag, "About to PURGE catalog file (" + filename + ").");
            				  mDatabase.delete("favorites", "filename=?", new String[] { filename });
            				  loadFavorites();
            				  File f = new File(Environment.getExternalStorageDirectory()+"/"+filename);
            				  f.delete();
            				  Toast.makeText(Favorites.this, getBaseContext().getResources().getString(R.string.tx_fileDeleted), Toast.LENGTH_SHORT).show();
            			  }
            			  else if(getResources().getStringArray(R.array.favActionsValues)[which].equals("unlink"))
            			  {
            				  //Aus Favoriten entfernen
            				  Log.d(tag, "About to remove catalog file (" + filename + ") from favorites.");
            				  mDatabase.delete("favorites", "filename=?", new String[] { filename });
            				  loadFavorites();
            				  Toast.makeText(Favorites.this, getBaseContext().getResources().getString(R.string.tx_entryDeleted), Toast.LENGTH_SHORT).show();
            			  }
            			  else
            			  {
            				  //Öffnen
            				  Log.d(tag, "About to open catalog file (" + filename + ") with id #" + rowId + ".");
            				  Intent frage = new Intent(Favorites.this, Question.class);
            				  frage.putExtra("filename", filename);
            				  startActivityForResult(frage, ACTIVITY_FRAGEN);
            			  }
            			  
            			  //Dialog schließen
            			  dialog.dismiss();
            		  }
    			})
    			.show();
              }                 
        });
        
    }
    
	@Override
	protected void onPause() {
		//Datenbank schließen
		mDatabase.close();
		Log.d(tag, "Database closed");
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//Datenbank öffnen
		mDatabase = mHelper.getReadableDatabase();
		Log.d(tag, "Database opened");
		loadFavorites();
	}
	
	private void loadFavorites()
	/* Favoriten laden */
	{
		//Daten auslesen
		Cursor favCursor = mDatabase.query("favorites", new String[] { "_id", "filename" }, null, null, null, null, null);
		startManagingCursor(favCursor);
		SimpleCursorAdapter favAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, favCursor, new String[] {"filename"}, new int[] { android.R.id.text1 });
		setListAdapter(favAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return super.onCreateOptionsMenu(menu);
	}

    public void menueAktion(final View view) {
    	switch(view.getId())
    	{
    		case R.id.btn_favoritesSave:		//Favorit speichern
    			
    												//EditText instanzieren und Favorit speichern, falls eingegebener Pfad existiert
    												EditText et = (EditText) findViewById(R.id.tx_favoritesFileName);
    												if(et.getText().toString().length() > 0)
    												{
    													//Überpüfen, ob der Pfad existiert
    													File targetFile = new File(Environment.getExternalStorageDirectory() + "/" +et.getText().toString());
    													if(targetFile.exists())
    													{
    														//Ja, Eintrag speichern
    														ContentValues favValues = new ContentValues();
    														favValues.put("filename", et.getText().toString());
    														mDatabase.insert("favorites", null, favValues);
    														loadFavorites();
    														et.setText(getBaseContext().getResources().getString(R.string.standardPath));
    													}
    													else { Toast.makeText(this, getBaseContext().getResources().getString(R.string.tx_nonexistingFavWarning), Toast.LENGTH_SHORT).show(); }
    												}
    												else { Toast.makeText(this, getBaseContext().getResources().getString(R.string.tx_emptyFavWarning), Toast.LENGTH_SHORT).show(); }
    											
    											break;

    	}
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