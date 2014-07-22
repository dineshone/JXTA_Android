/**
 * 
 */
package com.android.d0d0.FileObjectManager;

import java.util.concurrent.TimeUnit;

import net.jxta.logging.Logging;

import com.android.d0d0.Interface.IFileObjectFactory;
import com.android.d0d0.Interface.IFileObjectManager;
import com.android.d0d0.Interface.IFileObjectPool;
import com.android.d0d0.Interface.IMediator;
import com.android.d0d0.file.server.d0d0FileServer;
import com.android.d0d0.mediator.Mediator;

/**
 * @author d0d0
 * 
 * FileObject Manager is the object that manages the FileObject resource.
 * This object follows the Object Manager design pattern
 * the mediator object coordinates it's activities with this object 
 *
 */

public class FileObjectManager implements IFileObjectManager {

	protected IMediator mediator;
	protected IFileObjectFactory fileFactory;
	protected IFileObjectPool filePool;
	protected d0d0FileServer fileServer;
	
	protected int fileObjectPoolCount;
	
	public FileObjectManager(Mediator mediator) {
		// TODO Auto-generated constructor stub
		this.mediator = mediator;
		fileFactory = new FileObjectFactory(this);
		filePool = new FileObjectPool(this);
		// does fileServer need a mediator ? will look into it later
		fileServer = new d0d0FileServer(mediator);
		
		runPeriodicTasks();
	}

	/* (non-Javadoc)
	 * @see com.android.d0d0.FileObjectManager.FileObjectManagerInterface#putFileObjectIntoFilePool()
	 */
	@Override
	public void putFileObjectIntoFilePool(FileObject fileObject) {
		// TODO Auto-generated method stub
		
		filePool.putFileObjectToPoolQueue(fileObject);

	}

	/* (non-Javadoc)
	 * 
	 */
	@Override
	public void putFileObjectIntoFileClient(FileObject fileObject) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * 
	 */
	@Override
	public void putFileObjectIntoFilePlayer(FileObject fileObject) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * 
	 */
	@Override
	public void putFileObjectIntoFileServer(FileObject fileObject) {
		fileServer.putFileObjectToServerQueue(fileObject);
		

	}

	/* (non-Javadoc)
	 * @see com.android.d0d0.FileObjectManager.FileObjectManagerInterface#getFileObjectFromFilePool(java.lang.String)
	 */
	@Override
	public FileObject getFileObjectFromFilePool(String FileType) {
		// TODO Auto-generated method stub
		return filePool.getFileObjectfromPoolQueue();
	}
	
	
	public void createFileObject(String FileType){
		
		if(FileType == "audio")
		{
			putFileObjectIntoFilePool(fileFactory.createAudioFile());
		}
	}

	@Override
	public void createMultipleFileObjectsInFilePool(String FileType, int numToCreate) {
		// TODO Auto-generated method stub
		
		int i = 0;
		
		while(i < numToCreate){
			createFileObject("audio");
			i++;
		}
	}

	
	@Override
	public void CheckFileObjectCountInFilePool() {
		// TODO Auto-generated method stub
		
		fileObjectPoolCount = filePool.getFileObjectNumbers();
	
		if (fileObjectPoolCount > IFileObjectPool.fileObjectNumbers){
			
			deleteMultipleFileObjectsInFilePool("audio", fileObjectPoolCount - IFileObjectPool.fileObjectNumbers );
						
		}else if(fileObjectPoolCount < IFileObjectPool.fileObjectNumbers){
			
			createMultipleFileObjectsInFilePool("audio", IFileObjectPool.fileObjectNumbers - fileObjectPoolCount);
		}
		
	}

	@Override
	public void deleteMultipleFileObjectsInFilePool(String FileType, int numToDelete) {
		// TODO Auto-generated method stub
		
		int j = 0;
		
		while(j < numToDelete){
			filePool.deleteFileObjectInFilePool("audio");
			j++;
		}
		
	}

	
	protected void runPeriodicTasks()
	{
	Thread periodicTasksThread = new Thread(){

		public void run(){
			while(true)
			{
				CheckFileObjectCountInFilePool();
				
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	};

	if(!periodicTasksThread.isAlive())
	{
		periodicTasksThread.start();
	}

	}
}
