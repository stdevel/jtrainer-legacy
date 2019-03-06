package eu.stdevel.jtrainer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

	//Klassen-Tag generieren
	private static final String tag = DatabaseManager.class.getSimpleName();
	
	//Datenbank-Konstanten
	private static final String DB_NAME = "jtrainer.db";
	private static final int DB_VERSION = 2;
	private static final String SQL_CREATE = "CREATE TABLE favorites (_id INTEGER PRIMARY KEY AUTOINCREMENT, filename TEXT NOT NULL)";
	private static final String SQL_DROP = "DROP TABLE IF EXISTS favorites";

	public DatabaseManager(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
		Log.d(tag, "Created database");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP);
		onCreate(db);
		Log.d(tag, "Rebuild database");
	}

}
