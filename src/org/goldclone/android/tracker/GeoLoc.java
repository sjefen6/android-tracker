package org.goldclone.android.tracker;

import android.location.Location;

public class GeoLoc {
	private Location location;
	private long time;
	
	public GeoLoc(Location location){
//		this.location = new Location()
		this.location = location;
		this.time = System.currentTimeMillis();
	}
	
	public long getTime(){
		return time;
	}
	
	public Location getLocation(){
		return location;
	}

}
