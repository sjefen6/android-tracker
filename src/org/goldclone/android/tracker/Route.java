package org.goldclone.android.tracker;

import java.util.ArrayList;

public class Route {
	private String name;
	private ArrayList<GeoLoc> locationArray;
	
	public Route(){
		locationArray = new ArrayList<GeoLoc>();
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<GeoLoc> getArray(){
		return locationArray;
	}
	
	public void addLocation(GeoLoc location){
		locationArray.add(location);
	}
	

}
