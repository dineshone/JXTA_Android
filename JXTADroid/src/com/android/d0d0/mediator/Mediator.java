package com.android.d0d0.mediator;

import java.io.File;
import java.util.Enumeration;
import java.util.logging.Logger;

import net.jxta.content.ContentService;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.FileDocument;
import net.jxta.document.MimeMediaType;
import net.jxta.logging.Logging;
import net.jxta.protocol.ContentShareAdvertisement;

import com.android.d0d0.droidEdge;
import com.android.d0d0.FileObjectManager.FileObject;
import com.android.d0d0.Interface.IFileObjectManager;
import com.android.d0d0.Interface.IMediator;
import com.android.d0d0.SpeechService.d0d0SpeechRecognizer;
import com.android.d0d0.SpeechService.d0d0SpeechRecorder;
import com.android.d0d0.advertisements.d0d0Advertisements;
import com.android.d0d0.file.server.d0d0FileServer;
import com.android.d0d0.FileObjectManager.FileObjectManager;

public final class Mediator implements IMediator {

	public IMediator mediator;
	public d0d0SpeechRecognizer recognizer;
	public d0d0SpeechRecorder recorder;
	public d0d0FileServer fileServer;
	public d0d0Advertisements ads;
	public IFileObjectManager FileObjectManager;

	private final Logger LOG = Logger.getLogger(Mediator.class.getName());	
	
	public Mediator() {
		// TODO Auto-generated constructor stub
		
		//this.mediator = new MediatorImpl();
		
		recognizer = new d0d0SpeechRecognizer(this);
		recorder = new d0d0SpeechRecorder(this);
		fileServer = new d0d0FileServer(this);
		FileObjectManager = new FileObjectManager(this) ;
		FileObjectManager.CheckFileObjectCountInFilePool();
		
	}

	@Override
	public void startVoiceDector() {
		// TODO Auto-generated method stub
		recognizer.startVoiceDector();
	}

	@Override
	public void stopVoiceDector() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startRecording() {
		// TODO Auto-generated method stub

		recorder.startRecording();
	}

	@Override
	public void stopRecording() {
		// TODO Auto-generated method stub
		recorder.stopRecording();

	}

	public FileDocument createFileDocument(File file, MimeMediaType type) {

		FileDocument filedoc = new FileDocument(file, type);

		return filedoc;
	}

	@Override
	public ContentShareAdvertisement getShareAdvForAudioFile(FileDocument fileDoc) {
		return fileServer.getShareAdvForAudioFile(fileDoc);
	}

	@Override
	public final void startJXTA() {
		// TODO Auto-generated method stub
		Logging.logCheckedInfo(LOG,"Starting JXTA");
		droidEdge.startJXTA();

		
		
		
	}

	public ContentService getContentService(){
		return droidEdge.getContentService();
	}

	public DiscoveryService getDiscoveryService(){
		return droidEdge.getDiscoveryService();
	}

	public Enumeration<Advertisement> getLocalContentShareAdvs(){
		
		return ads.getLocalContentShareAdvs();
	}

	@Override
	public FileObject getFileObjectFromFilePool() {
		// To start with, I will use just audio file
		// As the scope widens, I will add code to get other files as well 
		
		return FileObjectManager.getFileObjectFromFilePool("audio");
	}
	
	
	
	public void putFileObjectIntoFileServer(FileObject fileObject){
		
		FileObjectManager.putFileObjectIntoFileServer(fileObject);
	}

	@Override
	public IMediator getMediator() {
		// TODO Auto-generated method stub
		return this.mediator;
	}
}
