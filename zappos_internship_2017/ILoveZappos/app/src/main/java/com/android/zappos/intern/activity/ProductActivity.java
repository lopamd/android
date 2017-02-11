package com.android.zappos.intern.activity;

import com.android.zappos.intern.apiInterface.ZapposInterface;
import com.android.zappos.intern.common.Constants;
import com.android.zappos.intern.restClient.ZapposClient;
import com.android.zappos.intern.R;
import com.android.zappos.intern.model.Product;
import com.android.zappos.intern.model.ProductDetails;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import android.util.Log;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.android.zappos.intern.databinding.ProductDisplayBinding;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener{

    private String asin;
    private String productName;
    private String imageURL;
    private String price;
    private String rating;
    private String description;
    private Intent intent;

    private static final String TAG = ProductActivity.class.getSimpleName();
    private ProductDisplayBinding binding;
    TextView itemCount;
    private Boolean isCartOpen = false;
    private FloatingActionButton cartFab,cartAddFab,cartDelFab;
    private Animation cartOpenAnim,cartCloseAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.product_display);
        setupActionBar();
        handleIntent(getIntent());
        cartFab = (FloatingActionButton)findViewById(R.id.cart);
        cartAddFab = (FloatingActionButton)findViewById(R.id.cart_add);
        cartDelFab = (FloatingActionButton)findViewById(R.id.cart_delete);
        cartOpenAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cart_open);
        cartCloseAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.cart_close);
        cartFab.setOnClickListener(this);
        cartAddFab.setOnClickListener(this);
        cartDelFab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.cart:
                animateFAB();
                break;
            case R.id.cart_add:
                updateCart(true);
                //Toast.makeText(getApplicationContext(),"Item added to cart successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Item added to cart");
                break;
            case R.id.cart_delete:
                updateCart(false);
                //Toast.makeText(getApplicationContext(),"Item removed from cart successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Item removed from cart");

                break;
        }
    }
    public void animateFAB(){

        if(isCartOpen){

            cartAddFab.startAnimation(cartCloseAnim);
            cartDelFab.startAnimation(cartCloseAnim);
            cartAddFab.setClickable(false);
            cartDelFab.setClickable(false);
            isCartOpen = false;
            Log.d(TAG, "close");

        } else {

            cartAddFab.startAnimation(cartOpenAnim);
            cartDelFab.startAnimation(cartOpenAnim);
            cartAddFab.setClickable(true);
            cartDelFab.setClickable(true);
            isCartOpen = true;
            Log.d(TAG,"open");

        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    public void setupActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setLogo(R.drawable.zappos_logo);
        actionBar.setDisplayUseLogoEnabled(true);

    }


    public void handleIntent(Intent intent) {
        if (intent.getAction() == Intent.ACTION_SEARCH) {
            String query = intent.getStringExtra(Constants.INTENT_QUERY_KEY);
            Log.v(TAG, "Rcvd the intent "+query);
            getProductData(query);
        }
    }

    private void getProductData(String squery) {
        ZapposInterface apiService = ZapposClient.getClient().create(ZapposInterface.class);

        Call<ProductDetails> call = apiService.getProduct(squery, Constants.API_KEY);
        call.enqueue(new Callback<ProductDetails>() {
            public void onResponse(Call<ProductDetails> call, Response<ProductDetails> response) {
                int statusCode = response.code();
                List<Product> products = response.body().getResults();
                if(products != null) {

                    binding.setProduct(products.get(0));
                    Log.d(TAG, "Number of Products received: " + products.size());
                }
            }

            public void onFailure(Call<ProductDetails> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.cartMenu);
        MenuItemCompat.setActionView(item, R.layout.cart_menu);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);
        if (notifCount != null) {
            itemCount = (TextView) notifCount.findViewById(R.id.cartItem);
            itemCount.setVisibility(View.INVISIBLE);

        } else {
            Log.e(TAG, "cart view is null");
        }
        return super.onCreateOptionsMenu(menu);

    }

    public void updateCart(boolean bAdd){
        itemCount.setVisibility(View.VISIBLE);
        String setItemCount = "0";
        int cCount = 0;
        itemCount.setVisibility(View.VISIBLE);
        String currCount = (String)itemCount.getText();
        Log.v(TAG, "CurrentCount: " + currCount);
        if (currCount == "")
            currCount = setItemCount;

        if (bAdd)
            cCount = Integer.parseInt(currCount) + 1;
        else {
            cCount = Integer.parseInt(currCount) - 1;
            if (cCount < 0)
                cCount = 0;
        }
        Log.v(TAG, "ItemCount after update: " + cCount);
        setItemCount = Integer.toString(cCount);

        Log.v(TAG, "ItemCount: " + setItemCount);
        if (!setItemCount.equals("0"))
            itemCount.setText(setItemCount);
        else {
            itemCount.setText("");
            itemCount.setVisibility(View.INVISIBLE);
        }
    }

}
