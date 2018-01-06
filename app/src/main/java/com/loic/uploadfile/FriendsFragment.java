package com.loic.uploadfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by 胡敏浩 on 2017/10/23.
 */

public class FriendsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_friends,container,false);
       /* TextView textView = new TextView(getActivity());
        textView.setGravity(Gravity.CENTER);
        textView.setText("第三个Fragment");
        textView.setTextSize(30);*/
        return v;
    }

}
