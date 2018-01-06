package com.loic.uploadfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * Created by 胡敏浩 on 2017/10/23.
 */

public class FriendsFragment extends Fragment {
    private ImageButton friendButton;
    private ImageButton galleryButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_friends,container,false);
        friendButton = (ImageButton) v.findViewById(R.id.friend_btn);
        friendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(getActivity(),UnfoldableDetailsActivity.class);
                intent.putExtra("ok","test");
                getActivity().startActivity(intent);
            }
        });
        galleryButton = (ImageButton) v.findViewById(R.id.gallery_btn);
        galleryButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(getActivity(),FoldableListActivity.class);
                intent.putExtra("ok","test");
                getActivity().startActivity(intent);
            }
        });
        return v;
    }

}
