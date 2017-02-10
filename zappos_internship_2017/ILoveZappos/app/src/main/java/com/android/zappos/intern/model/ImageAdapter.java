package com.android.zappos.intern.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class ImageAdapter {

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        System.out.println("Image URL :"+ url);
        if ((url != null) && (!url.equals(""))) {
            Picasso.with(imageView.getContext()).load(url).into(imageView);
        }
    }
}
