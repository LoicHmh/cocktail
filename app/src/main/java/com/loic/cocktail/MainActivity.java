package com.loic.cocktail;

/**
 * Created by 胡敏浩 on 2018/1/6.
 */

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.loic.cocktail.eventbus.MyEvent;
import com.loic.cocktail.fragment.FriendsFragment;
import com.loic.cocktail.fragment.SignInFragment;
import com.loic.cocktail.fragment.UploadFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private TabLayout tabLayout;
    public ViewPager viewPager;

    public static List<Fragment> list;
    private MyAdapter adapter;
    private String[] titles = {"用户", "图库","上传",};

    private String info="";
    private int state = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //透明状态栏  
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            Window window=getWindow();
            // Translucent status bar  
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //实例化
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        //页面，数据源
        list = new ArrayList<>();
        list.add(new SignInFragment());
        list.add(new FriendsFragment());
        list.add(new UploadFragment());
        //ViewPager的适配器
        FragmentManager fm = getSupportFragmentManager();
        adapter = new MyAdapter(fm);
        viewPager.setAdapter(adapter);
        //绑定
        tabLayout.setupWithViewPager(viewPager);

        //利用EventBus进行两个Fragment之间的通信
        //注册EventBus
        try{
            EventBus.getDefault().register(this);
        }catch (Exception e){
            e.printStackTrace();
        }

        //getWindow().setFormat(PixelFormat.RGBX_8888);
    }

    public String getInfo(){
        return info;
    }

    public void setInfo(String info){
        this.info=info;
    }

    public int getState(){
        return state;
    }

    public void setState(int state){
        this.state=state;
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Subscribe
    public void onEventMainThread(MyEvent myEvent){
        int tag=myEvent.getTag();
        String msg = myEvent.getMsg();
        if (tag == 1){
            this.setInfo(msg);
        }else if (tag == 0){
            if (msg=="ONLINE")
                this.setState(1);
            else
                this.setState(0);
        }else if (tag == 8){
            this.setState(0);
            this.setInfo("");
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
