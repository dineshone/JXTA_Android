package com.android.d0d0;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.logging.Logger;

import net.jxse.JxseInstantiator;
import net.jxta.logging.Logging;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.android.d0d0.R.id;
import com.android.d0d0.Interface.IDatabaseSchemaObjects;
import com.android.d0d0.Interface.Id0d0Database;
import com.android.d0d0.database.d0d0DataProvider;
import com.android.d0d0.database.d0d0DatabaseSchemaObjectProvider;
import com.android.d0d0.database.schema.LocationDataSchema;
import com.android.d0d0.database.schema.LocationStatusSchema;
import com.android.d0d0.database.schema.StatusDataSchema;
import com.android.d0d0.utils.OnSwipeTouchListener;
import com.android.d0d0.utils.serializableEditText;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements
		OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener,
		LocationListener, OnMarkerClickListener {

	private static final String TAG = "MapActivity";
	private GoogleMap resqMap;
	private TextView mTapTextView;
	private TextView mCameraTextView;
	private final Random mRandom = new Random();
	public static Context context = ApplicationClass
			.getApplicationClassContext();
	Context thisContext = this;

	private final static Logger LOG = Logger.getLogger(MapActivity.class
			.getName());

	SpannableStringBuilder statusSSB = new SpannableStringBuilder("");

	String statusDrawable;

	int start;

	public Marker currMarker;

	SpannableStringBuilder mapSSB = new SpannableStringBuilder("");
	EditText info_window_title = new EditText(context);

	public double latitude;
	public double longitude;

	public int radiusInMiles = 50;

	protected Id0d0Database d0d0DataTables = new d0d0DataProvider();

	protected IDatabaseSchemaObjects d0d0DataObjects = new d0d0DatabaseSchemaObjectProvider();

	List<LocationDataSchema> MarkerLocations = d0d0DataObjects
			.getLocationDataList();
	LocationDataSchema MarkerLocationRecord = d0d0DataObjects
			.getLocationDataRow();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		final View contentView = findViewById(R.id.linearLayout1);
		contentView.setOnTouchListener(new OnSwipeTouchListener() {
			public void onSwipeTop() {
			}

			public void onSwipeBottom() {
			}

			public void onSwipeLeft() {

			}

			public void onSwipeRight() {
				startReportGridActivity();
			}
		});

		LocationManager myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		getLocationUpdates(myLocationManager);

		showMap(latitude, longitude);

	}

	@Override
	protected void onResume() {
		super.onResume();

		showMap(latitude, longitude);
	}

	public void showMap(double latitude2, double longitude2) {

		if (resqMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			resqMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.d0d0MapFragment1)).getMap();
			// Check if we were successful in obtaining the map.
			if (resqMap != null) {

				resqMap.setMyLocationEnabled(true);

				// map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,
				// 13));

				LatLng myLoc = new LatLng(latitude2, longitude2);

				resqMap.addMarker(new MarkerOptions().title("You are Here")
						.snippet("This location currently has no events")
						.position(myLoc));

				MarkerLocations = d0d0DataTables.getLocations();

				if (!MarkerLocations.isEmpty()) {

					ListIterator<LocationDataSchema> MarkerLocationsIterator = MarkerLocations
							.listIterator();

					while (MarkerLocationsIterator.hasNext()) {

						MarkerLocationRecord = MarkerLocationsIterator.next();

						resqMap.addMarker(new MarkerOptions()
								.title("You are Here")
								.snippet(
										"This location currently has no events")
								.position(
										new LatLng(
												MarkerLocationRecord.Loc_Latitude,
												MarkerLocationRecord.Loc_Longitude)));
					}

				}

				resqMap.setOnMapClickListener(this);
				resqMap.setOnMapLongClickListener(this);
				resqMap.setOnCameraChangeListener(this);
				resqMap.setOnMarkerClickListener(this);
				resqMap.setInfoWindowAdapter(new InfoWindowAdapter() {

					@Override
					public View getInfoContents(Marker marker) {
						// TODO Auto-generated method stub

						View customInfoWindowView = getLayoutInflater()
								.inflate(R.layout.custom_info_window_layout,
										null);

						LatLng latLng = marker.getPosition();

						EditText info_window_editText = (EditText) customInfoWindowView
								.findViewById(id.info_window_edit_text);

						info_window_editText = info_window_title;

						return customInfoWindowView;
					}

					@Override
					public View getInfoWindow(Marker marker) {
						// TODO Auto-generated method stub
						return null;
					}

				}

				);

			}
		}
	}

	@Override
	public void onCameraChange(CameraPosition position) {
		// TODO Auto-generated method stub
		// mCameraTextView.setText(position.toString());
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		// mTapTextView.setText("long pressed, point=" + point);

		d0d0DataTables.createLocation(point);

		currMarker = resqMap
				.addMarker(new MarkerOptions()
						.icon(BitmapDescriptorFactory.defaultMarker(mRandom
								.nextFloat() * 360)).anchor(0.0f, 1.0f) // Anchors
																		// the
																		// marker
																		// on
																		// the
																		// bottom
																		// left
						.position(point));

		startKeyBoardActivity(point);

	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		// mTapTextView.setText("short pressed, point=" + point);
		Logging.logCheckedInfo(LOG, "Map has been clicked");

	}

	public void startKeyBoardActivity(LatLng location) {
		double[] coordinates = { location.latitude, location.longitude };
		Intent startKeyBoardIntent = new Intent(context, KeyBoardActivity.class);
		startKeyBoardIntent.putExtra("coordinates", coordinates);
		startActivity(startKeyBoardIntent);

		// startActivityForResult(startKeyBoardIntent, 2);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// check if the request code is same as what is passed here it is 2
		if (requestCode == 2) {

			Bundle bundle = data.getExtras();
			// mapSSB = (SpannableStringBuilder)
			// bundle.getSerializable("mySerializableEditText");

			info_window_title = (EditText) bundle
					.getSerializable("mySerializableEditText");

			// String message=data.getStringExtra("MESSAGE");
			// String[] empty_array = new String[message.length()+1];
			//
			// mapSSB.append(String.valueOf(empty_array));
			// mapSSB.setSpan(message, 0, message.length(), 0);
			//
			//
			//
			// info_window_title.setText(mapSSB, BufferType.SPANNABLE);

			// currMarker.setTitle(info_window_title);
			// currMarker.setSnippet(mapSSB.toString());

			currMarker.showInfoWindow();

		}

	}

	private void getLocationUpdates(LocationManager myLocationManager) {
		// TODO Auto-generated method stub
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);

		List<String> enabledProviders = myLocationManager.getProviders(
				criteria, true);

		if (!enabledProviders.isEmpty()) {

			for (String enabledProvider : enabledProviders) {
				myLocationManager.requestSingleUpdate(enabledProvider, this,
						null);
				continue;
			}

		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		latitude = location.getLatitude();
		longitude = location.getLongitude();

		LatLng curLatLon = new LatLng(location.getLatitude(),
				location.getLongitude());

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void startReportGridActivity() {
		Intent startReportGridIntent = new Intent(context,
				ReportGridActivity.class);
		startActivity(startReportGridIntent);

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub

		LayoutInflater li_markerClick = (LayoutInflater) thisContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View mapPopUpView = li_markerClick.inflate(R.layout.activity_map_popup,
				(ViewGroup) findViewById(R.layout.activity_map));

		// setContentView(R.layout.activity_map_popup);

		// EditText editTextPopUp = (EditText) findViewById(R.id.editTextPopUp);

		final PopupWindow mapPopUpWindow = new PopupWindow(mapPopUpView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		EditText editTextPopUp = (EditText) mapPopUpView
				.findViewById(R.id.editTextPopUp);

		fillpopUpEditTextwithIncidents(editTextPopUp, marker);

		Button btnClose = (Button) mapPopUpView.findViewById(R.id.btn_close);
		btnClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapPopUpWindow.dismiss();
			}
		});

		Button btnAdd = (Button) mapPopUpView.findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityRecordMessage();

			}

		});

		/*
		 * Looks like Android wont get touch events if the popUp window is
		 * focussable if I create the popUp Window with the focussable parameter
		 * set to false, I am able to Receive touch events through the
		 * background activity which is the MapActivity
		 */
		/*
		 * mapPopUpWindow.setOutsideTouchable(true);
		 * mapPopUpWindow.setTouchable(true);
		 * 
		 * 
		 * mapPopUpWindow.setTouchInterceptor(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub if (event.getAction() ==
		 * MotionEvent.ACTION_OUTSIDE) { mapPopUpWindow.dismiss();
		 * 
		 * return true; }
		 * 
		 * return false; } });
		 */

		int popUPdrawableID = context.getResources().getIdentifier(
				"callout_border", "drawable", context.getPackageName());

		// mapPopUpWindow.setBackgroundDrawable(context.getResources().getDrawable(popUPdrawableID));

		// mapPopUpWindow.showAsDropDown(mapPopUpView);
		mapPopUpWindow.showAtLocation(mapPopUpView, Gravity.CENTER, 0, 0);

		return true;
	}

	/**
	 * @param popUpEditText
	 */
	public void fillpopUpEditTextwithIncidents(EditText popUpEditText,
			Marker marker) {

		LatLng markerLocation = marker.getPosition();
		start = popUpEditText.length();
		statusSSB.clearSpans();

		List<LocationStatusSchema> incidentsAtLocationList = d0d0DataTables
				.getIncidents(markerLocation);

		if (!incidentsAtLocationList.isEmpty()) {

			Logging.logCheckedInfo(LOG, incidentsAtLocationList.toString());

			Iterator<LocationStatusSchema> incidentsAtLocationListIterator = incidentsAtLocationList
					.listIterator();

			do {

				LocationStatusSchema incidentAtLocationRecord = d0d0DataObjects
						.getLocationStatusRow();
				incidentAtLocationRecord = (LocationStatusSchema) incidentsAtLocationListIterator
						.next();

				StatusDataSchema statusDataRecord = d0d0DataObjects
						.getStatusDataRow();
				statusDataRecord = d0d0DataTables
						.getStatusData(incidentAtLocationRecord.STATUS_CODE);

				Logging.logCheckedInfo(LOG, statusDataRecord.toString());

				// statusDrawable = "R.drawable."
				// + statusDataRecord.Status_FilePath.toString();

				statusDrawable = statusDataRecord.Status_FilePath.toString();

				int drawableID = context.getResources().getIdentifier(
						statusDrawable, "drawable", context.getPackageName());
				Bitmap statusBitmap = BitmapFactory.decodeResource(
						getResources(), drawableID);

				ImageSpan iconSpan = new ImageSpan(context, statusBitmap);

				statusSSB.append(" ");

				statusSSB.setSpan(iconSpan, start, start + 1,
						Spannable.SPAN_INCLUSIVE_INCLUSIVE);

				popUpEditText.setText(statusSSB, BufferType.SPANNABLE);

				/*
				 * d0d0 If there are more than 5 statuses, the edit text should
				 * start a new line and expand to show all the statuses. it does
				 * not. I was trying to start a newline after every 5 statuses.
				 * But this just did not show the status. if (start % 5 == 0) {
				 * popUpEditText.setText("\n"); }
				 */

				start++;

			} while (incidentsAtLocationListIterator.hasNext());

		}
	}

	/**
	 * 
	 */
	public void startActivityRecordMessage() {
		Intent startRecordMessageIntent = new Intent(context,
				ActivityRecordMessage.class);
		startActivity(startRecordMessageIntent);
	}
}
