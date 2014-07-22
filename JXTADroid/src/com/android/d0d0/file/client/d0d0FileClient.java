package com.android.d0d0.file.client;

import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.jxta.content.ContentService;
import net.jxta.content.ContentTransfer;
import net.jxta.content.ContentTransferEvent;
import net.jxta.content.ContentTransferListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.logging.Logging;
import net.jxta.protocol.ContentShareAdvertisement;

import com.android.d0d0.FileObjectManager.FileObject;
import com.android.d0d0.Interface.IFileObjectManager;
import com.android.d0d0.Interface.IMediator;
import com.android.d0d0.file.server.d0d0FileServer;
import com.android.d0d0.mediator.*;

public class d0d0FileClient {

	/*
	 * 1) get ContentService 2) get discoveryService 3) retrieve Content Share
	 * Advertisement 4) iterate through the ContentShareAdvs & get the content
	 * ID from the advertisement 5) retrieve content 6) invoke d0d0SpeechPlayer
	 * class to play the retrieved file
	 */

	public d0d0FileClient() {
		// TODO Auto-generated constructor stub
	}

	private IMediator mediator = new Mediator();
	private static BlockingQueue<FileObject> FileObjectClientQueue =  new LinkedBlockingQueue<FileObject>() ;

	protected ContentService CService = mediator.getContentService();
	protected DiscoveryService DService = mediator.getDiscoveryService();
	protected final static Logger LOG = Logger.getLogger(d0d0FileServer.class.getName());
	
	protected IFileObjectManager FileObjectManager;
	protected FileObject fileObject;
	protected Enumeration<Advertisement> ContentShareAdvs = mediator
			.getLocalContentShareAdvs();
	
	/**
     * Content transfer listener used to receive asynchronous updates regarding
     * the transfer in progress.
     */
    private ContentTransferListener xferListener =
            new ContentTransferListener() {
        public void contentLocationStateUpdated(ContentTransferEvent event) {
            System.out.println("Transfer location state: "
                    + event.getSourceLocationState());
            System.out.println("Transfer location count: "
                    + event.getSourceLocationCount());
        }

        public void contentTransferStateUpdated(ContentTransferEvent event) {
            System.out.println("Transfer state: " + event.getTransferState());
        }

        public void contentTransferProgress(ContentTransferEvent event) {
            Long bytesReceived = event.getBytesReceived();
            Long bytesTotal = event.getBytesTotal();
            System.out.println("Transfer progress: "
                    + bytesReceived + " / " + bytesTotal);
        }
    };

	

	public void iterateContent() {

		while (ContentShareAdvs.hasMoreElements()) {
			
			fileObject = FileObjectManager.getFileObjectFromFilePool("audio"); 
			fileObject.FileContentShareAdvertisement = (ContentShareAdvertisement)ContentShareAdvs;
			ContentTransfer transfer = CService.retrieveContent(fileObject.FileContentShareAdvertisement);
			transfer.addContentTransferListener(xferListener);
// 			at this point I dont want a content transfer listener or a content transfer aggregator listener
			if (transfer == null ){
				transfer.startSourceLocation();
				transfer.startTransfer(fileObject.FileObject);
			} else{
				transfer.startTransfer(fileObject.FileObject);
			}
		}
		

	}

	

	public void putFileObjectToClientQueue(FileObject fileObject) {
		// TODO Auto-generated method stub

		FileObjectClientQueue.offer(fileObject);

	}

	public FileObject getFileObjectfromClientQueue() {

		try {
			return FileObjectClientQueue.poll(5L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Logging.logCheckedInfo(LOG, "unable to get FileObject from FileObjectServerQueue");
			e.printStackTrace();
			return null;
		}

	}
	
}
