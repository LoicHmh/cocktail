package com.loic.cocktail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

        Intent intent = getIntent();
        photoInfoList = intent.getStringExtra("photoInfoList");

        FoldableListLayout foldableListLayout = Views.find(this, R.id.foldable_list);
        foldableListLayout.setAdapter(new PaintingsAdapter(this));
    }

}
