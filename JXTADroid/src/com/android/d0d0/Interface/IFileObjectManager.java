package com.android.d0d0.Interface;

import com.android.d0d0.FileObjectManager.FileObject;


/*
 * 
 * This Manager coordinates all the activities for the FileObject
 * It is the Mediator between all the coordinating objects in the FileObject
 * The ObjectManager performs the following:
 *  Puts the FileObject, into the FilePool
 *  Puts the FileObject into the FileClient
 *  Puts the FileObject into the FilePlayer
 *  Puts the FileObject into the FileServer
 * 	gets the FileObject from the FilePool
 * 	creates a FileObject using the FileObjectFactory
 * 	checks the count of the FileObjects, if < 5, creates more fileObjects
 * 	if the FileObject count is more than 5, then deletes the fileObjects
 */


public interface IFileObjectManager {

	public void putFileObjectIntoFilePool(FileObject fileObject);

	public void putFileObjectIntoFileClient(FileObject fileObject);

	public void putFileObjectIntoFilePlayer(FileObject fileObject);

	public void putFileObjectIntoFileServer(FileObject fileObject);
	
	public FileObject getFileObjectFromFilePool(String FileType);
	
	public void createFileObject(String FileType);
	
	public void createMultipleFileObjectsInFilePool(String FileType, int numToCreate);
	
	public void CheckFileObjectCountInFilePool();
	
	public void deleteMultipleFileObjectsInFilePool(String FileType, int numToDelete);
	
	
	
	
	
	
}
