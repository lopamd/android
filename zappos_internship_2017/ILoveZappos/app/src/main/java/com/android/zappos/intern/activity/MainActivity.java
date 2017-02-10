package com.android.zappos.intern.activity;


import android.content.Intent;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.SearchView;

import com.android.zappos.intern.R;
import com.android.zappos.intern.common.Constants;
import android.content.Intent;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private SearchView searchView;
    public String searchQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBar();
        SearchView searchView = (SearchView) findViewById(R.id.search);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v(TAG, "SerchQuery is" + query);
                searchItem(query);
                return true;
            }
        });
    }

    public void searchItem(String searchQuery) {
        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(Constants.INTENT_QUERY_KEY, searchQuery);
        startActivity(intent);
    }

    public void setupActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.title_bar);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.zappos_logo);
        actionBar.setDisplayUseLogoEnabled(true);

    }
}
