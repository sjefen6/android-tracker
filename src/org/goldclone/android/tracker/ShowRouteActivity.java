package org.goldclone.android.tracker;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ShowRouteActivity extends Activity {
	private RouteDBAdapter db;
	private ArrayList<String> rutes;
	private ArrayList<String[]> routesStringArray;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_route);

		ListView routesView = (ListView) findViewById(R.id.routes);

		db = new RouteDBAdapter(this);
		db.open();

		routesStringArray = db.getAllRoutes();
		rutes = new ArrayList<String>();

		if (routesStringArray.isEmpty()) {
			Toast.makeText(getBaseContext(), "No routes found!", Toast.LENGTH_SHORT)
					.show();
			finish();
		}

		for (String[] s : routesStringArray) {
			rutes.add(s[1]);
		}

		routesView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, rutes));

		routesView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String r = rutes.get(position);
				for (String[] rsa : routesStringArray) {
					if (r.equals(rsa[0])) {
						Intent returnIntent = getIntent();
						returnIntent.putExtra("routeId", rsa[1]);
						setResult(1, returnIntent);
						finish();
					}
				}
				Toast.makeText(getBaseContext(), "Code gone to far!",
						Toast.LENGTH_SHORT).show();
			}
		});
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
