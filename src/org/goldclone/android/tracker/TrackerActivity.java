package org.goldclone.android.tracker;

import java.util.Date;
import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TrackerActivity extends MapActivity /* extends Activity */ {
	
	public Route currentRoute;
	private RouteDBAdapter db;
	private Boolean tracking;
	private locListener currentLocListener;
	private Projection projection;
	private List<Overlay> mapOverlays;
	private MyLocationOverlay whereAmI;
	private MapView mapView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		tracking = false;
		db = new RouteDBAdapter(this);
		db.open();
		
		final Button new_route = (Button) findViewById(R.id.new_route);
		final Button view_route = (Button) findViewById(R.id.view_route);
		
		new_route.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!tracking) {
					currentRoute = new Route(new Date().toString());
					currentLocListener = new locListener(currentRoute, getBaseContext());
					new_route.setText(R.string.stop_route);
					tracking = true;
					projection = mapView.getProjection();
					whereAmI = new MyLocationOverlay(getParent(), mapView);
					
					mapOverlays.add(whereAmI);
			        mapOverlays.add(new RouteOverlay(getParent(), mapView));
				} else {
					currentLocListener.stop();
					new_route.setText(R.string.new_route);
					tracking = false;
				}
			}
		});
		
		view_route.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});
	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	class RouteOverlay extends Overlay {
	  	Context context;
    	MapView mapview;
    	
    	public RouteOverlay(Context _context, MapView _mapView) {
    		this.context = _context;
    		this.mapview = _mapView;
    	}
		
		public void draw(Canvas canvas, MapView mapv, boolean shadow) {
			super.draw(canvas, mapv, shadow);

			Path path = new Path();
    		
    		Paint mPaint = new Paint();
    		mPaint.setDither(true);
            mPaint.setColor(Color.RED);
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(2);

			for (int i = 0; i < currentRoute.getArray().size(); i++) {
				Point from = new Point();
				Point to = new Point();

				projection.toPixels(currentRoute.getArray().get(i)
						.getGeoPoint(), from);
				projection.toPixels(currentRoute.getArray().get(i + 1)
						.getGeoPoint(), to);

				path.moveTo(from.x, from.y);
				path.lineTo(to.x, to.y);
			}
			
			canvas.drawPath(path, mPaint);
		}
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == resultCode){
        	Toast.makeText(getBaseContext(), "yup", Toast.LENGTH_SHORT)
			.show();
        	currentRoute = db.getEntry(Long.valueOf(data.getStringExtra("routeId")));
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