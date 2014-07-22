package com.android.d0d0.advertisements;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;

import com.android.d0d0.FileObjectManager.FileObject;
import com.android.d0d0.Interface.IMediator;
import com.android.d0d0.mediator.Mediator;

public class d0d0Advertisements {

	public d0d0Advertisements() {
		// TODO Auto-generated constructor stub
	}

	BlockingQueue<Advertisement> AdvertisementQueue =  new LinkedBlockingQueue<Advertisement>() ;
	private IMediator mediator = new Mediator();

	/*
	 * 1) get discovery service 
	 * 2) look for remote advertisements 
	 * 3) look for local advertisements 
	 * 4) return advertisements of a given type 'contentAdv'
	 * 5) whenever you get a new advertisement provide a callback or an observable interface to file client
	 * 6) A method to add advertisements to the queue
	 * 7) A method to start downloading the file in the FileClient Object 
	 * 
	 */

	DiscoveryService dservice = mediator.getDiscoveryService();

	public Enumeration<Advertisement> getLocalContentShareAdvs() {
		
		Enumeration<Advertisement> fileAdvertisements = null;
		try {
			fileAdvertisements = dservice
					.getLocalAdvertisements(3, "type", "jxta:ContentShare");
			
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileAdvertisements;
		
	}

	public void getRemoteContentShareAdvs() {
		int RemoteQueryID = dservice
				.getRemoteAdvertisements(null, 3, "type", "jxta:ContentShare", 10);
	}

	
	
	
	
}
