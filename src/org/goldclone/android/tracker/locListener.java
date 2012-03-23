package org.goldclone.android.tracker;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class locListener implements LocationListener {
	public static long T = 5000; // milliseconds
	public static float DISTANCE = 5.0f; // meters
	
	private LocationManager locationManager;
	private String providerName;
	Route newRoute;
	
	public locListener(Route newRoute){
		locationManager.requestLocationUpdates(providerName, T, DISTANCE, this);
		this.newRoute = newRoute;
	}
	
	public void onLocationChanged(Location location) {
		newRoute.addLocation(location);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
