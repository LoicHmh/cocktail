package com.loic.cocktail.helper;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

public class GlideHelper {

    private GlideHelper() {}

    public static void loadPaintingImage(ImageView image, Painting painting) {
        Glide.with(image.getContext().getApplicationContext())
                .load(painting.getImageAddress())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }

    public static void loadPaintingImage(ImageView image, String photoInfo) {

        JSONObject photoInfoJson;
        String address="";
        try {
            photoInfoJson=new JSONObject(photoInfo);
            address=photoInfoJson.getString("photoAddress");
        }catch (JSONException e){
            e.printStackTrace();
        }

        Glide.with(image.getContext().getApplicationContext())
                .load(address)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);
    }

}
