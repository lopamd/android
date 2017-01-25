package vandy.mooc;

import java.io.File;
import java.net.URL;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ProgressDialog;


public class MainActivity extends LifecycleLoggingActivity implements AsyncTaskCompleteListener {
	
	private final String TAG = getClass().getSimpleName();
	private EditText mUrlEditText;
	private Uri mDefaultUrl =
	        Uri.parse("http://www.dre.vanderbilt.edu/~schmidt/robot.png");
	private Uri downloadedImagePath;
	private RetainedFragment mFragment;
	public static final String TAG_RETAINED_FRAGMENT = "retained_fragment";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mUrlEditText = (EditText) findViewById(R.id.url);
   
		if(savedInstanceState == null){
			mFragment = new RetainedFragment();
			getFragmentManager().beginTransaction().add(mFragment, TAG_RETAINED_FRAGMENT).commit();
			
		}else{
			mFragment = (RetainedFragment)getFragmentManager().findFragmentByTag(TAG_RETAINED_FRAGMENT);	
		}
		
	}
	
	/*Interface methods*/
	
	@Override
    public void onDownloadTaskStarted() {
		mFragment.isDownloadTaskRunning = true;
		if(mFragment.mDownloadImageAsync != null){
			mFragment.progressDialog = ProgressDialog.show(this, "Downloading", "Please wait a moment!");	 	
		}
    }
	
	public void onDownloadTaskCompleted(Uri result){
		mFragment.mDownloadImageAsync= null;
		mFragment.beginFilterTask(result);
		mFragment.isDownloadTaskRunning = false;		
	}
	
	@Override
    public void onFilterTaskStarted() {
		mFragment.isFilterTaskRunning = true;
		if((mFragment.mFilterImageAsync != null) && (mFragment.progressDialog != null)){
			if(mFragment.progressDialog.isShowing()){
				mFragment.progressDialog.dismiss();
				mFragment.progressDialog = ProgressDialog.show(this, "Filtering", "Please wait a moment!");
			} 	 	
		}
		
    }
	
	public void onFilterTaskCompleted(Uri result) {
		
		Intent gallery = null;
		downloadedImagePath = result;
        if (downloadedImagePath != null){
        	gallery = makeGalleryIntent(downloadedImagePath.toString());
        }
       
        if (gallery != null) {
        	startActivity(gallery);
        }
        if((mFragment.mFilterImageAsync != null) && (mFragment.progressDialog != null)){
          	 if (mFragment.progressDialog.isShowing()) {
          		mFragment.progressDialog.dismiss();
               }
          }
        mFragment.mFilterImageAsync = null;
        mFragment.isFilterTaskRunning = false;
    }
	
	/*Method to be called on download button pressed*/
	
	public void downloadImageActivity(View view){
		try{
		//* Hide the keyboard.
            hideKeyboard(this,
                         mUrlEditText.getWindowToken());
            
      
            Log.d(TAG, "Starting image download");
            Uri imageUri = getUrl();
            if(imageUri!=null){
            	mFragment.beginDownloadTask(imageUri);
            }

        } catch (Exception e) {
            e.printStackTrace();}
		}


    /**
     * Factory method that returns an implicit Intent for viewing the
     * downloaded image in the Gallery app.
     */
    private Intent makeGalleryIntent(String pathToImageFile) {
    	Log.d(TAG, "Downloaded Image Path "+pathToImageFile);
    	Intent galleryIntent = new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(new File(pathToImageFile)), "image/*");;
        return galleryIntent;
    }

    /**
     * Get the URL to download based on user input.
     */
    protected Uri getUrl() {
        Uri url = null;

        // Get the text the user typed in the edit text (if anything).
        url = Uri.parse(mUrlEditText.getText().toString());

        // If the user didn't provide a URL then use the default.
        String uri = url.toString();
        if (uri == null || uri.equals(""))
            url = mDefaultUrl;

        // Do a sanity check to ensure the URL is valid, popping up a
        // toast if the URL is invalid.
        // @@ TODO XXX -- you fill in here, replacing "true" with the
        // proper code.
        if (URLUtil.isValidUrl(url.toString()) && isValidUri(uri))
            return url;
        else {
            Toast.makeText(getApplicationContext(), "Invalid Image URL",
                           Toast.LENGTH_SHORT).show();
            return null;
        } 
    }

	/**
	 * Helper ---
	 * @param uri
	 * @return true||false
	 */
	private boolean isValidUri(String uri) {
		URL urlTest = null;
        boolean validUri = true;
        try {
        	urlTest = new URL(uri);
        } catch (Exception ex) {
        	validUri = true;
        }
		return validUri;
	}

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the url.
     */
    public void hideKeyboard(Activity activity,
                             IBinder windowToken) {
        InputMethodManager mgr =
            (InputMethodManager) activity.getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,0);
    }
	
}
