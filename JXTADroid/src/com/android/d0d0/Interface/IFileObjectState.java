package com.android.d0d0.Interface;


/*
 * This interface follows the Methods for State pattern
 * This interface provides the various states and it's methods
 * 
 */

public interface IFileObjectState {
	
	public enum State{
		created,
		emptied,
		filled
};
	public void created();
	public void initialised();
	public void queued(); 
	public void emptied();
	public void filled();
	
	
	
}
