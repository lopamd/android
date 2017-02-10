package vandy.mooc;


import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.content.Context;


    public final class FilterImageAsync extends AsyncTask <Uri, Integer, Uri> {
    	
    	private AsyncTaskCompleteListener callback;
    	private final String TAG = getClass().getSimpleName();
    	private Context mContext;
    	private Activity activity;
    	
    	
    	public FilterImageAsync(Context context , AsyncTaskCompleteListener cb, Activity activity) {
    		super();
    		mContext = context; 
    		callback = cb;
    		this.activity = activity;
            onAttach(activity);
    	}
    	
    	public void onAttach(Activity activity){
    		this.activity = activity;	
    	}
    	
    	public void onDetach(){
    		this.activity = null;	
    	}
  
        @Override
        protected void onPreExecute() {
        	 super.onPreExecute();
        	 Log.d(TAG,"In FilterImage's onPreExecute()");
        	 
        	 if(callback != null){
        		 callback.onFilterTaskStarted();
        	 }
        	 else
        		 Log.d(TAG,"callback.onFilterTaskStarted() is null");  	 
        }

        @Override
        protected Uri doInBackground(Uri... uri) {
        	Log.d(TAG,"Filtering Image" + uri[0]);
        	return Utils.grayScaleFilter(mContext, uri[0]);
        }

        @Override
        protected void onPostExecute(Uri result) {
        	super.onPostExecute(result);
        	Log.d(TAG,"In FilterImage's onPostExecute()");
        	if(callback != null){
        	callback.onFilterTaskCompleted(result);
        	}
        }
        
    }

