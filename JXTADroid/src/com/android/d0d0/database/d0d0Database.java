/**
 * 
 */
package com.android.d0d0.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author d0d0
 *
 */
public class d0d0Database extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "resquology.db";
    private static final int DATABASE_VERSION = 2;
   
	
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public d0d0Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public d0d0Database(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param errorHandler
	 */
	public d0d0Database(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// Table containing Location data		
		db.execSQL("CREATE TABLE LocationData (" +
					"Loc_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					"Loc_Latitude TEXT," +
					"Loc_Longitude TEXT, " +
					"Loc_Advertisement TEXT, " +
					"Loc_ContentShareAdvertisement TEXT " +
					");");
		
		// Table containing Status data
		db.execSQL("CREATE TABLE StatusData (" +
					"Status_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"Status_Code INTEGER, " +
					"Status_Desc TEXT, " +
					"Status_FilePath TEXT " +
					");");
		
		// Table containing Incident status at the loaction
		db.execSQL("CREATE TABLE LocationStatus (" +
					"LocStatus_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					"Loc_ID INTEGER, " +
					"Loc_Latitude TEXT, " +
					"Loc_Longitude TEXT," +
					"STATUS_CODE INTEGER, " +
					"STATUS_DESC TEXT" +
					");");
		
		
		db.execSQL("INSERT INTO StatusData (Status_Code, Status_Desc, Status_FilePath) " +
					"  SELECT  '1' AS Status_Code, "+ "'FirstAidRequired' AS Status_Desc, "+"'ic_firstaid' AS Status_FilePath " +  
					"UNION SELECT '2'," + "'SaveOurSouls'," +"'ic_sos' "+ 
					"UNION SELECT '3'," + "'OnePersonDead'," +"'ic_corpse' " +
					"UNION SELECT '4'," + "'OneFallenTree'," +"'ic_fallentree' "+
					"UNION SELECT '5'," + "'Avalanche'," +"'ic_avalanche' "+
					"UNION SELECT '6'," + "'EarthQuake'," +"'ic_earthquake' "+
					"UNION SELECT '7'," + "'ForestFire'," +"'ic_fire' "+
					"UNION SELECT '8'," + "'Flooding'," +"'ic_flooding' "+
					"UNION SELECT '9'," + "'HighWind'," +"'ic_highwind' "+
					"UNION SELECT '10'," + "'LandSlide'," +"'ic_landslide' "+
					"UNION SELECT '11'," + "'Tsunami'," +"'ic_tsunami' "+
					"UNION SELECT '12'," + "'Twister'," +"'ic_twister' "+
					"UNION SELECT '13'," + "'Volcano'," +"'ic_volcano' "+
					"UNION SELECT '14'," + "'FireHazard'," +"'ic_fire_hazard' "+
					"UNION SELECT '15'," + "'BioHazard'," +"'ic_bio_hazard_hazard' "+
					"UNION SELECT '16'," + "'RadiationHazard'," +"'ic_radiation_hazard' "+
					"UNION SELECT '17'," + "'ExplosionHazard'," +"'ic_explosion_hazard' " +
					"UNION SELECT '18'," + "'HighVoltageHazard'," +"'ic_highvoltage_hazard' ");
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		

	}

}
