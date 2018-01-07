package com.loic.cocktail.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.loic.cocktail.MainActivity;
import com.loic.cocktail.R;
import com.loic.cocktail.eventbus.MyEvent;
import com.loic.cocktail.util.NetUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 胡敏浩 on 2017/10/23.
 */

public class SignInFragment extends Fragment {

    private final static int ONLINE=1;
    private final static int OFFLINE=0;
    private EditText usrnameEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private String usrname;
    private String password;
    private int state=OFFLINE;
    private TextInputLayout passwordLayout;
    private JSONObject usrInfoJson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_sign_in,container,false);
        usrnameEditText = (EditText) v.findViewById(R.id.usrname);
        passwordEditText = (EditText) v.findViewById(R.id.password);
        signInButton = (Button) v.findViewById(R.id.sign_in_btn);
        passwordLayout = (TextInputLayout) v.findViewById(R.id.password_layout);

        state=((MainActivity) getActivity()).getState();
        switch (state){
            case ONLINE:
                signInButton.setText("退出");

                usrnameEditText.setFocusable(false);
                usrnameEditText.setFocusableInTouchMode(false);
                passwordEditText.setFocusable(false);
                passwordEditText.setFocusableInTouchMode(false);
                passwordLayout.setVisibility(View.INVISIBLE);


                break;
            case OFFLINE:
                signInButton.setText("登录");

                usrnameEditText.setFocusable(true);
                usrnameEditText.setFocusableInTouchMode(true);
                usrnameEditText.requestFocus();
                passwordEditText.setFocusable(true);
                passwordEditText.setFocusableInTouchMode(true);
                passwordLayout.setVisibility(View.VISIBLE);

                break;
        }

        usrnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("text",s.toString());
                usrname = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("text",s.toString());
                password = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (state){
                    case ONLINE:
                        signInButton.setText("登录");
                        state=OFFLINE;
                        usrnameEditText.setFocusable(true);
                        usrnameEditText.setFocusableInTouchMode(true);
                        usrnameEditText.requestFocus();
                        passwordEditText.setFocusable(true);
                        passwordEditText.setFocusableInTouchMode(true);
                        passwordLayout.setVisibility(View.VISIBLE);
                        EventBus.getDefault().post(new MyEvent("OFFLINE",0));
                        break;
                    case OFFLINE:
                        signInButton.setText("退出");
                        state=ONLINE;
                        usrInfoJson=new JSONObject();
                        try{
                            usrInfoJson.put("usrname",usrname);
                            usrInfoJson.put("password",password);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        usrnameEditText.setFocusable(false);
                        usrnameEditText.setFocusableInTouchMode(false);
                        passwordEditText.setFocusable(false);
                        passwordEditText.setFocusableInTouchMode(false);
                        passwordLayout.setVisibility(View.INVISIBLE);
                        EventBus.getDefault().post(new MyEvent("ONLINE",0));//传给mainActivity
                        EventBus.getDefault().post(new MyEvent(usrInfoJson.toString(),1));//传给mainActivity

                        final String url1="http://192.168.1.112:8080/transfer_server?type=1&usrname="+usrname+"&password="+password;
                        doGet(url1);
                        break;
                }
            }
        });
        return v;
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
                        EventBus.getDefault().post(new MyEvent(state,6));
                    }
                });
            }
        }).start();

    }

}
