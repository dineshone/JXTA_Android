
package com.android.d0d0.file.player;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.jxta.logging.Logging;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.android.d0d0.FileObjectManager.FileObject;
import com.android.d0d0.Interface.Id0d0FilePlayer;
import com.android.d0d0.file.server.d0d0FileServer;

/**
 * @author d0d0
 *
 */
public class d0d0FilePlayer implements Id0d0FilePlayer {

	/**
	 * 
	 */

	protected final static Logger LOG = Logger.getLogger(d0d0FileServer.class.getName());
	protected FileObject tempFileObject ;
	protected MediaPlayer d0d0MediaPlayer = new MediaPlayer();

	public d0d0FilePlayer() {
		// TODO Auto-generated constructor stub
		
		
	}

	/* (non-Javadoc)
	 * @see com.android.d0d0.file.player.d0d0FilePlayerInterface#playSpeechFile(com.android.d0d0.FileObjectManager.FileObject)
	 */
	@Override
	public void playAudioFile(FileObject speechFileObject) {
		// TODO Auto-generated method stub
		
		try {
			d0d0MediaPlayer.setDataSource(speechFileObject.filePath);
			d0d0MediaPlayer.start();
			
//			d0d0MediaPlayer.stop();
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/* (non-Javadoc)
	 * @see com.android.d0d0.file.player.d0d0FilePlayerInterface#playTextFile(com.android.d0d0.FileObjectManager.FileObject)
	 */
	@Override
	public void playTextFile(FileObject textFileObject) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.android.d0d0.file.player.d0d0FilePlayerInterface#playVideoFile(com.android.d0d0.FileObjectManager.FileObject)
	 */
	@Override
	public void playVideoFile(FileObject videoFileObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void putFileObjectToPlayerQueue(FileObject fileObject) {

		FileObjectPlayerQueue.offer(fileObject);

	}

	@Override
	public FileObject getFileObjectfromPlayerQueue() {

		try {
			return FileObjectPlayerQueue.poll(5L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Logging.logCheckedInfo(LOG, "unable to get FileObject from FileObjectPlayerQueue");
			e.printStackTrace();
			return null;
		}

	}

	/*
	 * 
	 * This method is called whenever a FileObject is placed in the queue
	 * It activates the thread in the method playFileObject, If the thread is not active
	 * If the thread is active, it does nothing
	 * 
	 */

	@Override
	public void playFileObject() {
		// TODO Auto-generated method stub

		Thread playerThread = new Thread(){

			public void run(){
				while(FileObjectPlayerQueue.size() > 0)
				{
					try {
						tempFileObject = FileObjectPlayerQueue.poll(1L, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (tempFileObject != null)
					{
						
						playAudioFile(tempFileObject);
					}
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Logging.logCheckedInfo(LOG, "unable to get FileObjectPlayer to Sleep");
					e.printStackTrace();
				}
			}
		};

		if(!playerThread.isAlive())
		{
			playerThread.start();
		}
	}

}
