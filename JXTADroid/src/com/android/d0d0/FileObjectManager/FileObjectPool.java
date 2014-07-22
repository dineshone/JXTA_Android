/**
 * 
 */
package com.android.d0d0.FileObjectManager;


import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.android.d0d0.Interface.IFileObjectManager;
import com.android.d0d0.Interface.IFileObjectPool;

import net.jxta.logging.Logging;



/*
 * @author d0d0
 * FileObject Pool is responsible for creating and maintaining files in the pool
 */

public class FileObjectPool implements IFileObjectPool {
	

	
	protected final static Logger LOG = Logger.getLogger(FileObjectPool.class.getName());
	protected IFileObjectManager fileObjectManager;
	
	
	public FileObjectPool(FileObjectManager fileObjectManager) {
		// TODO Auto-generated constructor stub
		this.fileObjectManager = fileObjectManager;
	}

	@Override
	public void putFileObjectToPoolQueue(FileObject fileObject) {
		// TODO Auto-generated method stub
		
		FileObjectPoolQueue.offer(fileObject);
		
	}

	@Override
	public FileObject getFileObjectfromPoolQueue() {
		
		try {
			return FileObjectPoolQueue.poll(5L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Logging.logCheckedInfo(LOG, "unable to get FileObject from FileObjectPoolQueue");
			e.printStackTrace();
			return null;
		}
		
		
		
	}

	@Override
	public int getFileObjectNumbers() {
		// TODO Auto-generated method stub
		
		return FileObjectPoolQueue.size();
	
	}

	@Override
	public void createFileObjectInFilePool(String fileType) {
		// TODO Auto-generated method stub
		
		fileObjectManager.createFileObject(fileType);
		
		
	}

	@Override
	public void deleteFileObjectInFilePool(String fileType) {
		// d0d0 I am not checking for the file types yet ...
		// to start with, I will just focus on Audio files ...
		
		if( !FileObjectPoolQueue.isEmpty()){
			
			FileObjectPoolQueue.remove(FileObjectPoolQueue.element());
		}
		
	}

	
}
