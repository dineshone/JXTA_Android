package com.android.d0d0.file.server;



import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.jxta.content.Content;
import net.jxta.content.ContentID;
import net.jxta.content.ContentShare;
import net.jxta.document.FileDocument;
import net.jxta.id.IDFactory;
import net.jxta.logging.Logging;
import net.jxta.protocol.ContentShareAdvertisement;

import com.android.d0d0.droidEdge;
import com.android.d0d0.FileObjectManager.FileObject;
import com.android.d0d0.Interface.IMediator;
import com.android.d0d0.mediator.*;



/**
 * @author d0d0
 * 
 * This class serves the files created by the d0d0 application 
 * To start with it the application creates audio files
 * Later on when the scope increases the application will create and 
 * serve image files and video stream is necessary  
 *
 */


public class d0d0FileServer {

	private ContentID contentID;
	private IMediator mediator;

	private static BlockingQueue<FileObject> FileObjectServerQueue =  new LinkedBlockingQueue<FileObject>() ;

	protected final static Logger LOG = Logger.getLogger(d0d0FileServer.class.getName());

	public d0d0FileServer( IMediator mediator) {
		// TODO Auto-generated constructor stub
		this.mediator = mediator;
	}

	public ContentShareAdvertisement getShareAdvForAudioFile(FileDocument audioFileDoc){

		contentID = IDFactory.newContentID( droidEdge.TheNetPeerGroup.getPeerGroupID(), false);


		Content content = new Content(contentID, null, audioFileDoc);

		ContentShare shares = (ContentShare) droidEdge.TNPGContentService.shareContent(content);


			/*
			 * We'll attach a listener to the ContentShare so that we
			 * can see any activity relating to it.
			 */
			//             share.addContentShareListener(shareListener);

			/*
			 * Each ContentShare has it's own Advertisement, so we publish
			 * them all.
			 */
			ContentShareAdvertisement adv = shares.getContentShareAdvertisement();
			
			return adv;
//			d0d0 I shuld probably use another method to publish the adv from the file server queue			
//			try {
//				d0d0Edge.TNPGDiscoveryService.publish(adv);
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	


	public void putFileObjectToServerQueue(FileObject fileObject) {
		// TODO Auto-generated method stub

		FileObjectServerQueue.offer(fileObject);

	}

	public FileObject getFileObjectfromServerQueue() {

		try {
			return FileObjectServerQueue.poll(5L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Logging.logCheckedInfo(LOG, "unable to get FileObject from FileObjectServerQueue");
			e.printStackTrace();
			return null;
		}

	}



}
