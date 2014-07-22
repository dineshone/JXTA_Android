/**
 * 
 */
package com.android.d0d0.FileObjectManager;

import java.io.File;

import com.android.d0d0.Interface.IFileObjectState;


import net.jxta.document.Advertisement;
import net.jxta.protocol.ContentShareAdvertisement;

/**
 * @author d0d0
 * The file could be either audio, text or video
 * To start with, this Factory will create audio files
 * The files will have an advertisement with it
 * The file will have it's own lifecycle status in it
 * The created file object will be added to a queue and made available to clients  
 * Clients will have use a look up pattern or virtual proxy pattern to access the file
 */


public  class FileObject implements IFileObjectState {

	/**
	 * 
	 */

	public String fileName = null;
	public String filePath = null;
	public String fileType = null;
	public File FileObject = null;
	public State fileState;

	public Advertisement FileAdvertisement = null; 
	public ContentShareAdvertisement FileContentShareAdvertisement = null;
	public String AdvertisedfileName = null;




	public FileObject(String fileName, String filePath, String fileType) {
		// TODO Auto-generated constructor stub
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileType = fileType;
		this.fileState = State.created;
		this.FileObject = new File(filePath, fileName + fileType);

	}




	@Override
	public void created() {
		// TODO Auto-generated method stub

	}




	@Override
	public void initialised() {
		// TODO Auto-generated method stub

	}




	@Override
	public void queued() {
		// TODO Auto-generated method stub

	}




	@Override
	public void emptied() {
		// TODO Auto-generated method stub

	}




	@Override
	public void filled() {
		// TODO Auto-generated method stub

	}

}
