package com.example.model.contentProvider;

import com.example.assignment3videoupdownclient.R;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This contract defines the metadata for the VideoContentProvider,
 * including the provider's access URIs and its "database" constants.
 */
public final class VideoContract {
    /**
     * This ContentProvider's unique identifier.
     */
    public static final String CONTENT_AUTHORITY =
        "com.example.assignment3";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which
     * apps will use to contact the content provider.
     */
    public static final Uri BASE_CONTENT_URI =
        Uri.parse("content://"
                  + CONTENT_AUTHORITY);

    /**
     * Possible paths (appended to base content URI for possible
     * URI's).  For instance, content://vandy.mooc/character_table/ is
     * a valid path for looking at Character data.  Conversely,
     * content://vandy.mooc/givemeroot/ will fail, as the
     * ContentProvider hasn't been given any information on what to do
     * with "givemeroot".
     */
    public static final String PATH_VIDEO =
        VideoEntry.TABLE_NAME;

    /*
     * Columns
     */

    /**
     * Inner class that defines the table contents of the Hobbit
     * table.
     */
    public static final class VideoEntry implements BaseColumns {
        /**
         * Use BASE_CONTENT_URI to create the unique URI for Acronym
         * Table that apps will use to contact the content provider.
         */
        public static final Uri CONTENT_URI = 
            BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_VIDEO).build();

        /**
         * When the Cursor returned for a given URI by the
         * ContentProvider contains 0..x items.
         */
        public static final String CONTENT_ITEMS_TYPE =
            "vnd.android.cursor.dir/"
            + CONTENT_AUTHORITY
            + "/" 
            + PATH_VIDEO;
            
        /**
         * When the Cursor returned for a given URI by the
         * ContentProvider contains 1 item.
         */
        public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/"
            + CONTENT_AUTHORITY
            + "/" 
            + PATH_VIDEO;

        /**
         * Columns to display.
         */
        public static final String sColumnsToDisplay = 
            new String(
            VideoContract.VideoEntry.COLUMN_TITLE
        	);
    
        /**
         * Resource Ids of the columns to display.
         */
        public static final int[] sColumnResIds = 
            new int[] {
            R.id.VideoTitle
        };

        /**
         * Name of the database table.
         */
        public static final String TABLE_NAME =
            "videolist_table";

        /**
         * Columns to store data.
         */
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_TITLE = "name";
        public static final String COLUMN_DURATION = "video_duration";
        public static final String COLUMN_CONTENT_TYPE = "content_type";
        public static final String COLUMN_DATA_URL = "data_url";
        public static final String COLUMN_RATING_SUM = "star_rating_sum";
        public static final String COLUMN_RATING_COUNT = "star_rating_count";
 
        /**
         * Return a Uri that points to the row containing a given id.
         * 
         * @param id
         * @return Uri
         */
        public static Uri buildUri(Long id) {
            return ContentUris.withAppendedId(CONTENT_URI,
                                              id);
        }
    }
}
