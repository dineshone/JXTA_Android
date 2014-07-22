/**
 * 
 */
package com.android.d0d0.Interface;

import java.io.File;

import com.android.d0d0.FileObjectManager.FileObject;


import net.jxta.document.Advertisement;

/*
 * @author d0d0
 *
 */

public interface IFileObjectFactory {

	/*
	 * 
	 * This Factory Interface for creating a file
	 * 	   
	 */	
	
	public FileObject createAudioFile();
	
	public FileObject createTextFile();
	
	public FileObject createImageFile();
	
	public FileObject createVideoFile();

}
