package com.lopa.auctionapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lopa.auctionapp.SQLiteDB.SQLiteDBHelper;
import com.lopa.auctionapp.common.Constants;

public class LogInActivity extends AppCompatActivity {
    TextView _btnreg,_signIn;
    int userType;
    private static final String TAG = "LoginActivity";
    SQLiteDBHelper sqLiteDBHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        final EditText _txtemail = (EditText) findViewById(R.id.txtemail);
        final EditText _txtpass = (EditText) findViewById(R.id.txtpass);

        _btnreg = (TextView) findViewById(R.id.btnreg);
        _signIn = (Button) findViewById(R.id.btnsignin);

        sqLiteDBHelper = new SQLiteDBHelper(this);

        _signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = _txtemail.getText().toString();
                String pass = _txtpass.getText().toString();
                Log.d(TAG,"Value of email"+ email);

                final AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);

                if(email.equals("") || pass.equals("")) {
                    Log.d("Value of email", email);
                    builder.setTitle(Constants.POPUP_ALERT);
                    builder.setMessage(Constants.LOGIN_BLANK);
                    builder.setPositiveButton(Constants.POPUP_OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    boolean found = sqLiteDBHelper.user_validation(email, pass);

                    if (!found) {
                        Log.d("Value of email", email);
                        builder.setTitle(Constants.POPUP_ALERT);
                        builder.setMessage(Constants.LOGIN_INVALID_MSG);
                        builder.setPositiveButton(Constants.POPUP_OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        _txtemail.setText("");
                        _txtpass.setText("");
                        return;
                    }

                    userType = sqLiteDBHelper.getUserType(email);
                    if (userType == Constants.UTYPE_SELLER) {
                        Intent intent = new Intent(getApplicationContext(), SellerHomePageActivity.class);
                        intent.putExtra(Constants.UTYPE, userType);
                        intent.putExtra(Constants.EMAIL, email);
                        startActivity(intent);
                    } else if (userType == Constants.UTYPE_BUYER) {
                        Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                        intent.putExtra(Constants.UTYPE, userType);
                        intent.putExtra(Constants.EMAIL, email);
                        startActivity(intent);
                    } else if(userType == Constants.UTYPE_BOTH) {
                        //sellerBuyerLayout.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(getApplicationContext(), SellerHomePageActivity.class);
                        intent.putExtra(Constants.UTYPE, userType);
                        intent.putExtra(Constants.EMAIL, email);
                        startActivity(intent);
                    }
                }
                _txtemail.setText("");
                _txtpass.setText("");

            }});


        _btnreg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

}
