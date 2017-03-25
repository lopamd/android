package com.lopa.auctionapp.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lopa.auctionapp.common.Constants;
import com.lopa.auctionapp.model.Product;
import com.lopa.auctionapp.model.User;

public class SQLiteDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteDBHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ecommerceDb";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCT = "product";
    //user columns
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_USERTYPE = "userType";
    //product columns
    private static final String KEY_PRODUCT_ID = "_id";
    private static final String KEY_PRODUCT_NAME = "productName";
    private static final String KEY_PRODUCT_USERTYPE = "userType";
    private static final String KEY_PRODUCT_EMAIL = "email";

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY " + "AUTOINCREMENT ,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_MOBILE + " TEXT,"
                + KEY_USERTYPE + " INTEGER " +")";

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY " + "AUTOINCREMENT ,"
                + KEY_PRODUCT_NAME + " TEXT ,"
                + KEY_PRODUCT_EMAIL + " TEXT,"
                + KEY_PRODUCT_USERTYPE + " INTEGER )";

        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    // code to add the new user
    public long addUser(User user) {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getUserName());
        values.put(KEY_EMAIL, user.getEmailId());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_MOBILE, user.getMobile());
        values.put(KEY_USERTYPE, user.getUserType());

        // Inserting Row
        result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result;
    }

    public boolean searchUser(String email, int userType) {
        boolean found = false;
        int user_Type = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String squery = "SELECT " + KEY_USERTYPE +  " FROM " + TABLE_USERS+ " WHERE " +KEY_EMAIL+ " =\'" +email+"\' AND "+KEY_USERTYPE+" =" + userType;
        Log.i(TAG,"Search Query "+squery);
        Cursor cursor = db.rawQuery(squery,null);
        if (cursor != null && cursor.getCount() > 0) {
            if(cursor.moveToFirst()){
                user_Type = cursor.getInt(cursor.getColumnIndex(KEY_USERTYPE));
                Log.i(TAG,"user type from searchuser : " + user_Type);
            }
            found = true;
            Log.i(TAG,"Email "+email+ " already registered."+cursor.getCount());
        }
        return found;
    }

    /**
     * Get the user type from email address.
     */
    public int getUserType(String email) {
        boolean found = false;
        int user_type = Constants.UTYPE_INVALID;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_USERTYPE + " FROM " + TABLE_USERS + " WHERE " + KEY_EMAIL+ " = \'" + email + "\'";

        Log.i(TAG, "Query:"+selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            user_type = cursor.getInt(cursor.getColumnIndex(KEY_USERTYPE));
            Log.i(TAG,"userType from DB: "+user_type );
        }
        return user_type;

    }
    /**
     * Validate user from the database.
     */
    public boolean user_validation(String email, String pass){
        boolean found = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String squery = "SELECT * FROM " + TABLE_USERS+ " WHERE " +KEY_EMAIL+ " =\'" +email+"\' AND "
                        + KEY_PASSWORD +" =\'" +pass+ "\'";
        Log.i(TAG,"Search Query "+squery);
        Cursor cursor = db.rawQuery(squery,null);
        if (cursor != null && cursor.getCount() > 0) {
            found = true;
            cursor.moveToFirst();
            String username = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            Log.i(TAG,"User "+email+ " is a registered user : "+ username);
        }
        Log.i(TAG,"User "+email+ " is a not registered user.");
        return found;
    }
    /**
     *  Add the product to the database.
     */

    public long addProduct(Product product) {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, product.getProductName());
        values.put(KEY_PRODUCT_EMAIL, product.getEmailId());
        values.put(KEY_PRODUCT_USERTYPE, product.getUserType());

        // Inserting Row
        result = db.insert(TABLE_PRODUCT, null, values);
        Log.i(TAG,"The product inserted:"+product.getProductName());
        db.close();

        return result;
    }

    //Updating a todo

    public int updateUser(User user, int userTypeToUpdate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //String updateQuery = "UPDATE "
        //String strFilter = "KEY_EMAIL = " + user.getEmailId() + " AND KEY_PRODUCT_USERTYPE = " + product.getUserType();

        values.put(KEY_NAME, user.getUserName());
        values.put(KEY_EMAIL, user.getEmailId());
        values.put(KEY_MOBILE, user.getMobile());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_USERTYPE, userTypeToUpdate);

        // updating row
        String filter = KEY_EMAIL + " = \'" + user.getEmailId() + "\' AND " + KEY_USERTYPE + " = " + user.getUserType();
        Log.i(TAG, "Updating the User for filter "+filter);
        return db.update(TABLE_USERS, values, filter, null);
    }

    public void deleteProduct(String email, int userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String strFilter = "KEY_PRODUCT_EMAIL = \'" + email + "\' AND KEY_PRODUCT_USERTYPE = " + userType;

        db.delete(TABLE_PRODUCT, KEY_PRODUCT_EMAIL + " = \'" + email + "\' AND " + KEY_PRODUCT_USERTYPE +" = " +userType, null);
    }

    public Cursor getAllProductList(String email){
        Cursor cursor;
        String selectQuery = "SELECT " + KEY_PRODUCT_ID + " , "+ KEY_PRODUCT_NAME + " FROM " + TABLE_PRODUCT + " WHERE NOT " + KEY_EMAIL+ " = \'" + email + "\'";

        Log.i(TAG, "Query:"+selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.rawQuery(selectQuery, null);
        Log.i(TAG, "Cursor values : "+cursor);
        return cursor;

    }

}


