package com.lopa.auctionapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.lopa.auctionapp.SQLiteDB.SQLiteDBHelper;
import com.lopa.auctionapp.adapter.ProductCursorAdapter;
import com.lopa.auctionapp.common.Constants;

public class ProductListActivity extends AppCompatActivity {
    private static final String TAG = "ProductListActivity";
    private ProductCursorAdapter customAdapter;
    private SQLiteDBHelper sqLiteDBHelper;
    private ListView listView;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        sqLiteDBHelper = new SQLiteDBHelper(this);
        Bundle extras = getIntent().getExtras();

        if(extras == null) {
            Log.e(TAG, "No user type intent. Cannot create user");
            return;
        }

        email= extras.getString(Constants.EMAIL);

        listView = (ListView) findViewById(R.id.list_data);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                customAdapter = new ProductCursorAdapter(ProductListActivity.this, sqLiteDBHelper.getAllProductList(email));
                listView.setAdapter(customAdapter);
            }
        });
    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage(Constants.LOG_OUT_POPUP_BODY)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        ProductListActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
