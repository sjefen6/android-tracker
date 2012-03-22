package org.goldclone.android.tracker;

import java.util.ArrayList;

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
	public static final String COL_ROUTEID = "_id", COL_ROUTENAME = "routename";
	public static final int COL_ROUTEID_NO = 0, COL_ROUTENAME_NO = 1;

	// SQL som brukes til å opprette tabellen i databasen:
	private static final String TABLE_CREATE_ROUTE = "create table " + DATABASE_TABLE_ROUTES
			+ " (" + COL_ROUTEID + " integer primary key autoincrement, "
			+ COL_ROUTENAME + " text not null"
			+ ");";
	
	/*
	 * RouteTable
	 */
	private static final String DATABASE_TABLE_GEOLOC = "GeoLocTable";
	// Kolonnenavn og kolonneindeks:
	public static final String COL_GEOLOCID = "_id";
	public static final int COL_GEOLOCID_NO = 0;
	
	private static final String COL_ACC = "acc", COL_BEAR ="bear", COL_SPD = "spd"; // floats
	private static final int COL_ACC_NO = 1, COL_BEAR_NO = 2, COL_SPD_NO = 3;
	private static final String COL_ALT = "alt", COL_LATE6 = "latE6", COL_LONE6 = "lonE6"; // doubles
	private static final int COL_ALT_NO = 4, COL_LATE6_NO = 5, COL_LONE6_NO = 6;
	private static final String COL_GEOLOCROUTEID = "routeID"; // long
	private static final int COL_GEOLOCROUTEID_NO = 7;
	
	
	// SQL som brukes til å opprette tabellen i databasen:
	private static final String TABLE_CREATE_GEOLOC = "create table " + DATABASE_TABLE_GEOLOC
			+ " (" + COL_ROUTEID + " integer primary key autoincrement, "
			+ COL_ACC + " real not null, "
			+ COL_BEAR + " real not null, "
			+ COL_SPD + " real not null, "
			+ COL_ALT + " real not null, "
			+ COL_LATE6 + " real not nul, l"
			+ COL_LONE6 + " real not null"
			+ COL_GEOLOCROUTEID + " real not null"
			+ ");";

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
//	public long insertEntry(Contact _myObject) {
//		ContentValues newEntryValue = new ContentValues();
//		newEntryValue.put(COL_LASTNAME, _myObject.getLastname());
//		newEntryValue.put(COL_FIRSTNAME, _myObject.getFirstname());
//		newEntryValue.put(COL_TLF, _myObject.getTlf());
//
//		return db.insert(DATABASE_TABLE, null, newEntryValue);
//	}

	// Fjerne post gitt radindeks:
//	public boolean removeEntry(long _rowIndex) {
//		return db.delete(DATABASE_TABLE, COL_ID + "=" + _rowIndex, null) > 0;
//	}

	// Fjerner alle poster fra tabellen:
//	public boolean removeAllEntries() {
//		return db.delete(DATABASE_TABLE, null, null) > 0;
//	}

	// Henter alle poster fra tabellen og returnerer en cursor:
//	public Cursor getAllEntries() {
//		return db.query(DATABASE_TABLE, new String[] { COL_ID, COL_LASTNAME,
//				COL_FIRSTNAME, COL_TLF }, null, null, null, null, null);
//	}

	// Henter ut en spesifikt Contact (gitt radindeks):
			
	public Route getEntry(long _rowIndex) {
		Cursor result = db.query(true, DATABASE_TABLE_ROUTES, new String[] { COL_ROUTEID,
				COL_ROUTENAME}, COL_ROUTEID + "=" + _rowIndex,
				null, null, null, null, null);
		
		if (result.getCount() == 0 || !result.moveToFirst()) {
			throw new SQLException("Could not find any routes at id " + _rowIndex);
		}
		
		String routename = result.getString(COL_ROUTENAME_NO);
		ArrayList<GeoLoc> locationArray = getAllGeoLoc(_rowIndex);

		Route res = new Route(routename, locationArray);
		return res;
	}
	
	// Henter alle poster fra tabellen og returnerer en cursor:
	private ArrayList<GeoLoc> getAllGeoLoc(long _routeId) {
		Cursor result = db.query(DATABASE_TABLE_GEOLOC, new String[] { COL_GEOLOCID, COL_ACC, COL_ALT, COL_BEAR
				, COL_LATE6, COL_LONE6, COL_SPD, COL_GEOLOCROUTEID}, null, null, null, null, null);
		ArrayList<GeoLoc> res = new ArrayList<GeoLoc>();
//		for(result)
		return null;
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
			_db.execSQL(TABLE_CREATE_ROUTE);
			_db.execSQL(TABLE_CREATE_GEOLOC);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			Log.w("TaskDBAdapter", "Upgrading from version " + _oldVersion
					+ " to " + _newVersion
					+ ", which will destroy all old data");

			// Sletter gammel tabell og oppretter på nytt:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ROUTES);
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_GEOLOC);
			onCreate(_db);
		}
	}
}