package com.loic.cocktail.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loic.cocktail.FoldableListActivity;
import com.loic.cocktail.MainActivity;
import com.loic.cocktail.R;
import com.loic.cocktail.UnfoldableDetailsActivity;
import com.loic.cocktail.eventbus.MyEvent;
import com.loic.cocktail.util.NetUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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

        //利用EventBus进行两个Fragment之间的通信
        //注册EventBus
        try{
            EventBus.getDefault().register(this);
        }catch (Exception e){
            e.printStackTrace();
        }

        View v= inflater.inflate(R.layout.fragment_friends,container,false);
        textView=(TextView) v.findViewById(R.id.friend_text);
        textView.setText("nothing at all");


        friendButton = (ImageButton) v.findViewById(R.id.friend_btn);
        friendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updateUsrInfo();
                Intent intent=new Intent();
                intent.setClass(getActivity(),UnfoldableDetailsActivity.class);

                String picname="P1.jpeg";
                final String url1="http://192.168.1.112:8080/transfer_server?request=P1.jpeg&usrname=hmh&password=loic";
                doGet(url1);

                JSONArray photoInfoList;
                JSONObject photoInfo = new JSONObject();
                try{
                    photoInfo.put("usrname","hmh");
                    photoInfo.put("picname","天气真好");
                    photoInfo.put("photoAddress","https://img.alicdn.com/imgextra/i2/880734502/TB2bfW7sSFjpuFjSspbXXXagVXa-880734502.jpg");
                    photoInfo.put("good",10);
                    photoInfoList = new JSONArray();
                    for (int i=0;i<10;i++){
                        photoInfoList.put(i,photoInfo.toString());
                    }
                    intent.putExtra("photoInfoList",photoInfoList.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
                getActivity().startActivity(intent);
            }
        });

        galleryButton = (ImageButton) v.findViewById(R.id.gallery_btn);
        galleryButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                updateUsrInfo();
                Intent intent=new Intent();
                intent.setClass(getActivity(),FoldableListActivity.class);
                JSONArray photoInfoList;
                JSONObject photoInfo = new JSONObject();
                try{
                    photoInfo.put("usrname","hmh");
                    photoInfo.put("picname","天气真好");
                    photoInfo.put("photoAddress","https://img.alicdn.com/imgextra/i2/880734502/TB2bfW7sSFjpuFjSspbXXXagVXa-880734502.jpg");
                    photoInfo.put("good",10);
                    photoInfoList = new JSONArray();
                    for (int i=0;i<10;i++){
                        photoInfoList.put(i,photoInfo.toString());
                    }
                    intent.putExtra("photoInfoList",photoInfoList.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }

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
        if (tag == 2){
            final String pic_url = msg;
            Toast.makeText(getActivity(),pic_url,Toast.LENGTH_LONG).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //downloadBitmap(pic_url, imageView2);
                }
            }).start();
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void downloadBitmap(String bmurl,final ImageView iv)    //bmurl是解析出来的utl， iv是显示图片的imageView控件
    {
        Log.d("download","bitmap");

        Bitmap bm=null;
        InputStream is =null;
        BufferedInputStream bis=null;
        try{
            URL url=new URL(bmurl);
            URLConnection connection=url.openConnection();
            bis=new BufferedInputStream(connection.getInputStream());
            bm= BitmapFactory.decodeStream(bis);
            final Bitmap bm1 = bm;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    iv.setImageBitmap(bm1);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if(bis!=null)
                    bis.close();
                if (is!=null)
                    is.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
