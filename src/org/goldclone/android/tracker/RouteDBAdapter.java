package org.goldclone.android.tracker;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.widget.Toast;

public class RouteDBAdapter {
	private static final String DATABASE_NAME = "Route.db";
	private static final int DATABASE_VERSION = 4;
	
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
	 * GeoLocTable
	 */
	private static final String DATABASE_TABLE_GEOLOC = "GeoLocTable";
	// Kolonnenavn og kolonneindeks:
	public static final String COL_GEOLOCID = "_id";
	public static final int COL_GEOLOCID_NO = 0;
	
	private static final String COL_ACC = "acc", COL_BEAR ="bear", COL_SPD = "spd"; // floats
	private static final int COL_ACC_NO = 1, COL_BEAR_NO = 2, COL_SPD_NO = 3;
	private static final String COL_ALT = "alt", COL_LATE6 = "latE6", COL_LONE6 = "lonE6"; // doubles
	private static final int COL_ALT_NO = 4, COL_LATE6_NO = 5, COL_LONE6_NO = 6;
	private static final String COL_GEOLOCROUTEID = "routeID", COL_TIME = "time"; // long
	private static final int COL_GEOLOCROUTEID_NO = 7, COL_TIME_NO = 8;
	
	
	// SQL som brukes til å opprette tabellen i databasen:
	private static final String TABLE_CREATE_GEOLOC = "create table " + DATABASE_TABLE_GEOLOC
			+ " (" + COL_ROUTEID + " integer primary key autoincrement, "
			+ COL_ACC + " real not null, "
			+ COL_BEAR + " real not null, "
			+ COL_SPD + " real not null, "
			+ COL_ALT + " real not null, "
			+ COL_LATE6 + " real not null, "
			+ COL_LONE6 + " real not null, "
			+ COL_GEOLOCROUTEID + " real not null, "
			+ COL_TIME + " real not null"
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

	// Legge til Route i tabellen:
	public long insertEntry(Route _newRoute) {
		ContentValues newEntryValue = new ContentValues();
		newEntryValue.put(COL_ROUTENAME, _newRoute.getName());
		
		Toast.makeText(context, "insertRoute", Toast.LENGTH_SHORT).show();
		
		long errorLvl = db.insert(DATABASE_TABLE_ROUTES, null, newEntryValue);
		
		if(errorLvl != -1){
			Cursor result = db.query(true, DATABASE_TABLE_ROUTES, new String[] { COL_ROUTEID},
					COL_ROUTENAME + " = \"" + _newRoute.getName() + "\"", null, null, null, null, null);
			
			if (result.getCount() == 0 || !result.moveToFirst()) {
				throw new SQLException("Derp!");
			}
			
			for (GeoLoc g : _newRoute.getArray()) {
				if (errorLvl != -1) {

					Toast.makeText(context, "insertGeoLoc", Toast.LENGTH_SHORT).show();
					
					newEntryValue = new ContentValues();
					newEntryValue.put(COL_ACC, g.getAcc());
					newEntryValue.put(COL_BEAR, g.getBear());
					newEntryValue.put(COL_SPD, g.getSpd());
					newEntryValue.put(COL_ALT, g.getAlt());
					newEntryValue.put(COL_LATE6, g.getLatE6());
					newEntryValue.put(COL_LONE6, g.getLonE6());
					newEntryValue.put(COL_GEOLOCROUTEID,
							result.getString(COL_ROUTEID_NO));
					newEntryValue.put(COL_TIME, g.getTime());

					errorLvl = db.insert(DATABASE_TABLE_GEOLOC, null,
							newEntryValue);
				}
			}
		}
		return errorLvl;
	}
	
	// Henter ut en spesifikt Contact (gitt radindeks):
			
	public Route getEntry(long _rowIndex) {
		Cursor result = db.query(true, DATABASE_TABLE_ROUTES, new String[] { COL_ROUTEID,
				COL_ROUTENAME}, COL_ROUTEID + "=" + _rowIndex,
				null, null, null, null, null);
		
		if (result.getCount() == 0 || !result.moveToFirst()) {
			throw new SQLException("Could not find any routes at id " + _rowIndex);
		}
		
		Toast.makeText(context, "getRoute", Toast.LENGTH_SHORT).show();
		
		String routename = result.getString(COL_ROUTENAME_NO);
		ArrayList<GeoLoc> locationArray = getAllGeoLoc(_rowIndex);

		Route res = new Route(routename, locationArray);
		return res;
	}
	
	public ArrayList<String[]> getAllRoutes(){
		Cursor result = db.query(true, DATABASE_TABLE_ROUTES, new String[] { COL_ROUTEID,
				COL_ROUTENAME}, null, null, null, null, null, null);
		
		ArrayList <String[]> res = new ArrayList <String[]>();
		
		if (result.moveToFirst()) {
			while (!result.isAfterLast()) {
				String[] s = {result.getString(COL_ROUTEID_NO), result.getString(COL_ROUTENAME_NO)};
				res.add(s);
				result.moveToNext();
			}
		}
		return res;
	}
	
	// Henter alle poster fra tabellen og returnerer en cursor:
	private ArrayList<GeoLoc> getAllGeoLoc(long _routeId) {
		Cursor result = db.query(true, DATABASE_TABLE_GEOLOC, new String[] { COL_GEOLOCID, COL_ACC, COL_ALT, COL_BEAR
				, COL_LATE6, COL_LONE6, COL_SPD, COL_GEOLOCROUTEID, COL_TIME}, null, null, null, null, null, null);
		ArrayList<GeoLoc> res = new ArrayList<GeoLoc>();

		if (result.moveToFirst()) {
			Toast.makeText(context, "moveToFirst", Toast.LENGTH_SHORT).show();
			while (!result.isAfterLast()) {
				Toast.makeText(context, "isAfterLast", Toast.LENGTH_SHORT).show();
				if (result.getString(COL_GEOLOCROUTEID_NO).equals(((Long) _routeId).toString())) {
					Toast.makeText(context, "getGeoLoc", Toast.LENGTH_SHORT)
							.show();
					//String tmp = result.getString(COL_TIME_NO);
					long time = Long.valueOf("0");
					float acc = Float.valueOf(result.getString(COL_ACC_NO));
					float bear = Float.valueOf(result.getString(COL_BEAR_NO));
					float spd = Float.valueOf(result.getString(COL_SPD_NO));
					double alt = Double.valueOf(result.getString(COL_ALT_NO));
					double latE6 = Double.valueOf(result
							.getString(COL_LATE6_NO));
					double lonE6 = Double.valueOf(result
							.getString(COL_LONE6_NO));

					res.add(new GeoLoc(acc, alt, bear, latE6, lonE6, spd, time));
				}
				result.moveToNext();
			}
		}
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