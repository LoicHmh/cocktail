package com.loic.cocktail.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Painting {



    private final String imageAddress;
    private final String title;


    private Painting(String imageAddress, String title) {
        this.imageAddress = imageAddress;
        this.title = title;

    }

    public String getImageAddress() {
        return imageAddress;
    }

    public String getTitle() {
        return title;
    }


    public static Painting[] getAllPaintings(String jsonArrayStr) {
        ArrayList<Painting> paintings = new ArrayList<Painting>();
        JSONArray jsonArray;
        try {
            jsonArray=new JSONArray(jsonArrayStr);
            for (int i=0; i<jsonArray.length();i++){
                String photoInfoJsonStr = jsonArray.getString(i);
                JSONObject photoInfo = new JSONObject(photoInfoJsonStr);
                String picname=photoInfo.getString("picname");
                String photoAddress = photoInfo.getString("photoAddress");
                Painting painting = new Painting(photoAddress,picname);
                paintings.add(painting);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        Painting[] rst = paintings.toArray(new Painting[paintings.size()]);

        return rst;
    }

}


/*
    private final int imageId;
    private final String title;
    private final String year;
    private final String location;

    private Painting(int imageId, String title, String year, String location) {
        this.imageId = imageId;
        this.title = title;
        this.year = year;
        this.location = location;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getLocation() {
        return location;
    }

    public static Painting[] getAllPaintings(Resources res) {
        String[] titles = res.getStringArray(R.array.paintings_titles);
        String[] years = res.getStringArray(R.array.paintings_years);
        String[] locations = res.getStringArray(R.array.paintings_locations);
        TypedArray images = res.obtainTypedArray(R.array.paintings_images);

        int size = titles.length;
        Painting[] paintings = new Painting[size];

        for (int i = 0; i < size; i++) {
            final int imageId = images.getResourceId(i, -1);
            paintings[i] = new Painting(imageId, titles[i], years[i], locations[i]);
        }

        images.recycle();
        return paintings;
    }*/
