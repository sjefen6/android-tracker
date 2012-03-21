package org.goldclone.android.tracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class locListener implements LocationListener {

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		double lat = location.getLatitude();
		double lng = location.getLongitude();
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
