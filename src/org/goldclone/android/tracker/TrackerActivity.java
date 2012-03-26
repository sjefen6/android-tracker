package org.goldclone.android.tracker;

import java.util.Date;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TrackerActivity extends MapActivity {
	public static long T = 5000; // milliseconds
	public static float DISTANCE = 5.0f; // meters
	
	public Route currentRoute;
	private RouteDBAdapter db;
	private Boolean tracking;
	private Drawable drawable;
	private locListener currentLocListener;
	private LocationManager locationManager;
	private List<Overlay> mapOverlays;
	private MapView mapView;
	private String providerName;
	private MyItemizedOverlay ItemizedOverlay;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		providerName = LocationManager.GPS_PROVIDER;
		currentLocListener = new locListener() ;
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapOverlays = mapView.getOverlays();
		
		drawable = this.getResources().getDrawable(R.drawable.dot);
		ItemizedOverlay = new MyItemizedOverlay(drawable);
		
		locationManager.requestLocationUpdates(providerName, T, DISTANCE, currentLocListener);
		
		tracking = false;
		
		db = new RouteDBAdapter(this);
		db.open();

		final Button new_route = (Button) findViewById(R.id.new_route);
		final Button view_route = (Button) findViewById(R.id.view_route);
		
		new_route.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!tracking) {
					currentRoute = new Route(new Date().toString());
					new_route.setText(R.string.stop_route);
					tracking = true;
				} else {
					tracking = false;
					new_route.setText(R.string.new_route);
					db.insertEntry(currentRoute);
				}
			}
		});
		
		view_route.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
                intent.setComponent(new ComponentName("org.goldclone.android.tracker", "org.goldclone.android.tracker.ShowRouteActivity"));
                startActivityForResult(intent, 1);
			}
		});
	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class locListener implements LocationListener{
		public void onLocationChanged(Location location) {
        	Toast.makeText(getBaseContext(), "yup", Toast.LENGTH_SHORT).show();
			if(tracking){
				// tegner et ikon når posisjon endres.
				GeoLoc pos = new GeoLoc(location);
				GeoPoint point = pos.getGeoPoint();
				OverlayItem oi = new OverlayItem(point, "", "");
				
				ItemizedOverlay.addOverlay(oi);
				
				mapOverlays.add(ItemizedOverlay);

				currentRoute.addLocation(location);
			}
		}
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == resultCode){
        	db.open();
        	Toast.makeText(getBaseContext(), "\"" + Long.valueOf(data.getStringExtra("routeId")).toString() + "\"", Toast.LENGTH_SHORT)
			.show();
        	currentRoute = db.getEntry(Long.valueOf(data.getStringExtra("routeId")));
        	
        	mapOverlays = mapView.getOverlays();
        	ItemizedOverlay = new MyItemizedOverlay(drawable);
        	
        	for (GeoLoc g: currentRoute.getArray()){
				GeoPoint point = g.getGeoPoint();
				OverlayItem oi = new OverlayItem(point, "", "");
				
				Toast.makeText(getBaseContext(), "lol", Toast.LENGTH_SHORT)
				.show();
				
				ItemizedOverlay.addOverlay(oi);
				
				mapOverlays.add(ItemizedOverlay);
        	}
        }        	
    }
    
	public void onResume() {
		super.onResume();
		try {
			db.open();
		} catch (SQLException e) {
			Toast.makeText(getBaseContext(), "db error", Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}

	public void onPause() {
		super.onPause();
		try {
			db.close();
		} catch (SQLException e) {
			Toast.makeText(getBaseContext(), "db error", Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}
}