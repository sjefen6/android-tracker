package org.goldclone.android.tracker;

import com.google.android.maps.GeoPoint;

import android.location.Location;

public class GeoLoc {
	//private Location location;
	private long time;
	private float acc, bear, spd;
	private double alt, latE6, lonE6;
	
	public GeoLoc(Location location){
		this.acc = location.getAccuracy();
		this.alt = location.getAltitude();
		this.bear = location.getBearing();
		this.latE6 = location.getLatitude() / 1E6;
		this.lonE6 = location.getLongitude() / 1E6;
		this.spd = location.getSpeed();
		this.time = location.getTime();
	}
	
	public long getTime(){
		return time;
	}
	
	public GeoPoint getGeoPoint(){
		return new GeoPoint((int) latE6, (int) lonE6);
	}

}
