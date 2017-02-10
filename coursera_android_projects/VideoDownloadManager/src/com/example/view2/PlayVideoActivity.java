package com.example.view2;

import java.io.File;
import java.util.List;

import com.example.assignment3videoupdownclient.R;
import com.example.model.mediator.webdata.*;
import com.example.model.services.DownloadVideoService;
import com.example.view.VideoCursorAdapter;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;
import android.graphics.PorterDuff;




public class PlayVideoActivity extends Activity
							   implements OnRatingBarChangeListener{
	
	private String pathname;
	private String title;
	private int videoID;
	private VideoCursorAdapter mVideoCursorAdapter;
	private VideoDataMediator mVideoDataMediator;
	MediaController media_Controller;
	VideoView mVideoView;
	ImageView mImageView;
	ImageButton playButton;
	ImageButton downloadButton1;
	//ImageButton downloadButton2;
	TextView videoTitle;
	RatingBar mRatingBar;
	
	protected final String TAG = getClass().getSimpleName();
	private DownloadResultReceiver mDownloadResultReceiver;
	String physicalPath;
	String downloadedPath;
	int flag=1;
	boolean playButtonFlag=false;
	
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.videothumbnail);
    mVideoView = (VideoView)findViewById(R.id.video_view);
    mImageView = (ImageView)findViewById(R.id.image_view);
    playButton = (ImageButton)findViewById(R.id.play_button);
    downloadButton1 = (ImageButton) findViewById(R.id.download_button1);
    videoTitle = (TextView) findViewById(R.id.videoName);
    
    mVideoDataMediator = new VideoDataMediator();
    mVideoCursorAdapter = new VideoCursorAdapter(getApplicationContext(), null, 0);
    
    mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
    LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
    stars.getDrawable(2).setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_ATOP);
    mRatingBar.setOnRatingBarChangeListener(this);
    
    
    media_Controller = new MediaController(this);
    

    Intent i = getIntent();
    Bundle extras = i.getExtras();
    pathname = extras.getString("VIDEOPATH");
    title = extras.getString("TITLE");
    videoTitle.setText(title);
    File vfile = new File(pathname);
    if(!vfile.exists()){
    	
    	Toast.makeText(getApplicationContext(), "Video not found.Download to Play!!", Toast.LENGTH_LONG).show();
    	downloadButton1.setImageResource(R.drawable.ic_action_download);
    	downloadButton1.setClickable(true);
    	downloadButton1.setOnClickListener(downloadListener);
    	playButton.setEnabled(false);
    	
		
    }
    setRating();
    setThumbnail(pathname);
   
 // Receiver for the notification after download.
    mDownloadResultReceiver =
        new DownloadResultReceiver();
    
    playButton.setOnClickListener(new OnClickListener() {	
		@Override
		public void onClick(View v) {
			if(!playButtonFlag){
			mImageView.setVisibility(View.GONE);
			mVideoView.setVisibility(View.VISIBLE);
		    mVideoView.setMediaController(media_Controller);
		    playButton.setVisibility(View.GONE);
		    mVideoView.setVideoURI(Uri.parse(pathname));
		    Log.i(TAG, "video path in sd :"+ pathname);
		    downloadButton1.setVisibility(View.GONE);
		    mVideoView.start();
		    
		    mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
	        {           
	            public void onCompletion(MediaPlayer mp) 
	            {
	                playButton.setVisibility(View.VISIBLE);

	            }           
	        }); 

			}else{
				mImageView.setVisibility(View.GONE);
				mVideoView.setVisibility(View.VISIBLE);
			    mVideoView.setMediaController(media_Controller);
			    playButton.setVisibility(View.GONE);
			    mVideoView.setVideoURI(Uri.parse(downloadedPath));
			    Log.i(TAG, "video path in sd :"+ downloadedPath);
			    downloadButton1.setVisibility(View.GONE);
			    mVideoView.start();
			    
			    mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
		        {           
		            public void onCompletion(MediaPlayer mp) 
		            {
		                playButton.setVisibility(View.VISIBLE);

		            }   
		        });
			}
		    	   
		}
	});    
    
}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}
	private void setRating(){
		 Video mVideo = new Video();
		    mVideo = mVideoCursorAdapter.getMetaData(title, mVideo);
		    float rating = mVideo.getStarRatingSum()/mVideo.getStarRatingCount();
		    mRatingBar.setRating(rating);
	}
	
	
	private void setThumbnail(String path){
		 Bitmap bmThumbnail;
		    bmThumbnail = ThumbnailUtils.createVideoThumbnail(path, Thumbnails.MINI_KIND);
		    mImageView.setImageBitmap(bmThumbnail);
	}
	
	private OnClickListener downloadListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if(flag==1){
				downloadButton1.setEnabled(false);
				downloadButton1.setImageResource(R.drawable.ic_disable_download_icon);
				videoID = mVideoCursorAdapter.getVideoId(title);
				downloadVideo((long) videoID);
				Log.i(TAG, "Downloading the file : " + videoID);
			}
			flag=0;	
			
		}
		
	};
	
	public void downloadVideo(Long videoid){
	    // Sends an Intent command to the UploadVideoService.
	    getApplicationContext().startService
	        (DownloadVideoService.makeIntent 
	             (getApplicationContext(),
	            		 videoid));
	}
	
	@Override
	 public void onRatingChanged(RatingBar ratingBar, float rating,
	   boolean fromTouch) {
	   //int numStars = ratingBar.get;
		if (fromTouch) {
			Video v = new Video();
			Log.i(TAG,"Star Rating Changed Testing" + rating);
			v = mVideoCursorAdapter.getMetaData(title, v);
			float sum = v.getStarRatingSum() + rating;
			float count = v.getStarRatingCount() + 1;
			float avgRating = sum/count;
			Log.i(TAG,"Avg rating" + avgRating);
			//ratingBar.setProgress((int) avgRating);
			ratingBar.setRating(avgRating);
			
			v.setStarRatingSum(sum);
			v.setStarRatingCount(count);
			mVideoCursorAdapter.updateStarRating(title, v);
			
			UpdateRatingAsync rateAsync = new UpdateRatingAsync();
			rateAsync.execute(v);
		}			
	 }
	
	
	/** Gets the VideoList from Server by executing the AsyncTask  **/
    class UpdateRatingAsync extends AsyncTask <Video, Void, Void>{
    	
		@Override
		protected Void doInBackground(Video... params) {
			// TODO Auto-generated method stub
			Log.i(TAG,"INSIDE doInBackground UpdateRatingAsync: "+ ((Video)params[0]).getStarRatingSum());
			 	try {
					mVideoDataMediator.UpdateStarMetadata(getApplicationContext(), (Video)params[0]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 	return null;
		}

   	 public Void onPostExecute(List<Video> videos) {
   		 Log.i(TAG,"INSIDE UpdateRatingAsync onPostExecute");
            
   		 return null;
        }	
    }  
    
    
    @Override
    protected void onResume() {
        // Call up to the superclass.
        super.onResume();

        // Register BroadcastReceiver that receives result from
        // DownloadVideoService when a video download completes.
        registerReceiver();
    }
    
 private void registerReceiver() {
        
        // Create an Intent filter that handles Intents from the
        // DownloadVideoService.
        IntentFilter intentFilter =
            new IntentFilter(DownloadVideoService.ACTION_DOWNLOAD_SERVICE_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        // Register the BroadcastReceiver.
        LocalBroadcastManager.getInstance(this)
               .registerReceiver(mDownloadResultReceiver,
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
       .unregisterReceiver(mDownloadResultReceiver);
 }
 
 private class DownloadResultReceiver 
 extends BroadcastReceiver {

	 @Override
	 public void onReceive(Context context,
                   Intent intent) {
		 Log.i(TAG,"INSIDE OnReceive DownloadResultReceiver");
		 Toast.makeText(getApplicationContext(), "Video Download Successful",Toast.LENGTH_SHORT).show();
		 downloadedPath = intent.getStringExtra("DOWNLOADED_PATH");
		 Log.i(TAG,"INSIDE OnReceive downloadedPath: "+ downloadedPath);
		 mImageView.setVisibility(View.VISIBLE);
		 setThumbnail(downloadedPath);
		 mVideoCursorAdapter.updateVideoPath(downloadedPath, title);
		 playButton.setEnabled(true);
		 downloadButton1.setVisibility(View.GONE);
		 playButtonFlag=true;
	 }
 }
  

}