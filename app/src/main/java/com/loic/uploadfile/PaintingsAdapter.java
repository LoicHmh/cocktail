package com.loic.uploadfile;

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

import java.util.Arrays;

public class PaintingsAdapter extends ItemsAdapter<Painting, PaintingsAdapter.ViewHolder>
        implements View.OnClickListener {

    public PaintingsAdapter(Context context) {
        setItemsList(Arrays.asList(Painting.getAllPaintings(context.getResources())));
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

        holder.image.setTag(R.id.list_item_image, item);
        GlideHelper.loadPaintingImage(holder.image, item);
        holder.title.setText(item.getTitle());
    }

/*    @Override
    public void onClick(View view) {
        final Painting item = (Painting) view.getTag(R.id.list_item_image);
        MainActivity mainActivity= (MainActivity) view.getContext();
        int index = mainActivity.viewPager.getCurrentItem();
        //"android:switcher:" + R.id.viewpager + ":"+item 这个字符窜表示的就是该//fragment的tag，其中item 是fragment 在viewpager中的位置。
        //Fragment fragment = theActivity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":"+item);
        Fragment fragment = mainActivity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":"+item);
        ((FriendsFragment) fragment).openDetails(view, item);

    }*/

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
