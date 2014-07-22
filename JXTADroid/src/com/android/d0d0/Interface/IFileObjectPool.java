package com.android.d0d0.Interface;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.android.d0d0.FileObjectManager.FileObject;

/*
 * 
 * This interface is based on the resource pool pattern
 * This interface provides a blocking queue to pool the resources
 * The term resources mainly refers to FileObjects
 * The fileObjects in ths pool queue will be of status 'created'.
 * When needed, the FileObjectCordinator will obtain fileObjects from this pool queue
 * After using the files, the FileObjectCordinator will return fileObjects back to this queue
 * fileObjectNumbers will also ensure that a certain number(5) of filesObjects are always available in the pool constantly
 * 
 */

public interface IFileObjectPool  {

	int fileObjectNumbers = 5;
	
	BlockingQueue<FileObject> FileObjectPoolQueue =  new LinkedBlockingQueue<FileObject>() ;
 
	 public void putFileObjectToPoolQueue(FileObject fileObject);
	 public FileObject getFileObjectfromPoolQueue();
	 public int getFileObjectNumbers();
	 public void createFileObjectInFilePool(String fileType);
	 public void deleteFileObjectInFilePool(String fileType);
	  

}
