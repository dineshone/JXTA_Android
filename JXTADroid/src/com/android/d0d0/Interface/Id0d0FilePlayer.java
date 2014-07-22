package com.android.d0d0.Interface;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.android.d0d0.FileObjectManager.FileObject;

/*
 * 
 * This interface is used to 'play' files. 
 * The files can be either recorded speech or text or an image or video file
 * This contains the functionality that plays or displays media in the display 
 * This interface will not 'display' the media it is playing when the main display is switched off. 
 * the files that are to be played are queued up in it's queue
 * Later when time permits, I can use the Visitor pattern which handles playing diff file types
 * 
 */

public interface Id0d0FilePlayer {

	BlockingQueue<FileObject> FileObjectPlayerQueue =  new LinkedBlockingQueue<FileObject>() ;
	
	public void putFileObjectToPlayerQueue(FileObject fileObject);
	
	public FileObject getFileObjectfromPlayerQueue();
	
	public void playAudioFile(FileObject audioFileObject);
	
	public void playTextFile(FileObject textFileObject);
	
	public void playVideoFile(FileObject videoFileObject);
	
	public void playFileObject();
		
	
}
