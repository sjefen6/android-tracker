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
		this.latE6 = location.getLatitude() * 1E6;
		this.lonE6 = location.getLongitude() * 1E6;
		this.spd = location.getSpeed();
		this.time = location.getTime();
	}
	
	public GeoLoc(float acc, double alt, float bear, double latE6, double lonE6, float spd, long time){
		this.acc = acc;
		this.alt = alt;
		this.bear = bear;
		this.latE6 = latE6;
		this.lonE6 = lonE6;
		this.spd = spd;
		this.time = time;
	}
	
	public GeoPoint getGeoPoint(){
		return new GeoPoint((int) latE6, (int) lonE6);
	}

	public float getAcc() {
		return acc;
	}

	public float getBear() {
		return bear;
	}

	public float getSpd() {
		return spd;
	}

	public double getAlt() {
		return alt;
	}

	public double getLatE6() {
		return latE6;
	}

	public double getLonE6() {
		return lonE6;
	}
	
	public long getTime(){
		return time;
	}
}
