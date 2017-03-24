package com.lopa.auctionapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lopa.auctionapp.SQLiteDB.SQLiteDBHelper;
import com.lopa.auctionapp.Utility.ListScrollViewHelper;
import com.lopa.auctionapp.adapter.ProductCursorAdapter;
import com.lopa.auctionapp.common.Constants;
import com.lopa.auctionapp.model.Product;

public class SellerHomePageActivity extends AppCompatActivity {
    private static final String TAG = "SellerHomePage_Activity";
    EditText _num_fields;
    Button _btn_select,_btn_submit,_btn_update;
    LinearLayout _layout2;
    RelativeLayout _layout3;
    LinearLayout sellerBuyerLayout;
    EditText[] myDynamicTextValues;
    int userType;
    String email;
    SQLiteDBHelper sqLiteDBHelper;
    private ListView listView;
    private ProductCursorAdapter customAdapter;
    InputMethodManager imm;
    private boolean firstUpdate = true;



    //SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home_page);

        final Bundle extras = getIntent().getExtras();

        if(extras == null) {
            Log.e(TAG, "No user type and email in the intent. Cannot create product");
            return;
        }

        userType= extras.getInt(Constants.UTYPE);
        email = extras.getString(Constants.EMAIL);

        _num_fields = (EditText)findViewById(R.id.fieldNum);
        _btn_select = (Button)findViewById(R.id.btn_select);
        _layout2 = (LinearLayout)findViewById(R.id.linerlayout2);
        _layout3 =(RelativeLayout)findViewById(R.id.Rlayout3);
        _btn_submit = (Button)findViewById(R.id.btn_submit);
        _btn_update = (Button)findViewById(R.id.btn_update);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        sellerBuyerLayout = (LinearLayout) findViewById(R.id.activity_product_list);
        if(userType == Constants.UTYPE_BOTH) {
            sellerBuyerLayout.setVisibility(View.VISIBLE);
            sqLiteDBHelper = new SQLiteDBHelper(this);

            listView = (ListView) findViewById(R.id.list_data);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    customAdapter = new ProductCursorAdapter(SellerHomePageActivity.this, sqLiteDBHelper.getAllProductList(email));
                    listView.setAdapter(customAdapter);
                    ListScrollViewHelper.setListViewHeightBasedOnChildren(listView);
                }
            });
        }

        sqLiteDBHelper = new SQLiteDBHelper(this);

        _btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _layout2.removeAllViewsInLayout();

                int num;
                if (!_num_fields.getText().toString().equals("")) {
                    num = Integer.parseInt(_num_fields.getText().toString());
                    myDynamicTextValues= new EditText[num];

                    int count = 1;
                    for (int i = 0; i < num; i++) {
                        EditText myEditText = new EditText(SellerHomePageActivity.this);
                        myEditText.setHint(count++ + "." + Constants.ENTER_PRODUCT_HINT);
                        _layout2.addView(myEditText);
                        myDynamicTextValues[i] = myEditText;
                    }
                    _btn_update.setVisibility(View.GONE);
                    _btn_submit.setVisibility(View.VISIBLE);

                } else{
                    _num_fields.setError(Constants.FIELD_BLANK_MSG);
                }
            }
        });

        _btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long result = Constants.DEFAULT_ERROR;
                firstUpdate = true;
                if(myDynamicTextValues != null){
                    for(int i = 0; i < myDynamicTextValues.length; i++) {
                        String product = myDynamicTextValues[i].getText().toString();
                        if (!product.equals("")) {
                            result = sqLiteDBHelper.addProduct(new Product(product,userType,email));
                            if(result == Constants.DB_INSERT_ERROR) {
                                Log.e(TAG, "Error Inserting the product "+myDynamicTextValues[i].getText().toString()+ "into DB.");
                            }else {
                                myDynamicTextValues[i].setEnabled(false);

                            }
                        }
                    }
                    if (result != Constants.DEFAULT_ERROR) {
                        Toast.makeText(getApplicationContext(),Constants.TOAST_ISUBMITTED,Toast.LENGTH_SHORT).show();
                        _btn_update.setVisibility(View.VISIBLE);
                        _btn_submit.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(getApplicationContext(),Constants.TOAST_SUBMIT_NOPRODUCT,Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        _btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long result = Constants.DEFAULT_ERROR;
                if(myDynamicTextValues != null){
                    if (firstUpdate) {
                        for(int i = 0; i < myDynamicTextValues.length; i++) {
                            myDynamicTextValues[i].setEnabled(true);
                        }
                        firstUpdate = false;
                        return;
                    }
                    sqLiteDBHelper.deleteProduct(email,userType);
                    for(int i = 0; i < myDynamicTextValues.length; i++) {
                        String product = myDynamicTextValues[i].getText().toString();
                            if (!product.equals("")) {
                                Log.i(TAG, "Adding product "+product);
                                result = sqLiteDBHelper.addProduct(new Product(product,userType,email));
                                if(result == Constants.DB_INSERT_ERROR) {
                                    Log.e(TAG, "Error Inserting the product "+myDynamicTextValues[i].getText().toString()+ "into DB.");
                                }
                            }
                    }
                    if (result != Constants.DEFAULT_ERROR) {
                        Toast.makeText(getApplicationContext(),Constants.TOAST_IUPDATED,Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(),Constants.TOAST_UPDATE_NOPRODUCT,Toast.LENGTH_SHORT).show();
                    }

                }
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
                        SellerHomePageActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
