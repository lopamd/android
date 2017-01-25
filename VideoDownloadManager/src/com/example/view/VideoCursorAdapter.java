package com.example.view;

import com.example.assignment3videoupdownclient.R;
import com.example.model.contentProvider.VideoContract.VideoEntry;
import com.example.model.mediator.webdata.Video;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VideoCursorAdapter extends CursorAdapter {
 
 private LayoutInflater mInflater;
 protected final String TAG = getClass().getSimpleName();
 private static Context mContext;

 public VideoCursorAdapter(Context context, Cursor c, int flags) { 
	 super(context, c, flags);
	 mContext = context;
	 mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 }
 


@Override
 public void bindView(View view, Context context, Cursor cursor) {
	Log.i("TAG","CursorAdapter BindView");
	String title = null;
	if(null!=cursor){
	    Log.i("TAG","CursorAdapter BindView:Cursor not null");
	    title = cursor.getString( cursor.getColumnIndex( VideoEntry.COLUMN_TITLE ) );
	    TextView textViewTitle = (TextView) view.findViewById(R.id.VideoTitle);     
	    textViewTitle.setText(title);
	}else
		Log.i("TAG","CursorAdapter BindView:Cursor NULL");
 
 }
 
 @Override
 public View newView(Context context, Cursor cursor, ViewGroup parent) {
	 return mInflater.inflate(R.layout.video_list_item, parent, false);
 }
 
 public String getDBVideoUrl(String title) throws Exception{
 	String videoDBPath = null;
 	Cursor cs = null;
 	final String SELECTION_VIDEO = 
             VideoEntry.COLUMN_TITLE
             + " = ?";
     
     String[] selectionArgs = {title};
     String[] projection = {VideoEntry.COLUMN_DATA_URL};
     try {
     cs = mContext.getContentResolver().query(VideoEntry.CONTENT_URI, projection, SELECTION_VIDEO, selectionArgs, null);
     
	        if(cs.moveToFirst()){
	        	videoDBPath = cs.getString(cs.getColumnIndex(VideoEntry.COLUMN_DATA_URL));
	        	Log.i(TAG,"videoDBPath="+ videoDBPath);
	        }
     }
     finally {
         try {
             cs.close();
         }
         catch(Exception ignore) {
         }
     }
     return videoDBPath;
 }

 public void insertDBVideoMetaData(Video video){
 	ContentValues cv = new ContentValues();
 	
 	cv.put(VideoEntry.COLUMN_VIDEO_ID,video.getId());
 	cv.put(VideoEntry.COLUMN_TITLE,video.getTitle());
 	cv.put(VideoEntry.COLUMN_DURATION,video.getDuration());
 	cv.put(VideoEntry.COLUMN_CONTENT_TYPE,video.getContentType());
 	cv.put(VideoEntry.COLUMN_DATA_URL,video.getDataUrl());	
 	cv.put(VideoEntry.COLUMN_RATING_SUM,video.getStarRatingSum());
  	cv.put(VideoEntry.COLUMN_RATING_COUNT,video.getStarRatingCount());
 	try {
 		mContext.getContentResolver().insert(VideoEntry.CONTENT_URI,cv);
     	
		} catch (SQLException s) {
			Log.i(TAG, s.getMessage());
			// TODO: handle exception
		}
 	
 }
 /**
  * Adds a Video to the Adapter and notify the change.
  */
 public void add(Video video) {
 	//TODO: Verify insert 
	 insertDBVideoMetaData(video);
     notifyDataSetChanged();
 }

 /**
  * Removes a Video from the Adapter and notify the change.
  */
 public void remove(Video video) {
     //videoList.remove(video);
     notifyDataSetChanged();
 }

 /**
  * Get the List of Videos from Adapter.
  */
 /*public List<Video> getVideos() {
     return videoList;
 }*/

 /**
  * Set the Adapter to list of Videos.
  */
 /*public void setVideos(List<Video> videos) {
     this.videoList = videos;
     notifyDataSetChanged();
 }

 *//**
  * Get the no of videos in adapter.
  *//*
 public int getCount() {
     return videoList.size();
 }*/

 /**
  * Get video from a given position.
  */
 /*public Video getItem(int position) {
     return videoList.get(position);
 }*/
 /**
  * Get Id of video from a given position.
  */
 public String getVideoTitle(int videoid) {
	 Cursor cs = null;
	 String cpTitle = null;
	 	final String SELECTION_VIDEO = 
	             VideoEntry.COLUMN_VIDEO_ID
	             + " = ?";
	     
	     String[] selectionArgs = {Integer.toString(videoid)};
	    // String[] projection = {VideoEntry.COLUMN_DATA_URL};
	     try {
	    	 cs = mContext.getContentResolver().query(VideoEntry.CONTENT_URI, null, SELECTION_VIDEO, selectionArgs, null);
	     
		     if(cs.moveToFirst()){
		    	 cpTitle = cs.getString(cs.getColumnIndex(VideoEntry.COLUMN_TITLE));
		     }
	     }
	     finally {
	         try {
	             cs.close();
	         }
	         catch(Exception ignore) {
	         }
	     }
	     notifyDataSetChanged();   
	     return cpTitle;
	     
 }
 

 /**
  * Get Id of video from a given position.
  */
 public int getVideoId(String videoTitle) {
	 Cursor cs = null;
	 int cpID = 0 ;
	 	final String SELECTION_VIDEO = 
	             VideoEntry.COLUMN_TITLE
	             + " = ?";
	     
	     String[] selectionArgs = {videoTitle};
	    // String[] projection = {VideoEntry.COLUMN_DATA_URL};
	     try {
	    	 cs = mContext.getContentResolver().query(VideoEntry.CONTENT_URI, null, SELECTION_VIDEO, selectionArgs, null);
	     
		     if(cs.moveToFirst()){
		    	 cpID = cs.getInt(cs.getColumnIndex(VideoEntry.COLUMN_VIDEO_ID));
		     }
	     }
	     finally {
	         try {
	             cs.close();
	         }
	         catch(Exception ignore) {
	         }
	     }
	     notifyDataSetChanged();   
	     return cpID;
	     
 }
 
 public void updateVideoPath(String videopath, String title){

	 	final String SELECTION_VIDEO = 
	             VideoEntry.COLUMN_TITLE
	             + " = ?";
	 	String[] selectionArgs = {title};
	     
	    ContentValues cv = new ContentValues();
	  		cv.put(VideoEntry.COLUMN_DATA_URL,videopath);

	  	try{
	  		mContext.getContentResolver().update(VideoEntry.CONTENT_URI, cv, SELECTION_VIDEO, selectionArgs);
	  	} catch (SQLException s) {
	  		Log.i(TAG, s.getMessage());
	  		// TODO: handle exception
	  	  }
	    notifyDataSetChanged();   
 }
 public Video getMetaData(String title, Video v) {
	 Cursor cs = null;
	 int cpvideoID = 0 ;
	 float cpstarRatingSum = 0;
	 float cpstarRatingCount = 0;
	 	final String SELECTION_VIDEO = 
	             VideoEntry.COLUMN_TITLE
	             + " = ?";
	     
	     String[] selectionArgs = {title};
	    // String[] projection = {VideoEntry.COLUMN_DATA_URL};
	     try {
	    	 cs = mContext.getContentResolver().query(VideoEntry.CONTENT_URI, null, SELECTION_VIDEO, selectionArgs, null);
	     
		     if(cs.moveToNext()){
		    	 cpvideoID = cs.getInt(cs.getColumnIndex(VideoEntry.COLUMN_VIDEO_ID));
		    	 cpstarRatingSum = cs.getFloat(cs.getColumnIndex(VideoEntry.COLUMN_RATING_SUM));
		    	 cpstarRatingCount = cs.getFloat(cs.getColumnIndex(VideoEntry.COLUMN_RATING_COUNT));
		     }
		     v.setId(cpvideoID);
		     v.setStarRatingSum(cpstarRatingSum);
		     v.setStarRatingCount(cpstarRatingCount);
	     }
	     finally {
	         try {
	             cs.close();
	         }
	         catch(Exception ignore) {
	         }
	     }
	     notifyDataSetChanged();   
	     return v; 
	 
 }
 
 public void updateStarRating(String title, Video v){
	 final String SELECTION_VIDEO = 
             VideoEntry.COLUMN_TITLE
             + " = ?";
 	String[] selectionArgs = {title};
     
    ContentValues cv = new ContentValues();
  		cv.put(VideoEntry.COLUMN_RATING_SUM,v.getStarRatingSum());
  		cv.put(VideoEntry.COLUMN_RATING_COUNT, v.getStarRatingCount());

  	try{
  		mContext.getContentResolver().update(VideoEntry.CONTENT_URI, cv, SELECTION_VIDEO, selectionArgs);
  	} catch (SQLException s) {
  		Log.i(TAG, s.getMessage());
  		// TODO: handle exception
  	  }
    notifyDataSetChanged(); 
	 
 }
}