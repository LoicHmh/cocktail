package com.loic.uploadfile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends Activity implements OnClickListener{
    //private static String requestURL = "http://172.19.76.105/GET/Secret/com/FileUpload";
    private static String requestURL = "http://192.168.1.112:8080/transfer_server";
    private Button selectImage, uploadImage,downloadImage;
    private ImageView imageView,imageView2;
    private static int RESULT_LOAD_IMAGE = 10;

    private JSONObject userInfo;

    private String picPath = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectImage = (Button) this.findViewById(R.id.selectImage);
        uploadImage = (Button) this.findViewById(R.id.uploadImage);
        downloadImage = (Button) this.findViewById(R.id.downloadImage);
        selectImage.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        downloadImage.setOnClickListener(this);
        imageView = (ImageView) this.findViewById(R.id.imageView);
        imageView2 = (ImageView) this.findViewById(R.id.imageView2);

        userInfo=new JSONObject();
        try{
            userInfo.put("usrname","hmh");
            userInfo.put("password","loic");
        }catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onClick(View v) {
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
                    Toast.makeText(MainActivity.this, "请选择图片！", Toast.LENGTH_SHORT).show();
                } else {
                    final File file = new File(picPath);
                    if (file != null) {
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Map<String, String> params = new HashMap<>();
                                //设置编码类型为utf-8
                                params.put("usrname", "hmh");
                                params.put("password", "loic");
                                int request = UploadUtil.uploadFile(file, requestURL,params);
                            }
                        }).start();
                    }
                }
                break;
            case R.id.downloadImage:
                Toast.makeText(MainActivity.this, "ok！", Toast.LENGTH_SHORT).show();
                //final String url1="https://gd2.alicdn.com/imgextra/i2/46647549/TB2_CzZtZtnpuFjSZFvXXbcTpXa_!!46647549.jpg";
                final String url1="http://10.163.97.10:8080/transfer_server?request=timg.jpg";
                //http://10.163.97.10:8080/transfer_server?request=timg.jpg
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        downloadBitmap(url1,imageView2);
                    }
            }).start();
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
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picPath = cursor.getString(columnIndex);
            imageView2.setImageBitmap(BitmapFactory.decodeFile(picPath));
            Toast.makeText(MainActivity.this, "picPath:"+picPath, Toast.LENGTH_LONG).show();
            cursor.close();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //GGView.enable=false;
            System.exit(0);
        }
        return true;
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
            runOnUiThread(new Runnable() {
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