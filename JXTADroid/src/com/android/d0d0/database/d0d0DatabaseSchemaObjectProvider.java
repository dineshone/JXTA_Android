/**
 * 
 */
package com.android.d0d0.database;

import java.util.ArrayList;
import java.util.List;

import com.android.d0d0.Interface.IDatabaseSchemaObjects;

import com.android.d0d0.database.schema.LocationDataSchema;
import com.android.d0d0.database.schema.LocationStatusSchema;
import com.android.d0d0.database.schema.StatusDataSchema;


/**
 * @author d0d0
 * This class provides database objects like tables and rows based on the database schema
 * I am creating this class, so that I can better organize the schema of the database tables in one place
 * I might make some mistakes if I were to declare a custom data row or a data list based on a database table
 * In the future I will ensure that that tables that have been created in the SQLite database get the schema from this class.
 * For now this class provides methods to create data record and data list/    
 *
 */

public class d0d0DatabaseSchemaObjectProvider implements IDatabaseSchemaObjects {

	/**
	 * 
	 */
	
	
	
	public d0d0DatabaseSchemaObjectProvider() {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public LocationStatusSchema getLocationStatusRow() {
		// TODO Auto-generated method stub
		
		LocationStatusSchema LocStatusDataRecord = new LocationStatusSchema();
		return LocStatusDataRecord;
		
	}

	
	@Override
	public List<LocationStatusSchema> getLocationStatusList() {
		// TODO Auto-generated method stub
		
		List<LocationStatusSchema> LocStatusDataList = new ArrayList<LocationStatusSchema>();
		return LocStatusDataList;
	}

	@Override
	public StatusDataSchema getStatusDataRow() {
		// TODO Auto-generated method stub
		
		StatusDataSchema StatusDataRecord = new StatusDataSchema();
		return StatusDataRecord;
	}

	
	@Override
	public List<StatusDataSchema> getStatusDataList() {
		// TODO Auto-generated method stub
		
		List<StatusDataSchema> StatusDataList = new ArrayList<StatusDataSchema>();
		return StatusDataList;
	}

	@Override
	public LocationDataSchema getLocationDataRow() {
		// TODO Auto-generated method stub
		
		LocationDataSchema LocDataRecord = new LocationDataSchema();
		return LocDataRecord;
	}

	@Override
	public List<LocationDataSchema> getLocationDataList() {
		// TODO Auto-generated method stub
		
		List<LocationDataSchema> LocDataList = new ArrayList<LocationDataSchema>();
		return LocDataList;
	}

}
