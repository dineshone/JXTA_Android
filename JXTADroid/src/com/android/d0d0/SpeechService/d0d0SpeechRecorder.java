package com.android.d0d0.SpeechService;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.jxta.document.FileDocument;
import net.jxta.document.MimeMediaType;
import net.jxta.logging.Logging;
import android.media.MediaRecorder;

import com.android.d0d0.droidEdge;
import com.android.d0d0.FileObjectManager.FileObject;
import com.android.d0d0.Interface.IMediator;
import com.android.d0d0.mediator.Mediator;

public class d0d0SpeechRecorder {

	protected IMediator mediator;
	protected MediaRecorder recorder = getRecorder();
	protected FileObject audioMsgFileObject;
	protected String audioMsgFilePath = droidEdge.context.getFilesDir().toString()+ "audioMsg.3gp";
	protected File audioMsgFile = new File(audioMsgFilePath);

	private final static Logger LOG = Logger.getLogger(d0d0SpeechService.class.getName());

	


	public d0d0SpeechRecorder(IMediator mediator) {
		// TODO Auto-generated constructor stub
		this.mediator = mediator;
	}


	public void stopRecording() {
		
		Logging.logCheckedInfo(LOG, "stopping recorder");
		recorder.stop();
		//			 recorder.reset();   
		//			 recorder.release(); 
		
		FileDocument audioFileDoc = mediator.createFileDocument(audioMsgFileObject.FileObject, MimeMediaType.AOS);
		audioMsgFileObject.FileContentShareAdvertisement = mediator.getShareAdvForAudioFile(audioFileDoc);
		mediator.putFileObjectIntoFileServer(audioMsgFileObject);
		
	}

	/**
	 * 
	 */
	public void startRecording() {

		Logging.logCheckedInfo(LOG, "starting recording");
		
		audioMsgFileObject = mediator.getFileObjectFromFilePool();
		audioMsgFilePath = audioMsgFileObject.FileObject.getAbsolutePath();
		recorder.setOutputFile(audioMsgFilePath);
		
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		recorder.start();   

	}

	
	/**
	 * @return
	 */
	
	protected MediaRecorder getRecorder() {
		MediaRecorder recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOnErrorListener(recorderErrorListener);
		recorder.setOnInfoListener(recorderInfoListener);
		return recorder;
	}

	
	private MediaRecorder.OnErrorListener recorderErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
        	Logging.logCheckedInfo(LOG,"Error: " + what + ", " + extra);
        }
};


private MediaRecorder.OnInfoListener recorderInfoListener = new MediaRecorder.OnInfoListener() {
    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
    	Logging.logCheckedInfo(LOG,"Warning: " + what + ", " + extra);
    }
};


}
