/**
 * 
 */
package com.android.d0d0.Interface;


import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import com.android.d0d0.database.schema.LocationDataSchema;
import com.android.d0d0.database.schema.LocationStatusSchema;
import com.android.d0d0.database.schema.StatusDataSchema;


/**
 * 
 * @author d0d0
 * This is the interface to get data from database tables using sql
 * This interface provides methods to create, edit and delete location data
 * This interface provides methods to create, edit and delete Incident data at the given location
 * 	
 */


public interface Id0d0Database {
	
	/*
	 * This method gets all the records in the LocationData table We need to be
	 * careful while fetching all the records
	 */
	
	List<LocationDataSchema> getLocations();
		
	/*
	 * This method gets the location thats been specified by the currlocation
	 */
	LocationDataSchema getLocations(LatLng currlocation);
	
	
	/*
	 * This method gets the location thats been specified by the currlocation
	 * and within a radius specified in miles.
	 * 
	 */
	List<LocationDataSchema> getLocations(LatLng currlocation, int radiusInMiles);
	
	
	
	void createLocation(LatLng location);
	
	void editLocation(LatLng location);
	
	void deleteLocation(LatLng location);
	
	/*
	 * This method gets all the incodents that have been logged at the given location
	 * Sometimes it makes more sense to get all the incidents based on the location
	 * Sometimes it makes sense to retrieve all the locations which has a certain status code
	 * Like when we want to know all the locations which has a tree down.     
	 * 
	 */
	List<LocationStatusSchema> getIncidents(LatLng location);
	
	List<LocationStatusSchema> getIncidents(Integer statusCode);
	
	StatusDataSchema getStatusData(Integer statusCode);
	
	void createIncident(LatLng location, Integer statusCode);
	
	void editIncident(LatLng location, Integer statusCode);
	
	void deleteIncident(LatLng location, Integer statusCode);
	
	/*
	 * At a given location, delete all the incidents
	 * 
	 */
	void deleteIncidents(LatLng location);
	
}
