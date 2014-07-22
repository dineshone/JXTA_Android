package com.android.d0d0.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.d0d0.ApplicationClass;
import com.android.d0d0.Interface.IDatabaseSchemaObjects;
import com.android.d0d0.Interface.Id0d0Database;
import com.android.d0d0.database.schema.LocationDataSchema;
import com.android.d0d0.database.schema.LocationStatusSchema;
import com.android.d0d0.database.schema.StatusDataSchema;
import com.google.android.gms.maps.model.LatLng;

public class d0d0DataProvider implements Id0d0Database {

	public static Context context = ApplicationClass
			.getApplicationClassContext();

	private d0d0Database d0d0Data = new d0d0Database(context);

	protected IDatabaseSchemaObjects d0d0DataObjects = new d0d0DatabaseSchemaObjectProvider();

	private SQLiteDatabase sqdb = d0d0Data.getWritableDatabase();

	private double latitude;
	private double longitude;

	public d0d0DataProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createLocation(LatLng location) {
		// TODO Auto-generated method stub

		latitude = location.latitude ;
		longitude = location.longitude;

		sqdb.execSQL("INSERT INTO LocationData (Loc_Latitude, Loc_Longitude)"
				+ " Select '" + latitude + "' AS Loc_Latitude" + ",'"
				+ longitude + "' AS Loc_Longitude ");
		
		

	}

	/*
	 * This method gets all the records in the LocationData table We need to be
	 * careful while fetching all the records
	 */

	public List<LocationDataSchema> getLocations() {

		List<LocationDataSchema> locList = d0d0DataObjects
				.getLocationDataList();
		// d0d0DataObjects.getLocationDataList();
		// LocationDataSchema myLocRecord =
		// d0d0DataObjects.getLocationDataRow();

		Cursor SelectQueryCursor = sqdb.rawQuery(
				"SELECT * FROM LocationData ORDER BY Loc_ID", null);

		if (SelectQueryCursor.getCount() > 0) {
			SelectQueryCursor.moveToFirst();

			do {
				/*
				 * d0d0 I am not sure of this ... but do check this later
				 * Android is supposed to use the integer version of LatLng
				 * values. Hence the values of Latitude and Longitude should be
				 * multiplied by 1E6... Check if this is necessary. if this is
				 * necessary, store the latitude and longitude values as integer
				 * in the database tables and then use
				 * SelectQueryCursor.getInteger instead of
				 * SelectQueryCorsor.getDouble
				 */

				/*
				 * The reason why I am initiating a new LocationDataSchema()
				 * here is that during every iteration of the cursor, I am
				 * assigning the table row data to the object myLocRecord. This
				 * object then is added to the ArrayList. However, since I am
				 * adding the same object during each iteration of the do while
				 * loop, it ends up overwriting the object again and again. This
				 * happens because the object is the same and so, each iteration
				 * it ends up overwriting the same record So the solution is to
				 * reinitialize the object using new() so that
				 */
				// LocationDataSchema myLocRecord = new LocationDataSchema();

				LocationDataSchema myLocRecord = d0d0DataObjects
						.getLocationDataRow();

				myLocRecord.Loc_ID = SelectQueryCursor.getInt(SelectQueryCursor
						.getColumnIndex("Loc_ID"));
				myLocRecord.Loc_Latitude = SelectQueryCursor
						.getDouble(SelectQueryCursor
								.getColumnIndex("Loc_Latitude"));
				myLocRecord.Loc_Longitude = SelectQueryCursor
						.getDouble(SelectQueryCursor
								.getColumnIndex("Loc_Longitude"));
				myLocRecord.Loc_Advertisement = SelectQueryCursor
						.getString(SelectQueryCursor
								.getColumnIndex("Loc_Advertisement"));
				myLocRecord.Loc_ContentShareAdvertisement = SelectQueryCursor
						.getString(SelectQueryCursor
								.getColumnIndex("Loc_ContentShareAdvertisement"));

				locList.add(myLocRecord);

				// myLocRecord.Loc_ID = null;

			} while (SelectQueryCursor.moveToNext());

		}

		return locList;
		// TODO Auto-generated method stub

	}

	/*
	 * This method gets the location thats been specified by the currlocation
	 */

	@Override
	public LocationDataSchema getLocations(LatLng currlocation) {
		// TODO Auto-generated method stub

		Cursor SelectQueryCursor = sqdb.rawQuery("SELECT * FROM LocationData "
				+ "where Loc_Latitude ='" + currlocation.latitude
				+ "' and Loc_Longitude = '" + currlocation.longitude + "'"
				+ "ORDER BY Loc_ID", null);

		SelectQueryCursor.moveToFirst();

		/*
		 * d0d0 I am not sure of this ... but do check this later Android is
		 * supposed to use the integer version of LatLng values. Hence the
		 * values of Latitude and Longitude should be multiplied by 1E6... Check
		 * if this is necessary. if this is necessary, store the latitude and
		 * longitude values as integer in the database tables and then use
		 * SelectQueryCursor.getInteger instead of SelectQueryCorsor.getDouble
		 */

		LocationDataSchema myLocRecord = d0d0DataObjects.getLocationDataRow();

		myLocRecord.Loc_ID = SelectQueryCursor.getInt(SelectQueryCursor
				.getColumnIndex("Loc_ID"));
		myLocRecord.Loc_Latitude = SelectQueryCursor
				.getDouble(SelectQueryCursor.getColumnIndex("Loc_Latitude"));
		myLocRecord.Loc_Longitude = SelectQueryCursor
				.getDouble(SelectQueryCursor.getColumnIndex("Loc_Longitude"));
		myLocRecord.Loc_Advertisement = SelectQueryCursor
				.getString(SelectQueryCursor
						.getColumnIndex("Loc_Advertisement"));
		myLocRecord.Loc_ContentShareAdvertisement = SelectQueryCursor
				.getString(SelectQueryCursor
						.getColumnIndex("Loc_ContentShareAdvertisement"));

		return myLocRecord;
		// TODO Auto-generated method stub

	}

	/*
	 * This method gets the location thats been specified by the currlocation
	 * and within a radius specified in miles. I havent implemented the formula
	 * to calculate the distances between two LatLngs in miles The formula is in
	 * my head right now .... will implement it soon.
	 */

	@Override
	public List<LocationDataSchema> getLocations(LatLng currlocation,
			int radiusInMiles) {

		List<LocationDataSchema> locList = d0d0DataObjects
				.getLocationDataList();

		Cursor SelectQueryCursor = sqdb.rawQuery("SELECT * FROM LocationData "
				+ "where Loc_Latitude ='" + currlocation.latitude
				+ "' and Loc_Longitude = '" + currlocation.longitude + "'"
				+ "ORDER BY Loc_ID", null);

		if (SelectQueryCursor.getCount() > 0) {
			SelectQueryCursor.moveToFirst();

			do {
				/*
				 * d0d0 I am not sure of this ... but do check this later
				 * Android is supposed to use the integer version of LatLng
				 * values. Hence the values of Latitude and Longitude should be
				 * multiplied by 1E6... Check if this is necessary. if this is
				 * necessary, store the latitude and longitude values as integer
				 * in the database tables and then use
				 * SelectQueryCursor.getInteger instead of
				 * SelectQueryCorsor.getDouble
				 */

				LocationDataSchema myLocRecord = d0d0DataObjects
						.getLocationDataRow();

				myLocRecord.Loc_ID = SelectQueryCursor.getInt(SelectQueryCursor
						.getColumnIndex("Loc_ID"));
				myLocRecord.Loc_Latitude = SelectQueryCursor
						.getDouble(SelectQueryCursor
								.getColumnIndex("Loc_Latitude"));
				myLocRecord.Loc_Longitude = SelectQueryCursor
						.getDouble(SelectQueryCursor
								.getColumnIndex("Loc_Longitude"));
				myLocRecord.Loc_Advertisement = SelectQueryCursor
						.getString(SelectQueryCursor
								.getColumnIndex("Loc_Advertisement"));
				myLocRecord.Loc_ContentShareAdvertisement = SelectQueryCursor
						.getString(SelectQueryCursor
								.getColumnIndex("Loc_ContentShareAdvertisement"));

				locList.add(myLocRecord);

			} while (SelectQueryCursor.moveToNext());

		}

		return locList;
		// TODO Auto-generated method stub

	}

	@Override
	public void editLocation(LatLng location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteLocation(LatLng location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createIncident(LatLng location, Integer statusCode) {
		// TODO Auto-generated method stub

		latitude = location.latitude;
		longitude = location.longitude;
		LocationDataSchema locationID = getLocations(location);
		StatusDataSchema statusData = getStatusData(statusCode);

		sqdb.execSQL("INSERT INTO LocationStatus (Loc_ID, Loc_Latitude, Loc_Longitude, STATUS_CODE, STATUS_DESC)"
				+ " Select '"
				+ locationID.Loc_ID
				+ "' AS Loc_ID, '"
				+ latitude
				+ "' AS Loc_Latitude, '"
				+ longitude
				+ "' AS Loc_Longitude, '"
				+ statusData.Status_Code
				+ "' AS STATUS_CODE, '"
				+ statusData.Status_Desc + "' AS STATUS_DESC ");

	}

	@Override
	public void editIncident(LatLng location, Integer statusCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteIncident(LatLng location, Integer statusCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public StatusDataSchema getStatusData(Integer statusCode) {
		// TODO Auto-generated method stub

		StatusDataSchema StatusDataRecord = d0d0DataObjects.getStatusDataRow();

		Cursor SelectQueryCursor = sqdb.rawQuery("SELECT * FROM StatusData "
				+ "where Status_Code ='" + statusCode + "'"
				+ "ORDER BY Status_ID", null);

		if (SelectQueryCursor.getCount() > 0) {
			SelectQueryCursor.moveToFirst();

			StatusDataRecord.Status_ID = SelectQueryCursor
					.getInt(SelectQueryCursor.getColumnIndex("Status_ID"));

			StatusDataRecord.Status_Code = SelectQueryCursor
					.getInt(SelectQueryCursor.getColumnIndex("Status_Code"));

			StatusDataRecord.Status_Desc = SelectQueryCursor
					.getString(SelectQueryCursor.getColumnIndex("Status_Desc"));

			StatusDataRecord.Status_FilePath = SelectQueryCursor
					.getString(SelectQueryCursor
							.getColumnIndex("Status_FilePath"));

		}
		return StatusDataRecord;

	}

	@Override
	public List<LocationStatusSchema> getIncidents(LatLng location) {
		// TODO Auto-generated method stub

		List<LocationStatusSchema> LocStatusList = d0d0DataObjects
				.getLocationStatusList();

		Cursor SelectQueryCursor = sqdb.rawQuery(
				"SELECT * FROM LocationStatus " + " WHERE Loc_Latitude ='"
						+ location.latitude + "' AND " + " Loc_Longitude ='"
						+ location.longitude + "' " + " ORDER BY LocStatus_ID",
				null);
		
//		
//		Cursor SelectQueryCursor = sqdb.rawQuery(
//				"SELECT * FROM LocationStatus " 
//						+ " ORDER BY LocStatus_ID",
//				null);

		if (SelectQueryCursor.getCount() > 0) {
			SelectQueryCursor.moveToFirst();

			do {
				LocationStatusSchema LocationStatusRecord = d0d0DataObjects
						.getLocationStatusRow();

				LocationStatusRecord.LocStatus_ID = SelectQueryCursor
						.getInt(SelectQueryCursor
								.getColumnIndex("LocStatus_ID"));

				LocationStatusRecord.Loc_ID = SelectQueryCursor
						.getInt(SelectQueryCursor.getColumnIndex("Loc_ID"));

				LocationStatusRecord.Loc_Latitude = SelectQueryCursor
						.getDouble(SelectQueryCursor
								.getColumnIndex("Loc_Latitude"));

				LocationStatusRecord.Loc_Longitude = SelectQueryCursor
						.getDouble(SelectQueryCursor
								.getColumnIndex("Loc_Longitude"));

				LocationStatusRecord.STATUS_CODE = SelectQueryCursor
						.getInt(SelectQueryCursor.getColumnIndex("STATUS_CODE"));

				LocationStatusRecord.STATUS_DESC = SelectQueryCursor
						.getString(SelectQueryCursor
								.getColumnIndex("STATUS_DESC"));

				LocStatusList.add(LocationStatusRecord);
			} while (SelectQueryCursor.moveToNext());
		}
		return LocStatusList;
	}

	@Override
	public List<LocationStatusSchema> getIncidents(Integer statusCode) {

		List<LocationStatusSchema> statusAtLocationsList = d0d0DataObjects
				.getLocationStatusList();

		Cursor SelectQueryCursor = sqdb.rawQuery(
				"SELECT * FROM LocationStatus " + " where STATUS_CODE ='"
						+ statusCode + "' " + " ORDER BY LocStatus_ID", null);

		if (SelectQueryCursor.getCount() > 0) {
			SelectQueryCursor.moveToFirst();

			do {
				LocationStatusSchema statusAtLocationRecord = d0d0DataObjects
						.getLocationStatusRow();

				statusAtLocationRecord.LocStatus_ID = SelectQueryCursor
						.getInt(SelectQueryCursor
								.getColumnIndex("LocStatus_ID"));

				statusAtLocationRecord.Loc_ID = SelectQueryCursor
						.getInt(SelectQueryCursor.getColumnIndex("Loc_ID"));

				statusAtLocationRecord.Loc_Latitude = SelectQueryCursor
						.getInt(SelectQueryCursor
								.getColumnIndex("Loc_Latitude"));

				statusAtLocationRecord.Loc_Longitude = SelectQueryCursor
						.getInt(SelectQueryCursor
								.getColumnIndex("Loc_Longitude"));

				statusAtLocationRecord.STATUS_CODE = SelectQueryCursor
						.getInt(SelectQueryCursor.getColumnIndex("STATUS_CODE"));

				statusAtLocationRecord.STATUS_DESC = SelectQueryCursor
						.getString(SelectQueryCursor
								.getColumnIndex("STATUS_DESC"));

				statusAtLocationsList.add(statusAtLocationRecord);
			} while (SelectQueryCursor.moveToNext());
		}
		return statusAtLocationsList;
	}

	@Override
	public void deleteIncidents(LatLng location) {
		// TODO Auto-generated method stub

		latitude = location.latitude;
		longitude = location.longitude;

		sqdb.execSQL("DELETE FROM LocationStatus "
				+ " where Loc_Latitude = '" + latitude + "' AND  "
				+ "Loc_Longitude = '" + longitude + "' ");

	}

}
