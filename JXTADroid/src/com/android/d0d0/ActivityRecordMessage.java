package com.android.d0d0;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ActivityRecordMessage extends Activity {

	public static Context context = ApplicationClass
			.getApplicationClassContext();

	final int REQUEST_IMAGE_CAPTURE = 1;

	int imageSequence = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_message);

		ImageView imgViewLeft = (ImageView) findViewById(R.id.imageViewLeft);
		ImageView imgViewCenter = (ImageView) findViewById(R.id.imageViewCenter);
		ImageView imgViewRight = (ImageView) findViewById(R.id.imageViewRight);

		ImageButton imgBtnCamera = (ImageButton) findViewById(R.id.imageButtonCamera);

		imgBtnCamera.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(context
						.getPackageManager()) != null) {
					imageSequence++;
					startActivityForResult(takePictureIntent,
							REQUEST_IMAGE_CAPTURE);

				}

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		ImageView imgViewLeft = (ImageView) findViewById(R.id.imageViewLeft);
		ImageView imgViewCenter = (ImageView) findViewById(R.id.imageViewCenter);
		ImageView imgViewRight = (ImageView) findViewById(R.id.imageViewRight);

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			if (imageSequence == 1) {
				imgViewLeft.setImageBitmap(imageBitmap);
			} else if (imageSequence == 2) {
				imgViewCenter.setImageBitmap(imageBitmap);
			} else if (imageSequence == 3) {
				imgViewRight.setImageBitmap(imageBitmap);
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_record_message, menu);
		return true;
	}

}
