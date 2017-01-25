package vandy.mooc;


import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.content.Context;


    public final class DownloadImageAsync extends AsyncTask <Uri, Integer, Uri> {
    	
    	private AsyncTaskCompleteListener callback;
    	private final String TAG = getClass().getSimpleName();
    	private ProgressDialog progressDialog;
    	private Context mContext;
    	private Activity activity;
    	
    	public DownloadImageAsync(Context context , AsyncTaskCompleteListener cb, Activity activity) {
    		super();
    		this.mContext = context; 
    		this.callback = cb;
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
        	 Log.d(TAG,"In DownloadingImage's onPreExecute()");
        	 if(callback != null){
        		 callback.onDownloadTaskStarted();
        	 }
       }

        @Override
        protected Uri doInBackground(Uri... uri) {
        	Log.d(TAG,"Downloading Image" + uri[0]);
        	return Utils.downloadImage(mContext, uri[0]);
        }


        @Override
        protected void onPostExecute(Uri result) {
        	super.onPostExecute(result);
        	 Log.d(TAG,"In DownloadingImage's onPostExecute()");
        	 if(callback != null)
        	 callback.onDownloadTaskCompleted(result); 	 
        }

    }

