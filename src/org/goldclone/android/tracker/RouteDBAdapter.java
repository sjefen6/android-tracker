package org.goldclone.android.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class RouteDBAdapter {
	private static final String DATABASE_NAME = "Route.db";
	private static final int DATABASE_VERSION = 1;
	
	/*
	 * RouteTable
	 */
	private static final String DATABASE_TABLE_ROUTES = "RouteTable";
	// Kolonnenavn og kolonneindeks:
	public static final String COL_ROUTEID = "_id";
	public static final int COL_ROUTEID_NO = 0;

	public static final String COL_ROUTENAME = "routename";
	public static final int COL_LASTNAME_NO = 1;

	// SQL som brukes til å opprette tabellen i databasen:
	private static final String TABLE_CREATE_ROUTE = "create table " + DATABASE_TABLE_ROUTES
			+ " (" + COL_ROUTEID + " integer primary key autoincrement, "
			+ COL_ROUTENAME + " text not null)";
	
	/*
	 * RouteTable
	 */
	private static final String DATABASE_TABLE_GEOLOC = "RouteTable";
	// Kolonnenavn og kolonneindeks:
	public static final String COL_GEOLOCID = "_id";
	public static final int COL_GEOLOCID_NO = 0;

	public static final String COL_GEOLOCNAME = "routename";
	public static final int COL_GEOLOCNAME_NO = 1;

	// SQL som brukes til å opprette tabellen i databasen:
	private static final String TABLE_CREATE = "create table " + DATABASE_TABLE_ROUTES
			+ " (" + COL_ROUTEID + " integer primary key autoincrement, "
			+ COL_ROUTENAME + " text not null)";

	// Databaseinstansen:
	private SQLiteDatabase db;
	// Context til app som bruker databasen:
	private final Context context;
	// Hjelpeklasse:
	private MyDBHelper dbHelper;

	// Konstruktør, databasen opprettes:
	public RouteDBAdapter(Context _context) {
		context = _context;
		// Databasen opprettes:
		dbHelper = new MyDBHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	// Åpner databasen for skriving (evt. kun lesing):
	public RouteDBAdapter open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
		return this;
	}

	// Lukker:
	public void close() {
		db.close();
	}

	// Legge til post i tabellen:
	public long insertEntry(Contact _myObject) {
		ContentValues newEntryValue = new ContentValues();
		newEntryValue.put(COL_LASTNAME, _myObject.getLastname());
		newEntryValue.put(COL_FIRSTNAME, _myObject.getFirstname());
		newEntryValue.put(COL_TLF, _myObject.getTlf());

		return db.insert(DATABASE_TABLE, null, newEntryValue);
	}

	// Fjerne post gitt radindeks:
	public boolean removeEntry(long _rowIndex) {
		return db.delete(DATABASE_TABLE, COL_ID + "=" + _rowIndex, null) > 0;
	}

	// Fjerner alle poster fra tabellen:
	public boolean removeAllEntries() {
		return db.delete(DATABASE_TABLE, null, null) > 0;
	}

	// Henter alle poster fra tabellen og returnerer en cursor:
	public Cursor getAllEntries() {
		return db.query(DATABASE_TABLE, new String[] { COL_ID, COL_LASTNAME,
				COL_FIRSTNAME, COL_TLF }, null, null, null, null, null);
	}

	// Henter ut en spesifikt Contact (gitt radindeks):
	public Contact getEntry(long _rowIndex) {
		Cursor result = db.query(true, DATABASE_TABLE, new String[] { COL_ID,
				COL_LASTNAME, COL_FIRSTNAME, COL_TLF }, COL_ID + "="
				+ _rowIndex, null, null, null, null, null);
		if (result.getCount() == 0 || !result.moveToFirst()) {
			throw new SQLException("Fant ingen kontakter i index " + _rowIndex);
		}
		String lastname = result.getString(COL_LASTNAME_NO);
		String firstname = result.getString(COL_FIRSTNAME_NO);
		String tlf = result.getString(COL_TLF_NO);

		Contact res = new Contact(lastname, firstname, tlf);
		return res;
	}

	// ////////////////
	// / MyDBHelper ///
	// ////////////////
	private static class MyDBHelper extends SQLiteOpenHelper {

		public MyDBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			Log.w("TaskDBAdapter", "Upgrading from version " + _oldVersion
					+ " to " + _newVersion
					+ ", which will destroy all old data");

			// Sletter gammel tabell og oppretter på nytt:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(_db);
		}
	}
}