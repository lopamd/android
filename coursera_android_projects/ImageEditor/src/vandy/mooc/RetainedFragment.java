package vandy.mooc;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;



public class RetainedFragment extends Fragment{
	
	private AsyncTaskCompleteListener mAsyncTaskCompleteListener;
    ProgressDialog progressDialog;
    private Activity activity;
    DownloadImageAsync mDownloadImageAsync;
    FilterImageAsync mFilterImageAsync;
    private static final String TAG = "RetainedFragment";
    boolean isDownloadTaskRunning = false;
    boolean isFilterTaskRunning = false;
   
    
    
    public void beginDownloadTask(Uri... args){
    	if(mDownloadImageAsync == null){
    		mDownloadImageAsync = new DownloadImageAsync(getActivity().getApplicationContext() , mAsyncTaskCompleteListener, activity);
    		mDownloadImageAsync.execute(args);
    		isDownloadTaskRunning = true;
    	}else
    		Log.i(TAG,getClass().getSimpleName() + "DownloadImageAsync already running");
    	
    }
    
    public void beginFilterTask(Uri... args){
    	if(mFilterImageAsync == null){
    		mFilterImageAsync = new FilterImageAsync(getActivity().getApplicationContext() , mAsyncTaskCompleteListener, activity);
    		mFilterImageAsync.execute(args);
    		isFilterTaskRunning = true;
  
    	}else
    		Log.i(TAG,getClass().getSimpleName() + "FilterImageAsync already running");
    	
    }

    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	Log.i(TAG, getClass().getSimpleName() + ":entered onAttach()");
    	this.activity =  activity;
    	mAsyncTaskCompleteListener = (AsyncTaskCompleteListener) activity;
    	if(mDownloadImageAsync != null){
    		mDownloadImageAsync.onAttach(activity);
    	}
    	if(mFilterImageAsync != null){
    		mFilterImageAsync.onAttach(activity);
    	}
    	
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);        
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        // If we are returning here from a screen orientation
        // and the AsyncTask is still working, re-create and display the
        // progress dialog.
        if (isDownloadTaskRunning) {
        	progressDialog = ProgressDialog.show(getActivity(), "Downloading", "Please wait a moment!");
        }
        
        if(isFilterTaskRunning) {
        	progressDialog = ProgressDialog.show(getActivity(), "Filtering", "Please wait a moment!");
        }
    }
 
 
    @Override
    public void onDetach() {
        // All dialogs should be closed before leaving the activity in order to avoid
        // the: Activity has leaked window com.android.internal.policy... exception
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        
        Log.i(TAG, getClass().getSimpleName() + ":entered onDetach()");
        mAsyncTaskCompleteListener = null;
        if(mDownloadImageAsync != null){
        	mDownloadImageAsync.onDetach();
        }
        if(mFilterImageAsync != null){
        	mFilterImageAsync.onDetach();
        }
        super.onDetach();
    }
}