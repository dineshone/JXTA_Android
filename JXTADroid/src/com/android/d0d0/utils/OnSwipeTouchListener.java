/**
 * 
 */
package com.android.d0d0.utils;

import com.android.d0d0.ApplicationClass;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;

/**
 * @author d0d0 I hope to use this class to implement swiping between Activity.
 * 
 */
public class OnSwipeTouchListener implements OnTouchListener {

	public static Context context = ApplicationClass
			.getApplicationClassContext();
	private final GestureDetector gestureDetector = new GestureDetector(
			context, new GestureListener());

	/**
	 * 
	 */
	public OnSwipeTouchListener() {
		// TODO Auto-generated constructor stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent motionEvent) {
		// TODO Auto-generated method stub
		return gestureDetector.onTouchEvent(motionEvent);
	}

	private final class GestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			boolean result = false;
			try {
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (Math.abs(diffX) > SWIPE_THRESHOLD
							&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffX > 0) {
							onSwipeRight();
						} else {
							onSwipeLeft();
						}
					}
				} else {
					if (Math.abs(diffY) > SWIPE_THRESHOLD
							&& Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffY > 0) {
							onSwipeBottom();
						} else {
							onSwipeTop();
						}
					}
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return result;
		}
	}

	public void onSwipeRight() {
	}

	public void onSwipeLeft() {
	}

	public void onSwipeTop() {
	}

	public void onSwipeBottom() {
	}

}
