/**
 * 
 */
package com.android.d0d0.FileObjectManager;

import com.android.d0d0.droidEdge;
import com.android.d0d0.Interface.IFileObjectFactory;
import com.android.d0d0.Interface.IFileObjectManager;
import com.android.d0d0.Interface.IFileObjectPool;

/**
 * @author d0d0
 *
 */
public class FileObjectFactory implements IFileObjectFactory {

	/**
	 * 
	 */
	
	
	
	public  int createdFileObjectCounter = 0;
	
	protected IFileObjectPool filePool;
	protected IFileObjectManager fileObjectManager;
	protected String fileName = null;
	protected String filePath = null;
	protected String fileType = null; 
	
	public FileObjectFactory(FileObjectManager fileObjectManager) {
		// TODO Auto-generated constructor stub
		this.fileObjectManager = fileObjectManager;
		
	}

	/* (non-Javadoc)
	 * @see com.android.d0d0.FileObjectManager.FileFactory#createAudioFile()
	 */
	@Override
	public FileObject createAudioFile() {
		// TODO Auto-generated method stub
		
		createdFileObjectCounter += 1;
		fileName = "d0d0audioMsg" + createdFileObjectCounter ;
		filePath = droidEdge.context.getFilesDir().toString();
		fileType = ".3gp" ;
		FileObject audioFileMsg = new FileObject(fileName, filePath, fileType );
		
		
		
		return audioFileMsg;

	}

	/* (non-Javadoc)
	 * @see com.android.d0d0.FileObjectManager.FileFactory#createTextFile()
	 */
	@Override
	public FileObject createTextFile() {
		createdFileObjectCounter ++;
		fileName = "d0d0textMsg" + createdFileObjectCounter ;
		filePath = droidEdge.context.getFilesDir().toString();
		fileType = ".txt" ;
		FileObject textFileMsg = new FileObject(fileName, filePath, fileType );
		
		return textFileMsg;

	}

	/* (non-Javadoc)
	 * @see com.android.d0d0.FileObjectManager.FileFactory#createImageFile()
	 */
	@Override
	public FileObject createImageFile() {
		
		createdFileObjectCounter ++;
		fileName = "d0d0imageMsg" + createdFileObjectCounter ;
		filePath = droidEdge.context.getFilesDir().toString();
		fileType = ".jpg" ;
		FileObject imageFileMsg = new FileObject(fileName, filePath, fileType );
		
		return imageFileMsg;

	}

	/* (non-Javadoc)
	 * @see com.android.d0d0.FileObjectManager.FileFactory#createVideoFile()
	 */
	@Override
	public FileObject createVideoFile() {
		return null;
		// TODO Auto-generated method stub

	}

}
