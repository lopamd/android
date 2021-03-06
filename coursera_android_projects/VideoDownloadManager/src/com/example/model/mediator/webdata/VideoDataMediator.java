package com.example.model.mediator.webdata;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import com.example.model.mediator.webdata.VideoServiceProxy;
import com.example.model.mediator.webdata.VideoStatus;
import com.example.model.mediator.webdata.VideoStatus.VideoState;
import com.example.utils.Constants;
import com.example.utils.VideoMediaStoreUtils;
import com.example.utils.VideoStorageUtils;
import com.example.view.VideoCursorAdapter;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Mediates communication between the Video Service and the local
 * storage on the Android device.  The methods in this class block, so
 * they should be called from a background thread (e.g., via an
 * AsyncTask).
 */
public class VideoDataMediator {
    /**
     * Status code to indicate that file is successfully
     * uploaded.
     */
    public static final String STATUS_UPLOAD_SUCCESSFUL =
        "Upload succeeded";
    
    protected final String TAG = getClass().getSimpleName();
    /**
     * Status code to indicate that file is successfully
     * downloaded.
     */
    public static final String STATUS_DOWNLOAD_SUCCESSFUL =
        "Download succeeded";
    
    /**
     * Status code to indicate that file upload failed 
     * due to large video size.
     */
    public static final String STATUS_UPLOAD_ERROR_FILE_TOO_LARGE =
        "Upload failed: File too big";
    
    /**
     * Status code to indicate that file upload failed.
     */
    public static final String STATUS_UPLOAD_ERROR =
        "Upload failed";
    
    /**
     * Status code to indicate that file download failed.
     */
    public static final String STATUS_DOWNLOAD_ERROR =
        "Download failed";
    
    public static final String STATUS_UPDATE_SUCCESSFUL =
            "Update Rating succeeded";
    
    public static final String STATUS_UPDATE_ERROR =
            "Rating Update Failed";
    
    public static File videoDownloadedFile;
    /**
     * Defines methods that communicate with the Video Service.
     */
    private VideoServiceProxy mVideoServiceProxy;
    
    private VideoCursorAdapter mVideoCursorAdapter;
    
    
    /**
     * Constructor that initializes the VideoDataMediator.
     * 
     * @param context
     */
    public VideoDataMediator() {
        // Initialize the VideoServiceProxy.
        mVideoServiceProxy = new RestAdapter
            .Builder()
            .setEndpoint(Constants.SERVER_URL)
            .build()
            .create(VideoServiceProxy.class);
    }

    /**
     * Uploads the Video having the given Id.  This Id is the Id of
     * Video in Android Video Content Provider.
     * 
     * @param videoId
     *            Id of the Video to be uploaded.
     *
     * @return A String indicating the status of the video upload operation.
     */
    public String uploadVideo(Context context,
                              Uri videoUri) throws Exception {
        // Get the path of video file from videoUri.
    	Log.i(TAG,"IN UPLOAD VIDEO of VideoDataMediator");
        String filePath = VideoMediaStoreUtils.getPath(context,
                                                       videoUri);
        Log.i(TAG,"IN VideoDataMediator (VideoUri):" +videoUri );
        Log.i(TAG,"IN VideoDataMediator (Sring filePath):" +filePath );
        // Get the Video from Android Video Content Provider having
        // the given filePath.
        Video androidVideo = 
            VideoMediaStoreUtils.getVideo(context,
                                          filePath);
        
        // Check if any such Video exists in Android Video Content
        // Provider.
        if (androidVideo != null) {
           
        	File videoFile = new File(filePath);
            
            // Check if the file size is less than the size of the
            // video that can be uploaded to the Video Service.
            if (videoFile.length() < Constants.MAX_SIZE_MEGA_BYTE) {
            	
	    	 // Add the metadata of the Video to the Video Service and
	        // get the resulting Video that contains additional
	        // meta-data (e.g., Id and ContentType) generated by the
	        // Video Service.	
            	Log.i(TAG, "before add data URL" + androidVideo.getDataUrl());
            	Video receivedVideo = mVideoServiceProxy.addVideo(androidVideo);
            Log.i(TAG, "recvd data URL" + receivedVideo.getDataUrl());	
            // Check if the Video Service returned any Video metadata.
            if (receivedVideo != null) {
                // Prepare to Upload the Video data.
                              
                // Create an instance of the file to upload.
                
                    // Upload the Video data to the Video Service and get the
                    // status of the uploaded video data.
                    VideoStatus status =
                        mVideoServiceProxy.setVideoData
                            (receivedVideo.getId(),
                             new TypedFile(receivedVideo.getContentType(),
                                           videoFile));

                    // Check if the Status of the Video is ready or not.
                    if (status.getState() == VideoState.READY) 
                        // Video successfully uploaded.
                        return STATUS_UPLOAD_SUCCESSFUL;
                } else 
                	return STATUS_UPLOAD_ERROR;
                   
            }
            // Video can't be uploaded due to large video size.
            return STATUS_UPLOAD_ERROR_FILE_TOO_LARGE;
        }

        // Error occured while uploading the video.
        return STATUS_UPLOAD_ERROR;
    }
    
    public String[] DownloadVideo(Context context,long videoID) throws Exception{
    	
    	Response mResponse = mVideoServiceProxy.getData(videoID);
    	mVideoCursorAdapter = new VideoCursorAdapter(context, null, 0);
    	String mTitle = mVideoCursorAdapter.getVideoTitle((int) videoID);

    	if(mResponse.getStatus() == 200){
    		
    		 try {
				final InputStream inputStream =
						 mResponse.getBody().in();
				Log.i(TAG,"inputStream : " + inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
    		videoDownloadedFile = VideoStorageUtils.
    								storeVideoInExternalDirectory(context,mResponse,mTitle);

    		mVideoCursorAdapter.updateVideoPath(videoDownloadedFile.toString(), mTitle);
    		Log.i(TAG,"videoDownloadedFile in DownloadVideo :"+ videoDownloadedFile);
    		return new String[] {STATUS_DOWNLOAD_SUCCESSFUL,videoDownloadedFile.toString()};
    	
    	}else
    		return new String[] {STATUS_DOWNLOAD_ERROR};
    	
    }

    public String UpdateStarMetadata (Context context,Video video) throws Exception{
    	String status = null;;
    	try{
    		Response mResponse  = mVideoServiceProxy.updateMetadata(video);
    		if(mResponse.equals(222))
    			status = STATUS_UPDATE_SUCCESSFUL;
    	}catch(RetrofitError e){
    		e.printStackTrace();
    		status=STATUS_UPDATE_ERROR;
    	}
    	return status;
    }
  
    
    /**
     * Get the List of Videos from Video Service.
     *
     * @return the List of Videos from Server or null if there is
     *         failure in getting the Videos.
     */
    public List<Video> getVideoList() {
        return (ArrayList<Video>) mVideoServiceProxy.getVideoList();
    }
}
