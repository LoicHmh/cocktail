package com.loic.cocktail;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.foldablelayout.FoldableListLayout;
import com.loic.cocktail.helper.PaintingsAdapter;

public class FoldableListActivity extends AppCompatActivity {

    public String getPhotoInfoList() {
        return photoInfoList;
    }

    public void setPhotoInfoList(String photoInfoList) {
        this.photoInfoList = photoInfoList;
    }

    private String photoInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foldable_list);

        //透明状态栏  
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            Window window=getWindow();
            // Translucent status bar  
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Intent intent = getIntent();
        photoInfoList = intent.getStringExtra("photoInfoList");

        FoldableListLayout foldableListLayout = Views.find(this, R.id.foldable_list);
        foldableListLayout.setAdapter(new PaintingsAdapter(this));
    }

}
