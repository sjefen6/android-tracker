package org.goldclone.android.tracker;

import java.util.ArrayList;

public class Route {
	private String name;
	private ArrayList<GeoLoc> locationArray;
	
	public Route(String name){
		this.name = name;
		this.locationArray = new ArrayList<GeoLoc>();
	}
	
	public Route(String name, ArrayList<GeoLoc> locationArray){
		this.name = name;
		this.locationArray = locationArray;
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
