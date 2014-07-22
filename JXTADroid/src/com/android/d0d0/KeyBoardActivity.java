package com.android.d0d0;

/*
 * d0d0 This activity contains the Keyboard with keys that describe a status or an incident or an event at a location
 * This activity is started when a longMapClick event is triggered by the user in the map.
 * The intent used to start this activity also contains the LatLng of the location where the user performed a longClick
 * 
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.NoCopySpan;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView.BufferType;

import com.android.d0d0.Interface.IDatabaseSchemaObjects;
import com.android.d0d0.Interface.Id0d0Database;
import com.android.d0d0.database.d0d0DataProvider;
import com.android.d0d0.database.d0d0DatabaseSchemaObjectProvider;
import com.android.d0d0.database.schema.LocationDataSchema;
import com.android.d0d0.database.schema.LocationStatusSchema;
import com.android.d0d0.database.schema.StatusDataSchema;

import com.android.d0d0.utils.OnSwipeTouchListener;
import com.android.d0d0.utils.serializableEditText;
import com.android.internal.util.*;
import com.google.android.gms.maps.model.LatLng;

public class KeyBoardActivity extends Activity {

	public static Context context = ApplicationClass
			.getApplicationClassContext();

	public Id0d0Database d0d0DataTables = new d0d0DataProvider();

	protected IDatabaseSchemaObjects d0d0DataObjects = new d0d0DatabaseSchemaObjectProvider();

	List<LocationStatusSchema> locationStatusList = d0d0DataObjects
			.getLocationStatusList();

	protected double latitude;
	protected double longitude;
	protected int locID;

	private int start;
	SpannableStringBuilder ssb = new SpannableStringBuilder("");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * d0d0 here is the intent I am recieving from the MapActivity that
		 * started this Activity
		 */

		Intent recievedIntent = getIntent();
		double locationCoordinates[] = recievedIntent
				.getDoubleArrayExtra("coordinates");
		latitude = locationCoordinates[0];
		longitude = locationCoordinates[1];

		LocationDataSchema locationData = d0d0DataTables
				.getLocations(new LatLng(latitude, longitude));
		locID = locationData.Loc_ID;

		setContentView(R.layout.activity_key_board);

		Keyboard mKeyboard = new Keyboard(context, R.xml.hazardkeyboard);

		KeyboardView mKeyboardView = (KeyboardView) findViewById(R.id.keyboardview);

		mKeyboardView.setKeyboard(mKeyboard);

		mKeyboardView.setPreviewEnabled(false);

		mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);

		// mKeyboardView.setOnTouchListener(new OnSwipeTouchListener(){
		// public void onSwipeTop() {}
		//
		// public void onSwipeBottom() {}
		//
		// public void onSwipeRight() {
		// startMapActivity();
		// }
		//
		// public void onSwipeLeft() {}
		// }
		//
		//
		// );

	}

	private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener() {

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			

			// View focusCurrent = KeyBoardActivity.this.getWindow()
			// .getCurrentFocus();
			// if (focusCurrent == null
			// || focusCurrent.getClass() != EditText.class)
			// return;
			// EditText edittext = (EditText) focusCurrent;
			// edittext.getText();

			EditText editText0 = (EditText) findViewById(R.id.edittext0);

			start = editText0.length();

			switch (primaryCode) {
			case 1: {

				ssb = keyFirstAid(editText0, ssb, start);

				addStatusToLocationStatusList(primaryCode);
				break;
			}
			case 2: {
				ssb = keySOS(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);

				break;
			}

			case 3: {
				ssb = keyCorpse(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 4: {
				ssb = keyFallenTree(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 5: {
				ssb = keyAvalanche(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 6: {
				ssb = keyEarthQuake(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 7: {
				ssb = keyForestFire(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 8: {
				ssb = keyFlooding(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 9: {
				ssb = keyHighWind(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 10: {
				ssb = keyLandSlide(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 11: {
				ssb = keyTsunami(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 12: {
				ssb = keyTwister(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 13: {
				ssb = keyVolcano(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 14: {
				ssb = keyFireHazard(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);

				break;
			}

			case 15: {
				ssb = keyBioHazard(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 16: {
				ssb = keyRadiationhazard(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 17: {
				ssb = keyExplosionHazard(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 18: {
				ssb = keyHighVoltageHazard(editText0, ssb, start);
				addStatusToLocationStatusList(primaryCode);
				break;
			}

			case 19: {
				keyCheckMark(editText0);
				break;
			}

			case 20: {
				keyCancel();
				break;
			}

			case 21: {
				ssb = keyBackSpace(editText0, ssb, start);
				break;
			}
			}

		}

		/**
		 * @param primaryCode
		 */
		public void addStatusToLocationStatusList(int primaryCode) {
			StatusDataSchema statusDataRecord = d0d0DataObjects
					.getStatusDataRow();
			statusDataRecord = d0d0DataTables.getStatusData(primaryCode);

			LocationStatusSchema locationStatusRecord = d0d0DataObjects
					.getLocationStatusRow();
			locationStatusRecord.Loc_Latitude = latitude;
			locationStatusRecord.Loc_Longitude = longitude;
			locationStatusRecord.Loc_ID = locID;
			locationStatusRecord.STATUS_CODE = statusDataRecord.Status_Code;
			locationStatusRecord.STATUS_DESC = statusDataRecord.Status_Desc;
			locationStatusList.add(locationStatusRecord);
		}

		private SpannableStringBuilder keyBackSpace(EditText editText0,
				SpannableStringBuilder ssb, int start) {
			// TODO Auto-generated method stub

			if (ssb.length() > 0) {

				Editable tmpEditable = editText0.getEditableText();

				ImageSpan[] tmpSpanList = tmpEditable.getSpans(
						ssb.length() - 1, ssb.length(), ImageSpan.class);
				if (tmpSpanList.length > 0) {
					ssb.removeSpan(tmpSpanList[tmpSpanList.length - 1]);

					editText0.setText(ssb, BufferType.SPANNABLE);
				}
			}
			return ssb;

		}

		private void keyCancel() {
			// TODO Auto-generated method stub

		}

		private void keyCheckMark(EditText editText0) {
			// Delete the existing incidents in the LocationStatus Data table
			// and
			// then save the new ones in locationStatusList

//			d0d0DataTables.deleteIncidents(new LatLng(latitude, longitude));

			LocationStatusSchema locationStatusRecord = d0d0DataObjects
					.getLocationStatusRow();
			Iterator<LocationStatusSchema> listIterator = locationStatusList
					.iterator();

			do {

				locationStatusRecord = (LocationStatusSchema) listIterator
						.next();
				d0d0DataTables.createIncident(new LatLng(
						locationStatusRecord.Loc_Latitude,
						locationStatusRecord.Loc_Longitude),
						locationStatusRecord.STATUS_CODE);
			} while (listIterator.hasNext());

			/*
			 * serializableEditText mySerializableEditText = new
			 * serializableEditText();
			 * mySerializableEditText.setEdiTtext(editText0); //
			 * mySerializableEditText.setSSB(ssb); Bundle bundle = new Bundle();
			 * bundle.putSerializable("mySerializableEditText",
			 * mySerializableEditText);
			 * 
			 * Intent intent = new Intent(); intent.putExtras(bundle);
			 * 
			 * setResult(2, intent);
			 */
			finish();

		}

		private SpannableStringBuilder keyHighVoltageHazard(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context,
					R.drawable.ic_highvoltage_hazard);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyExplosionHazard(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context,
					R.drawable.ic_explosion_hazard);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyRadiationhazard(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context,
					R.drawable.ic_radiation_hazard);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyBioHazard(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context,
					R.drawable.ic_bio_hazard_hazard);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyFireHazard(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context,
					R.drawable.ic_fire_hazard);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyVolcano(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_volcano);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyTwister(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_twister);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyTsunami(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_tsunami);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyLandSlide(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_landslide);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyHighWind(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_highwind);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyFlooding(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_flooding);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;
		}

		private SpannableStringBuilder keyForestFire(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_fire);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyEarthQuake(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context,
					R.drawable.ic_earthquake);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyAvalanche(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_avalanche);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyFallenTree(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context,
					R.drawable.ic_fallentree);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyCorpse(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_corpse);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keySOS(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_sos);

			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		private SpannableStringBuilder keyFirstAid(EditText editText0,
				SpannableStringBuilder ssb, int start_pos) {
			// TODO Auto-generated method stub

			ImageSpan iconSpan = new ImageSpan(context, R.drawable.ic_firstaid);
			ssb.append(" ");

			ssb.setSpan(iconSpan, start_pos, start_pos + 1,
					Spannable.SPAN_INCLUSIVE_INCLUSIVE);

			editText0.setText(ssb, BufferType.SPANNABLE);

			return ssb;

		}

		@Override
		public void onPress(int arg0) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeRight() {

			startMapActivity();
		}

		@Override
		public void swipeUp() {
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.key_board, menu);
		return true;
	}

	public void startMapActivity() {
		Intent startMapIntent = new Intent(context, MapActivity.class);
		startActivity(startMapIntent);

	}

}
