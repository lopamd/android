package com.lopa.auctionapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lopa.auctionapp.activity.R;

public class ProductCursorAdapter extends CursorAdapter {
        private static final String TAG = "ProductCursorAdapter";
        public ProductCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.adapter_productlist_layout, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            if(cursor.getPosition()%2==1) {
                view.setBackgroundColor(context.getResources().getColor(R.color.background_odd));
            }
            else {
                view.setBackgroundColor(context.getResources().getColor(R.color.background_even));
            }
            TextView productBody = (TextView) view.findViewById(R.id.productName);

            String product_name = cursor.getString(cursor.getColumnIndexOrThrow(cursor.getColumnName(1)));
            Log.i(TAG, "Product Name " + product_name + "row count:"+cursor.getCount());
            productBody.setText(product_name);

        }
    }