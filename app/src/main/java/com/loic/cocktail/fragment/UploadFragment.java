package com.loic.cocktail.fragment;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loic.cocktail.MainActivity;
import com.loic.cocktail.R;
import com.loic.cocktail.util.UploadUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;

@RuntimePermissions
public class UploadFragment extends Fragment implements OnClickListener{
    private static String IP="10.163.9.37";
    private static String requestURL = "http://"+IP+":8080/transfer_server";
    private Button  uploadImage;
    private ImageButton selectImage;
    private ImageButton modelButton1,modelButton2,modelButton3,modelButton4,modelButton5,modelButton6;
    private static int RESULT_LOAD_IMAGE = 10;
    private JSONObject usrInfoJson;



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
        selectImage.setOnClickListener(this);
        modelButton1.setOnClickListener(this);
        modelButton2.setOnClickListener(this);
        modelButton3.setOnClickListener(this);
        modelButton4.setOnClickListener(this);
        modelButton5.setOnClickListener(this);
        modelButton6.setOnClickListener(this);

        uploadImage.setOnClickListener(this);
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
                    Toast.makeText(getActivity(), "请选择图片！", Toast.LENGTH_SHORT).show();
                }else if (modelSelected==null){
                    Toast.makeText(getActivity(), "请选择风格！", Toast.LENGTH_SHORT).show();
                }else if (usrInfoJson==null){
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                    }
                }
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
            selectImage.setImageBitmap(BitmapFactory.decodeFile(picPath));
            cursor.close();
        }
    }

    public String updateUsrInfo(){
        MainActivity mainActivity = (MainActivity) getActivity();
        return mainActivity.getInfo();
    }
}