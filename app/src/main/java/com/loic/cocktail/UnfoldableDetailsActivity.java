package com.loic.cocktail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.texts.SpannableBuilder;
import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.alexvasilkov.foldablelayout.shading.GlanceFoldShading;
import com.loic.cocktail.helper.GlideHelper;
import com.loic.cocktail.helper.Painting;
import com.loic.cocktail.helper.PaintingsAdapter;

public class UnfoldableDetailsActivity extends AppCompatActivity {

    private View listTouchInterceptor;
    private View detailsLayout;
    private UnfoldableView unfoldableView;
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
        setContentView(R.layout.activity_unfoldable_details);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        ListView listView = Views.find(this, R.id.list_view);
        listView.setAdapter(new PaintingsAdapter(this));

        listTouchInterceptor = Views.find(this, R.id.touch_interceptor_view);
        listTouchInterceptor.setClickable(false);

        detailsLayout = Views.find(this, R.id.details_layout);
        detailsLayout.setVisibility(View.INVISIBLE);

        unfoldableView = Views.find(this, R.id.unfoldable_view);

        Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
        unfoldableView.setFoldShading(new GlanceFoldShading(glance));

        unfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
                detailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
                detailsLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (unfoldableView != null
                && (unfoldableView.isUnfolded() || unfoldableView.isUnfolding())) {
            unfoldableView.foldBack();
        } else {
            super.onBackPressed();
        }
    }

    public void openDetails(View coverView, Painting painting) {
        final ImageView image = Views.find(detailsLayout, R.id.details_image);
        final TextView title = Views.find(detailsLayout, R.id.details_title);
        final TextView description = Views.find(detailsLayout, R.id.details_text);

        //GlideHelper.loadPaintingImage(image, painting);

        GlideHelper.loadPaintingImage(image,painting);

        title.setText(painting.getTitle());

        SpannableBuilder builder = new SpannableBuilder(this);
        builder
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append("作品名称").append(": ")
                .clearStyle()
                .append(painting.getTitle()).append("\n")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append("所用风格").append(": ")
                .clearStyle()
                .append(painting.getStyle()).append("\n")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append("作者名称").append(": ")
                .clearStyle()
                .append(painting.getUsrname()).append("\n")
                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
                .append("已获赞数").append(": ")
                .clearStyle()
                .append(""+painting.getGood());
        description.setText(builder.build());

        unfoldableView.unfold(coverView, detailsLayout);
    }

}
