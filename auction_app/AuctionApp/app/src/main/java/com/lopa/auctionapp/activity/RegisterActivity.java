package com.lopa.auctionapp.activity;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lopa.auctionapp.SQLiteDB.SQLiteDBHelper;
import com.lopa.auctionapp.common.Constants;
import com.lopa.auctionapp.model.User;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "Register_Activity";
    SQLiteDBHelper sqLiteDBHelper;
    SQLiteDatabase db;
    int userType = Constants.UTYPE_INVALID;
    EditText _txtfullname;
    EditText _txtemail;
    EditText _txtpass;
    EditText _txtmobile;
    Button _btnreg;
    RadioGroup radioGroup;
    //RadioButton radio_seller,radio_buyer,radio_both;

    final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);

        _txtfullname = (EditText) findViewById(R.id.txtname_reg);
        _txtemail = (EditText) findViewById(R.id.txtemail_reg);
        _txtpass = (EditText) findViewById(R.id.txtpass_reg);
        _txtmobile = (EditText) findViewById(R.id.txtmobile_reg);
        _btnreg = (Button) findViewById(R.id.btn_reg);
        radioGroup = (RadioGroup) findViewById(R.id.radio1);


        sqLiteDBHelper = new SQLiteDBHelper(this);

        //Register Button Click Event
        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = sqLiteDBHelper.getWritableDatabase();

                String fullname = _txtfullname.getText().toString();
                String email = _txtemail.getText().toString();
                String pass = _txtpass.getText().toString();
                String mobile = _txtmobile.getText().toString();

                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                //Alert dialog after clicking the Register Account
                if (fullname.equals("") || email.equals("") || pass.equals("") || mobile.equals("")) {
                    builder.setTitle(Constants.POPUP_INFO);
                    builder.setMessage(Constants.REG_FIELD_BLANK);
                    builder.setPositiveButton(Constants.POPUP_OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Finishing the dialog.
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    boolean email_val = isValidEmail(email);
                    boolean mobile_val = Patterns.PHONE.matcher(mobile).matches();

                    if (!email_val) {
                        _txtemail.setError(Constants.REG_INVALID_EMAIL);
                        _txtemail.requestFocus();
                    } else if (!mobile_val) {
                        _txtmobile.setError(Constants.REG_INVALID_MOBILE);
                        _txtmobile.requestFocus();
                    } else if (radioGroup.getCheckedRadioButtonId() == -1) {
                        builder.setTitle(Constants.POPUP_INFO);
                        builder.setMessage(Constants.REG_RADIO_BLANK);
                        builder.setPositiveButton(Constants.POPUP_OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                    } else {
                        boolean found = sqLiteDBHelper.searchUser(email, userType);
                        int user_forUpdate = 0;
                        if (!found){
                            user_forUpdate = sqLiteDBHelper.getUserType(email);
                        }
                        //if (found == userType || found == Constants.UTYPE_BOTH) {
                        if (found) {
                            builder.setTitle(Constants.POPUP_INFO);
                            builder.setMessage(Constants.REG_REGISTERED_EMAIL_MSG);
                            builder.setPositiveButton(Constants.POPUP_OK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                }
                            });
                        } else {
                            long result = Constants.DEFAULT_ERROR;
                            Log.i(TAG,"found : "+ found);
                            Log.i(TAG,"user_forUpdate : " + user_forUpdate);

                            if ((user_forUpdate == Constants.UTYPE_SELLER && userType == Constants.UTYPE_BUYER) ||
                                    user_forUpdate == Constants.UTYPE_BUYER && userType == Constants.UTYPE_SELLER) {
                                //Update user with the new user type
                                Log.i(TAG,"Inside Update in register 1:" + userType);
                                Log.i(TAG,"Inside Update in register 2:" + user_forUpdate);
                                result = sqLiteDBHelper.updateUser(new User(fullname, email, pass, mobile,user_forUpdate), Constants.UTYPE_BOTH);
                                if(result != 0)
                                    Toast.makeText(getApplicationContext(),Constants.TOAST_SELLER_BUYER,Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Update user data "+result);

                            } else if (!found) {
                                //User is not found. Insert the user
                                Log.i(TAG,"Inside new user in register :" + userType);
                                result = sqLiteDBHelper.addUser(new User(fullname, email, pass, mobile, userType));

                            }
                            if (result != Constants.DEFAULT_ERROR) {
                                builder.setTitle(Constants.POPUP_INFO);
                                builder.setMessage(Constants.REG_SUCCESS_MSG);
                                builder.setPositiveButton(Constants.POPUP_OK, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //Finishing the dialog and removing Activity from stack.
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                            } else {
                                Log.e(TAG, "Inserting into DB failed.");
                                Toast.makeText(getApplicationContext(), Constants.REG_ERROR_DB, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio_buyer:
                if (checked)
                    userType = Constants.UTYPE_BUYER;
                    break;
            case R.id.radio_seller:
                if (checked)
                    userType = Constants.UTYPE_SELLER;
                    break;
            case R.id.radio_both:
                if (checked)
                    userType = Constants.UTYPE_BOTH;

                break;

        }
        Log.i(TAG,"Radio button checked "+userType);
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
