package com.loic.cocktail.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.alexvasilkov.android.commons.ui.ContextHelper;
import com.alexvasilkov.android.commons.ui.Views;
import com.loic.cocktail.FoldableListActivity;
import com.loic.cocktail.R;
import com.loic.cocktail.UnfoldableDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

public class PaintingsAdapter extends ItemsAdapter<Painting, PaintingsAdapter.ViewHolder>
        implements View.OnClickListener {

    private String photoInfoList;
    private JSONArray photoInfoJsonArray;

    public PaintingsAdapter(Context context) {

        if (context instanceof UnfoldableDetailsActivity) {
            this.photoInfoList =((UnfoldableDetailsActivity) context).getPhotoInfoList();
        } else if (context instanceof FoldableListActivity) {
            this.photoInfoList =((FoldableListActivity) context).getPhotoInfoList();
        }
        try {
            photoInfoJsonArray = new JSONArray(photoInfoList);
            setItemsList(Arrays.asList(Painting.getAllPaintings(photoInfoJsonArray.toString())));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    protected ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        final ViewHolder holder = new ViewHolder(parent);
        holder.image.setOnClickListener(this);
        return holder;
    }

    @Override
    protected void onBindHolder(ViewHolder holder, int position) {
        final Painting item = getItem(position);
        String photoInfo="";
        try {
            photoInfo = photoInfoJsonArray.getString(position);
        }catch (JSONException e){
            e.printStackTrace();
        }
        holder.image.setTag(R.id.list_item_image, item);
        GlideHelper.loadPaintingImage(holder.image, photoInfo);
        holder.title.setText(item.getTitle());
    }

    @Override
    public void onClick(View view) {
        final Painting item = (Painting) view.getTag(R.id.list_item_image);
        final Activity activity = ContextHelper.asActivity(view.getContext());

        if (activity instanceof UnfoldableDetailsActivity) {
            ((UnfoldableDetailsActivity) activity).openDetails(view, item);
        } else if (activity instanceof FoldableListActivity) {
            Toast.makeText(activity, item.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    static class ViewHolder extends ItemsAdapter.ViewHolder {
        final ImageView image;
        final TextView title;

        ViewHolder(ViewGroup parent) {
            super(Views.inflate(parent, R.layout.list_item));
            image = Views.find(itemView, R.id.list_item_image);
            title = Views.find(itemView, R.id.list_item_title);
        }
    }

}
