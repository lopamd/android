package vandy.mooc;

import android.net.Uri;

public interface AsyncTaskCompleteListener {
		public void onDownloadTaskStarted();
		public void onDownloadTaskCompleted(Uri result);
		public void onFilterTaskStarted();
		public void onFilterTaskCompleted(Uri result);
}
