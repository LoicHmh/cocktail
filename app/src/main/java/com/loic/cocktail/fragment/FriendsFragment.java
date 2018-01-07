package com.loic.cocktail.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.loic.cocktail.FoldableListActivity;
import com.loic.cocktail.MainActivity;
import com.loic.cocktail.R;
import com.loic.cocktail.UnfoldableDetailsActivity;
import com.loic.cocktail.eventbus.MyEvent;
import com.loic.cocktail.util.NetUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by 胡敏浩 on 2017/10/23.
 */

public class FriendsFragment extends Fragment {
    private ImageButton friendButton;
    private ImageButton galleryButton;

    private String getFriend;
    private String getMySelf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //利用EventBus进行两个Fragment之间的通信
        //注册EventBus
        try{
            EventBus.getDefault().register(this);
        }catch (Exception e){
            e.printStackTrace();
        }

        View v= inflater.inflate(R.layout.fragment_friends,container,false);

        final String url1="http://192.168.1.112:8080/transfer_server?type=2";
        doGet(url1);


        friendButton = (ImageButton) v.findViewById(R.id.friend_btn);
        friendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updateUsrInfo();
                Intent intent=new Intent();
                intent.setClass(getActivity(),UnfoldableDetailsActivity.class);
                intent.putExtra("photoInfoList",getFriend);
                getActivity().startActivity(intent);
            }
        });

        galleryButton = (ImageButton) v.findViewById(R.id.gallery_btn);
        galleryButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updateUsrInfo();
                Intent intent=new Intent();
                intent.setClass(getActivity(),FoldableListActivity.class);
                intent.putExtra("photoInfoList",getMySelf);
                getActivity().startActivity(intent);
            }
        });
        return v;
    }

    public String updateUsrInfo(){
        MainActivity mainActivity = (MainActivity) getActivity();
        return mainActivity.getInfo();
    }

    public void  doGet(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                final String state= NetUtil.loginOfGet(url);

                //执行在主线程上
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        EventBus.getDefault().post(new MyEvent(state,5));
                    }
                });
            }
        }).start();
    }

    @Subscribe
    public void onEventMainThread(MyEvent myEvent){
        int tag=myEvent.getTag();
        String msg = myEvent.getMsg();
        if (tag == 5){
            getFriend=msg;
        } else if (tag == 6){
            getMySelf=msg;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
