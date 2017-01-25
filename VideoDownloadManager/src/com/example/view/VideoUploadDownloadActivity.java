package com.example.view;

import java.util.List;


import com.example.assignment3videoupdownclient.R;
//import com.example.common.GenericActivity;
import com.example.common.Utils;
import com.example.model.contentProvider.VideoContract;
import com.example.model.contentProvider.VideoContract.VideoEntry;
import com.example.model.mediator.webdata.Video;
import com.example.model.mediator.webdata.VideoDataMediator;
import com.example.model.services.*;
import com.example.utils.*;
import com.example.view2.*;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.content.CursorLoader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class VideoUploadDownloadActivity	extends Activity
											implements UploadVideoDialogFragment.OnVideoSelectedListener,
													   LoaderCallbacks<Cursor>{
	
	
	
	protected final String TAG = getClass().getSimpleName();
	
	private final int REQUEST_VIDEO_CAPTURE = 0;
	
	private final int REQUEST_GET_VIDEO = 1;
	
	private UploadResultReceiver mUploadResultReceiver;
	
	private FloatingActionButton mVideoButton;
	
	private VideoCursorAdapter mVideoCursorAdapter;
	
	private ListView mVideosList;
	
    private Uri mRecordVideoUri;
    
    private static final int LOADER_ID = 100;
    
    private VideoDataMediator mVideoMediator;
    
    String videoTitle ;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Receiver for the notification.
        mUploadResultReceiver =
            new UploadResultReceiver();
        
		mVideosList =
	            (ListView) findViewById(R.id.videoList);
		
		createVideoFButton();
		//startNotification();
		
		getLoaderManager().initLoader(LOADER_ID, null, this);
		mVideosList.setOnItemClickListener(playVideoListener);
		System.setProperty("http.keepAlive", "false");
		 mVideoMediator =
		            new VideoDataMediator(); 
		
	}
	

    /**
     *  Hook method that is called when user resumes activity
     *  from paused state, onPause(). 
     */
    @Override
    protected void onResume() {
        // Call up to the superclass.
        super.onResume();

        // Register BroadcastReceiver that receives result from
        // UploadVideoService when a video upload completes.
        registerReceiver();
    }
    
    /**
     * Register a BroadcastReceiver that receives a result from the
     * UploadVideoService when a video upload completes.
     */
    private void registerReceiver() {
        
        // Create an Intent filter that handles Intents from the
        // UploadVideoService.
        IntentFilter intentFilter =
            new IntentFilter(UploadVideoService.ACTION_UPLOAD_SERVICE_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        // Register the BroadcastReceiver.
        LocalBroadcastManager.getInstance(this)
               .registerReceiver(mUploadResultReceiver,
                                 intentFilter);
    }

    
    /**
     * Hook method that gives a final chance to release resources and
     * stop spawned threads. onDestroy() may not always be called-when
     * system kills hosting process
     */
    @Override
    protected void onPause() {
        // Call onPause() in superclass.
        super.onPause();
        
        // Unregister BroadcastReceiver.
        LocalBroadcastManager.getInstance(this)
          .unregisterReceiver(mUploadResultReceiver);
    }
    
    
	
@SuppressWarnings("deprecation")
	private void createVideoFButton() {
	final DisplayMetrics metrics =
	            getResources().getDisplayMetrics();
	final int position =
	            (metrics.widthPixels / 8) ;
	// Create Floating Action Button using the Builder pattern.
	mVideoButton =
        new FloatingActionButton
        .Builder(this)
        .withDrawable(getResources()
                      .getDrawable(R.drawable.ic_video))
        .withButtonColor(getResources()
                         .getColor(R.color.theme_primary))
        .withGravity(Gravity.BOTTOM | Gravity.END)
        .withMargins(0, 
                     0,
                     position,
                     0)
        .create();

    // Show the UploadVideoDialog Fragment when user clicks the
    // button.
	mVideoButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new UploadVideoDialogFragment().show(getFragmentManager(),
                                                     "uploadVideo");
            }
        });
}


@Override
public void onVideoSelected(UploadVideoDialogFragment.OperationType which) {
    switch (which) {
    case VIDEO_GALLERY:
        // Create an intent that will start an Activity to get
        // Video from Gallery.
        final Intent videoGalleryIntent = 
            new Intent(Intent.ACTION_GET_CONTENT)
            .setType("video/*")
            .putExtra(Intent.EXTRA_LOCAL_ONLY,
                      true);

        // Verify the intent will resolve to an Activity.
        if (videoGalleryIntent.resolveActivity(getPackageManager()) != null) 
            // Start an Activity to get the Video from Video
            // Gallery.
            startActivityForResult(videoGalleryIntent,
                                   REQUEST_GET_VIDEO);
        break;
        
    case RECORD_VIDEO:
        // Create a file to save the video.
        mRecordVideoUri =
            VideoStorageUtils.getRecordedVideoUri
                               (getApplicationContext());  
        
        Log.i(TAG,"RECORD_VIDEO :"  + mRecordVideoUri );
        
        // Create an intent that will start an Activity to get
        // Record Video.
        final Intent recordVideoIntent =
            new Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT,
                      mRecordVideoUri);

        // Verify the intent will resolve to an Activity.
        if (recordVideoIntent.resolveActivity(getPackageManager()) != null) 
            // Start an Activity to record a video.
            startActivityForResult(recordVideoIntent,
                                   REQUEST_VIDEO_CAPTURE);
        break;
    }
}

@Override
public void onActivityResult(int requestCode,
                             int resultCode,
                             Intent data) {
    Uri videoUri = null; 

    // Check if the Result is Ok and upload the Video to the Video
    // Service.
    if (resultCode == Activity.RESULT_OK) {
        // Video picked from the Gallery.
        if (requestCode == REQUEST_GET_VIDEO){
            //videoUri = getRealPathFromURI(getApplicationContext(),data.getData());
        	videoUri = data.getData();
            Log.i(TAG, "videoUri FROM Gallery=" + videoUri );
        // Video is recorded.
        }else if (requestCode == REQUEST_VIDEO_CAPTURE)
            videoUri = mRecordVideoUri;
        	Log.i(TAG, "videoUri FROM Camera=" + videoUri );
          
        if (videoUri != null){
            Utils.showToast(this,"Uploading video"); 
            Log.i(TAG, "videoUri=" + videoUri );
            // Upload the Video.
        	uploadVideo(videoUri);
        }
    }

    // Pop a toast if we couldn't get a video to upload.
    if (videoUri == null)
        Utils.showToast(this,
                        "Could not get video to upload");
}


private OnItemClickListener playVideoListener = new OnItemClickListener() {  
	  public void onItemClick(AdapterView<?> parent, View v, int position,  
			    long id) {
		    String videoPath = null;
			Intent newActivity = new Intent(getApplicationContext(), PlayVideoActivity.class);
			TextView tv = (TextView)v.findViewById(R.id.VideoTitle);
	        String info = tv.getText().toString();
	        Log.i(TAG, "Select item text"+ info);
	        
			try {
				videoPath = mVideoCursorAdapter.getDBVideoUrl(info);
				Log.i(TAG, "Video Path in list item"+ videoPath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String videoPath = videoAdapter.getItem(position).getDataUrl();
			newActivity.putExtra("VIDEOPATH",videoPath);
			newActivity.putExtra("TITLE", info);
			startActivity(newActivity);
	}};
	
	 @Override
     public Loader<Cursor> onCreateLoader(int id, Bundle args) {
         // Define the columns to retrieve
         //String[] projectionFields =  new String[] { VideoContract.VideoEntry.COLUMN_TITLE };
         // Construct the loader
         CursorLoader cursorLoader = new CursorLoader(VideoUploadDownloadActivity.this,
                 VideoContract.VideoEntry.CONTENT_URI, // URI
                 null,  // projection fields
                 null, // the selection criteria
                 null, // the selection args
                 null // the sort order
         );
         // Return the loader for use
         return cursorLoader;
         
     }

     // When the system finishes retrieving the Cursor through the CursorLoader, 
     // a call to the onLoadFinished() method takes place. 
     @Override
     public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
         // The swapCursor() method assigns the new Cursor to the adapter
    	 mVideoCursorAdapter = new VideoCursorAdapter(getApplicationContext(), cursor,0);
    	 if(mVideoCursorAdapter!=null && cursor!=null){    		
    		// mVideoCursorAdapter.swapCursor(cursor); //swap the new cursor in.
    		 mVideosList.setAdapter(mVideoCursorAdapter);
    		 Log.i(TAG,"OnLoadFinish() entered");
    	 }else
    			Log.i(TAG,"OnLoadFinished: mVideoCursorAdapter is null");
     }

     // This method is triggered when the loader is being reset 
     // and the loader data is no longer available. Called if the data 
     // in the provider changes and the Cursor becomes stale.
     @Override
     public void onLoaderReset(Loader<Cursor> loader) {
         // Clear the Cursor we were using with another call to the swapCursor()
    	 if(mVideoCursorAdapter!=null)
    		 mVideoCursorAdapter.swapCursor(null);
    		else
    			Log.i(TAG,"OnLoadFinished: mVideoCursorAdapter is null");
    	   }
     
     
     public void uploadVideo(Uri videoUri){
         // Sends an Intent command to the UploadVideoService.
         getApplicationContext().startService
             (UploadVideoService.makeIntent 
                  (getApplicationContext(),
                   videoUri));
     }
     
     public String getVideoFileNameFromUrl(String path) {
         String[] pathArray = path.split("/");
         return pathArray[pathArray.length - 1];
     }
     
     public String getContentTypeFromUrl(String path) {
         String[] pathArray = path.split("\\.");
         return pathArray[pathArray.length - 1];
     }
     
     private class UploadResultReceiver 
     extends BroadcastReceiver {
  /**
  * Hook method that's dispatched when the UploadService has
  * uploaded the Video.
  */
    	 @Override
    	 public void onReceive(Context context,
                       Intent intent) {
     // Starts an AsyncTask to get fresh Video list from the
     // Video Service.
    		 Log.i(TAG,"INSIDE OnReceive");
    		 String status = intent.getStringExtra("VSTATUS");
    		 if (status.equalsIgnoreCase(VideoDataMediator.STATUS_UPLOAD_SUCCESSFUL)) {
    			 GetVideoListAsync getList = new GetVideoListAsync();
    		 	getList.execute();
    		 } else if (status.equalsIgnoreCase(VideoDataMediator.STATUS_UPLOAD_ERROR_FILE_TOO_LARGE)){
    			 Toast.makeText(context, "Can not upload files > 50 MB", Toast.LENGTH_LONG).show();
    		 } else {
    			 Toast.makeText(context, "Upload failed", Toast.LENGTH_LONG).show();
    		 }
    	 }
     }
     
   /** Gets the VideoList from Server by executing the AsyncTask  **/
     class GetVideoListAsync extends AsyncTask <Void, Void, List<Video>>{
    	 @Override
         public List<Video> doInBackground(Void... params) {
    		 Log.i(TAG,"INSIDE GetVideoListAsync doInBackground");
             return mVideoMediator.getVideoList();
         }
    	 
    	 @Override
         public void onPostExecute(List<Video> videos) {
    		 Log.i(TAG,"INSIDE GetVideoListAsync onPostExecute");
             displayVideoList(videos);
         }
     }  

     /**
      * Display the Videos in ListView.
      * 
      * @param videos
      */
     public void displayVideoList(List<Video> videos) {
         if (videos != null) {
       // deleting the database and inserting all list
        	 getContentResolver().delete(VideoEntry.CONTENT_URI, null, null);
        	 for(int i=0 ; i< videos.size(); i++){ 
        	 		mVideoCursorAdapter.add(videos.get(i));}
 
        	 Utils.showToast(getApplicationContext(),
                     "Videos available from the Video Service");
        	 

        	/* Utils.showToast(getApplicationContext(),
                     "Videos available from the Video Service");*/
         }else {
        	 Utils.showToast(getApplicationContext(),
                     "Please connect to the Video Service");
         }

     }
     

}
