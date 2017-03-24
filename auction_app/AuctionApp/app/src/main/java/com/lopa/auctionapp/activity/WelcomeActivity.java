package com.lopa.auctionapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lopa.auctionapp.common.Constants;

public class WelcomeActivity extends AppCompatActivity {
    Button seller,buyer;
    int s = Constants.UTYPE_SELLER;
    int b = Constants.UTYPE_BUYER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        seller = (Button)findViewById(R.id.buttonSeller);
        buyer = (Button)findViewById(R.id.buttonBuyer);

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSellerLogIn = new Intent(getApplicationContext(),LogInActivity.class);
                intentSellerLogIn.putExtra(Constants.UTYPE,s);
                startActivity(intentSellerLogIn);
            }
        });

        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBuyerLogIn = new Intent(getApplicationContext(),LogInActivity.class);
                intentBuyerLogIn.putExtra(Constants.UTYPE,b);
                startActivity(intentBuyerLogIn);
            }
        });
    }
}
