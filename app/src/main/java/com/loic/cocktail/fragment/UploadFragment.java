package com.loic.cocktail.fragment;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loic.cocktail.MainActivity;
import com.loic.cocktail.R;
import com.loic.cocktail.eventbus.MyEvent;
import com.loic.cocktail.util.NetUtil;
import com.loic.cocktail.util.UploadUtil;

import org.json.JSONException;
import org.json.JSONObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;

@RuntimePermissions
public class UploadFragment extends Fragment implements OnClickListener{
    private static String IP="192.168.1.112";
    private static String requestURL = "http://"+IP+":8080/transfer_server";
    private Button  uploadImage,downloadImage;
    private ImageButton selectImage;
    private ImageButton modelButton1,modelButton2,modelButton3,modelButton4,modelButton5,modelButton6;
    private ImageView imageView,imageView2;
    private static int RESULT_LOAD_IMAGE = 10;
    private JSONObject usrInfoJson;
    private TextView textView;


    private String picPath = null;
    private String modelSelected=null;

    /** Called when the activity is first created. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        View v= inflater.inflate(R.layout.fragment_upload,container,false);

        selectImage = (ImageButton) v.findViewById(R.id.selectImage);
        modelButton1 = (ImageButton) v.findViewById(R.id.model1);
        modelButton2 = (ImageButton) v.findViewById(R.id.model2);
        modelButton3 = (ImageButton) v.findViewById(R.id.model3);
        modelButton4 = (ImageButton) v.findViewById(R.id.model4);
        modelButton5 = (ImageButton) v.findViewById(R.id.model5);
        modelButton6 = (ImageButton) v.findViewById(R.id.model6);

        uploadImage = (Button) v.findViewById(R.id.uploadImage);
        downloadImage = (Button) v.findViewById(R.id.downloadImage);
        selectImage.setOnClickListener(this);
        modelButton1.setOnClickListener(this);
        modelButton2.setOnClickListener(this);
        modelButton3.setOnClickListener(this);
        modelButton4.setOnClickListener(this);
        modelButton5.setOnClickListener(this);
        modelButton6.setOnClickListener(this);

        uploadImage.setOnClickListener(this);
        downloadImage.setOnClickListener(this);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        imageView2 = (ImageView) v.findViewById(R.id.imageView2);

        textView = (TextView) v.findViewById(R.id.upload_text);
        textView.setText("nothing at all");
        MainActivity mainActivity = (MainActivity) getActivity();
        textView.setText(mainActivity.getInfo());


        //利用EventBus进行两个Fragment之间的通信
        //注册EventBus
        try{
            EventBus.getDefault().register(this);
        }catch (Exception e){
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        String usrInfo=updateUsrInfo();
        try {
            usrInfoJson = new JSONObject(usrInfo);
        }catch (JSONException e){
            e.printStackTrace();
        }

        switch (v.getId()) {
            case R.id.selectImage:
                /***
                 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
                 */
                //点击事件，而重定向到图片库
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //这里要传一个整形的常量RESULT_LOAD_IMAGE到startActivityForResult()方法。
                startActivityForResult(intent, RESULT_LOAD_IMAGE);

                break;
            case R.id.uploadImage:
                if (picPath == null) {
                    //Toast.makeText(UploadFragment.this, "请选择图片！", Toast.LENGTH_SHORT).show();
                } else {
                    final File file = new File(picPath);
                    if (file != null) {
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Map<String, String> params = new HashMap<>();
                                //设置编码类型为utf-8
                                try {
                                    params.put("usrname", usrInfoJson.getString("usrname"));
                                    params.put("password", usrInfoJson.getString("password"));
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                                params.put("model",modelSelected);
                                int request = UploadUtil.uploadFile(file, requestURL,params);
                            }
                        }).start();
                    }
                }
                break;
            case R.id.downloadImage:
                String picname="P1.jpeg";
                final String url1="http://192.168.1.112:8080/transfer_server?request=P1.jpeg&usrname=hmh&password=loic";
                doGet(url1);
                break;
            case R.id.model1:
                modelSelected="la_muse";
                break;
            case R.id.model2:
                modelSelected="rain_princess";
                break;
            case R.id.model3:
                modelSelected="scream";
                break;
            case R.id.model4:
                modelSelected="udnie";

                break;
            case R.id.model5:
                modelSelected="wave";
                break;
            case R.id.model6:
                modelSelected="wreck";
                break;


            default:
                break;
        }
    }


    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            //查询我们需要的数据
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picPath = cursor.getString(columnIndex);
            //imageView2.setImageBitmap(BitmapFactory.decodeFile(picPath));
            selectImage.setImageBitmap(BitmapFactory.decodeFile(picPath));
            //Toast.makeText(UploadFragment.this, "picPath:"+picPath, Toast.LENGTH_LONG).show();
            cursor.close();
        }
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

    public void  doGet(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //访问网络要在子线程中实现，使用get取数据
                final String state= NetUtil.loginOfGet(url);

                //执行在主线程上
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                       EventBus.getDefault().post(new MyEvent(state,2));
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
                    downloadBitmap(pic_url, imageView2);
                }
            }).start();
        }

    }

    public String updateUsrInfo(){
        MainActivity mainActivity = (MainActivity) getActivity();
        return mainActivity.getInfo();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}