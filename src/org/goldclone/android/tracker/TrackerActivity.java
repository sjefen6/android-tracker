package org.goldclone.android.tracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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
	private locListener currentLocListener;
	private MapController mc;
	private Projection projection;
	private List<Overlay> mapOverlays;
	private ArrayList<GeoPoint> overlayPoints;
	private locListener ll;
	private MapView mapView;
	private Location currentLocation;
	private String providerName;
	private LocationManager locationManager;
	private MyItemizedOverlay ItemizedOverlay;
	private Drawable drawable;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		providerName = LocationManager.GPS_PROVIDER;
		ll = new locListener() ;
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapOverlays = mapView.getOverlays();
//		projection = mapView.getProjection();
		
		mc = mapView.getController();
		
		drawable = this.getResources().getDrawable(R.drawable.ic_launcher); // bilde for tagging
		ItemizedOverlay = new MyItemizedOverlay(drawable);
		
		locationManager.requestLocationUpdates(providerName, T, DISTANCE, ll);
		
		tracking = false;
//		db = new RouteDBAdapter(this);
//		db.open();

		final Button new_route = (Button) findViewById(R.id.new_route);
		final Button view_route = (Button) findViewById(R.id.view_route);
		
		new_route.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!tracking) {
					currentRoute = new Route(new Date().toString());
					new_route.setText(R.string.stop_route);
					tracking = true;

//			        mapOverlays.add(new RouteOverlay(getParent(), mapView));
				} else {
//					currentLocListener.stop();
					new_route.setText(R.string.new_route);
					tracking = false;
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
	
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == resultCode){
//        	Toast.makeText(getBaseContext(), "yup", Toast.LENGTH_SHORT)
//			.show();
//        	currentRoute = db.getEntry(Long.valueOf(data.getStringExtra("routeId")));
//        }        	
//    }
//    
//	public void onResume() {
//		super.onResume();
//		try {
//			db.open();
//		} catch (SQLException e) {
//			Toast.makeText(getBaseContext(), "db error", Toast.LENGTH_SHORT)
//					.show();
//			finish();
//		}
//	}
//
//	public void onPause() {
//		super.onPause();
//		try {
//			db.close();
//		} catch (SQLException e) {
//			Toast.makeText(getBaseContext(), "db error", Toast.LENGTH_SHORT)
//					.show();
//			finish();
//		}
//	}
}