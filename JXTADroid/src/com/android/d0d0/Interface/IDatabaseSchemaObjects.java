/**
 * 
 */
package com.android.d0d0.Interface;

import java.util.List;

import com.android.d0d0.database.schema.LocationStatusSchema;
import com.android.d0d0.database.schema.LocationDataSchema;
import com.android.d0d0.database.schema.StatusDataSchema;

/**
 * @author d0d0
 * This interface provides data objects based on the schema of the SQL tables
 * These objects can be either a single data row or a list of data objects like a table 
 * This interface ensures that I can create and control the database schema objects in one place
 * 
 */


public interface IDatabaseSchemaObjects {

	public LocationDataSchema getLocationDataRow();
	
	public List<LocationDataSchema> getLocationDataList();
	
	public LocationStatusSchema getLocationStatusRow();
	
	public List<LocationStatusSchema> getLocationStatusList();

	public StatusDataSchema getStatusDataRow();
	
	public List<StatusDataSchema> getStatusDataList();

	
}
