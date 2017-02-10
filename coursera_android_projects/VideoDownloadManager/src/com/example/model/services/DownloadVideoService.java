package com.example.model.services;

import com.example.model.mediator.webdata.*;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Intent Service that runs in background and uploads the Video with a
 * given Id.  After the operation, it broadcasts the Intent to send
 * the result of the upload to the PlayVideoActivity.
 */
public class DownloadVideoService 
       extends IntentService {
    /**
     * Custom Action that will be used to send Broadcast to the
     * PlayVideoActivity.
     */
    public static final String ACTION_DOWNLOAD_SERVICE_RESPONSE =
                "com.example.services.DownloadVideoService.RESPONSE";
    
    /**
     * It is used by Notification Manager to send Notifications.
     */
    private static final int NOTIFICATION_ID = 2;
    
    /**
     * VideoDataMediator mediates the communication between Video
     * Service and local storage in the Android device.
     */
    private VideoDataMediator mVideoMediator;
    
    /**
     * Manages the Notification displayed in System UI.
     */
    private NotificationManager mNotifyManager;
    
    /**
     * Builder used to build the Notification.
     */
    private Builder mBuilder;
    
    /**
     * Constructor for UploadVideoService.
     * 
     * @param name
     */
    public DownloadVideoService(String name) {
        super("DownloadVideoService");     
    }
    
    /**
     * Constructor for UploadVideoService.
     * 
     * @param name
     */
    public DownloadVideoService() {
        super("DownloadVideoService");     
    }
    
    /**
     * Factory method that makes the explicit intent another Activity
     * uses to call this Service.
     * 
     * @param context
     * @param videoId
     * @return
     */
    public static Intent makeIntent(Context context,
                                    Long videoID) {
        return new Intent(context, 
        			DownloadVideoService.class)
                   .putExtra("VEDIOID",videoID);
    }
    
    /**
     * Hook method that is invoked on the worker thread with a request
     * to process. Only one Intent is processed at a time, but the
     * processing happens on a worker thread that runs independently
     * from other application logic.
     * 
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Starts the Notification to show the progress of video
        // upload.
    	startDownloadNotification();
        
        // Create VideoDataMediator that will mediate the communication
        // between Server and Android Storage.
        mVideoMediator =
            new VideoDataMediator(); 

        // Check if Video Download is successful.
        /*finishDownloadNotification(mVideoMediator.DownloadVideo(getApplicationContext(),
                                                   intent.getLongExtra("VEDIOID", 0)));*/
        
        String st[] = null;
		try {
			st = mVideoMediator.DownloadVideo(getApplicationContext(),
			        intent.getLongExtra("VEDIOID", 0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        finishDownloadNotification(st[0]);
        String downloadedPath = st[1];
        
        
             
        // Send the Broadcast to PlayVideoActivity that the Video
        // Upload is completed.
        
        //sendBroadcast();
        LocalBroadcastManager.getInstance(this)
        .sendBroadcast(new Intent(ACTION_DOWNLOAD_SERVICE_RESPONSE)
                       .addCategory(Intent.CATEGORY_DEFAULT)
                       .putExtra("DOWNLOADED_PATH", downloadedPath));
    }
    
    /**
     * Send the Broadcast to Activity that the Video Download is
     * completed.
     */
    private void sendBroadcast(){
        // Use a LocalBroadcastManager to restrict the scope of this
        // Intent to the VideoUploadClient application.
        LocalBroadcastManager.getInstance(this)
             .sendBroadcast(new Intent(ACTION_DOWNLOAD_SERVICE_RESPONSE)
                            .addCategory(Intent.CATEGORY_DEFAULT));
    }
    
    /**
     * Finish the Notification after the Video is Uploaded.
     * 
     * @param status
     */
    private void finishDownloadNotification(String status) {
        // When the loop is finished, updates the notification.
        mBuilder.setContentText(status)
                // Removes the progress bar.
                .setProgress (0,
                              0,
                              false)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentText(status) 
                .setTicker("");

        // Build the Notification with the given
        // Notification Id.
        mNotifyManager.notify(NOTIFICATION_ID,
                              mBuilder.build());
    }
    
    /**
     * Starts the Notification to show the progress of video upload.
     */
    private void startDownloadNotification() {
        // Gets access to the Android Notification Service.
        mNotifyManager = (NotificationManager)
            getSystemService(Context.NOTIFICATION_SERVICE); 

        // Create the Notification and set a progress indicator for an
        // operation of indeterminate length.
        mBuilder = new NotificationCompat
                       .Builder(this)
                       .setContentTitle("Video Download") 
                       .setContentText("Download in progress") 
                       .setSmallIcon(android.R.drawable.stat_sys_download)
                       .setTicker("Downloading video")
                       .setProgress(0,
                                    0,
                                    true);
 
        // Build and issue the notification.
        mNotifyManager.notify(NOTIFICATION_ID,
                              mBuilder.build());
    }
}
