package com.loic.uploadfile;

/**
 * Created by 胡敏浩 on 2018/1/6.
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private TabLayout tabLayout;
    public ViewPager viewPager;

    public static List<Fragment> list;
    private MyAdapter adapter;
    private String[] titles = {"用户", "图库","上传"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        //getWindow().setFormat(PixelFormat.RGBX_8888);
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

}
