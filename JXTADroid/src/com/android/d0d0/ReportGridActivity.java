package com.android.d0d0;

import com.android.d0d0.utils.OnSwipeTouchListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReportGridActivity extends Activity {

	GridView MainReportGrid;
	public static Context context = ApplicationClass
			.getApplicationClassContext();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_grid);

		MainReportGrid = (GridView) findViewById(R.id.report_grid);

		MainReportGrid.setAdapter(new ImageAdapter(this));
		MainReportGrid.setOnTouchListener(new OnSwipeTouchListener() {
			public void onSwipeTop() {
			}

			public void onSwipeBottom() {
			}

			public void onSwipeLeft() {
				startMapActivity();
			}

			public void onSwipeRight() {
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report_grid, menu);
		return true;
	}

	public class ImageAdapter extends BaseAdapter {
		Context MyContext;

		public ImageAdapter(Context _MyContext) {
			MyContext = _MyContext;
		}

		@Override
		public int getCount() {
			/* Set the number of element we want on the grid */
			return 9;
		}

		// Get a View that displays the data at the specified position in the
		// data set
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View MyView = convertView;

			if (convertView == null) {
				/* we define the view that will display on the grid */

				// Inflate the layout
				// LayoutInflater li = getLayoutInflater();
				 LayoutInflater li = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
				 MyView = li.inflate(R.layout.report_grid_item, null);


				// // Add The Text!!!
				 TextView tv = (TextView) MyView
				 .findViewById(R.id.grid_item_text);
				 tv.setText(R.string.Number_Zero);

				// Add The Image!!!
				 
				ImageView iv = (ImageView) MyView
						.findViewById(R.id.grid_item_image);
				iv.setImageResource(mThumbIds[position]);
			} else {
				MyView =  convertView;
			}

			
			return MyView;
		}

		public Integer[] mThumbIds = { 
				R.drawable.ic_corpse, R.drawable.ic_sos, 
				R.drawable.ic_firstaid, R.drawable.ic_fallentree,
				R.drawable.ic_fire, R.drawable.ic_flooding,
				R.drawable.ic_general_warning_hazard, R.drawable.ic_highvoltage_hazard,
				R.drawable.ic_highwind,R.drawable.ic_landslide, 
				R.drawable.ic_radiation_hazard, R.drawable.ic_toxic_hazard,
				R.drawable.ic_tsunami, R.drawable.ic_twister,
				R.drawable.ic_volcano, R.drawable.ic_watch_your_step_hazard

		};
		
		

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	public void startMapActivity() {
		Intent startMapIntent = new Intent(context, MapActivity.class);
		startActivity(startMapIntent);

	}
}
