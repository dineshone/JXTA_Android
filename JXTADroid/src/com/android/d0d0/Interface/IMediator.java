/**
 * 
 */
package com.android.d0d0.Interface;

import java.io.File;
import java.util.Enumeration;
import java.util.Observer;

import com.android.d0d0.FileObjectManager.FileObject;

import net.jxta.content.ContentService;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.FileDocument;
import net.jxta.document.MimeMediaType;
import net.jxta.protocol.ContentShareAdvertisement;
import net.jxta.service.Service;

import android.media.MediaRecorder;
import android.speech.RecognitionListener;

/**
 * @author d0d0
 *	This is the mediator that mediates interactions/communications between objects.
 * 	For Ex: The Service object can start the speech recognizer using the method startVoiceDetector
 * 	 
 */

public interface IMediator 
	{
	
	IMediator getMediator();
	
	void startJXTA();

	void startVoiceDector();

	public void stopVoiceDector();

	public void startRecording();

	public void stopRecording();

	public ContentShareAdvertisement getShareAdvForAudioFile(FileDocument fileDoc);

	public FileDocument createFileDocument(File file, MimeMediaType type );

	public ContentService getContentService();

	public DiscoveryService getDiscoveryService();

	public Enumeration<Advertisement> getLocalContentShareAdvs();
	
	public FileObject getFileObjectFromFilePool();
	
	public void putFileObjectIntoFileServer(FileObject fileObject);
	
}
