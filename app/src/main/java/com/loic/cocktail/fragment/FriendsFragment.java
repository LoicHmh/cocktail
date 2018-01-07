package com.loic.cocktail.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.loic.cocktail.FoldableListActivity;
import com.loic.cocktail.MainActivity;
import com.loic.cocktail.R;
import com.loic.cocktail.UnfoldableDetailsActivity;
import com.loic.cocktail.eventbus.MyEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 胡敏浩 on 2017/10/23.
 */

public class FriendsFragment extends Fragment {
    private ImageButton friendButton;
    private ImageButton galleryButton;
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_friends,container,false);
        textView=(TextView) v.findViewById(R.id.friend_text);
        textView.setText("nothing at all");


        friendButton = (ImageButton) v.findViewById(R.id.friend_btn);
        friendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updateUsrInfo();
                Intent intent=new Intent();
                intent.setClass(getActivity(),UnfoldableDetailsActivity.class);
                intent.putExtra("ok","test");
                getActivity().startActivity(intent);
            }
        });

        galleryButton = (ImageButton) v.findViewById(R.id.gallery_btn);
        galleryButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updateUsrInfo();
                Intent intent=new Intent();
                intent.setClass(getActivity(),FoldableListActivity.class);
                intent.putExtra("ok","test");
                getActivity().startActivity(intent);
            }
        });


        EventBus.getDefault().post(new MyEvent("lalal",0));

        return v;
    }

    public String updateUsrInfo(){
        MainActivity mainActivity = (MainActivity) getActivity();
        textView.setText(mainActivity.getInfo());
        return mainActivity.getInfo();
    }


}
