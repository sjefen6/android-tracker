package org.goldclone.android.tracker;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class TrackerActivity extends MapActivity/* extends Activity */{

	private LocationManager locationManager;
	private String providerName;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		
		long t = 5000; // milliseconds
		float distance = 5.0f; // meters

		LocationListener myLocationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Kj¿res nŒr lokasjon enten tiden (5s) l¿per ut eller
				// forflytningen > 5m
				double lat = location.getLatitude();
				double lng = location.getLongitude();

			}

			public void onProviderDisabled(String provider) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}
		};
		locationManager.requestLocationUpdates(providerName, t, distance,
				myLocationListener);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}