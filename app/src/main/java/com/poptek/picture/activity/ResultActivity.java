package com.poptek.picture.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TImage;
import com.poptek.picture.R;
import com.poptek.picture.entity.CItem;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import org.zackratos.ultimatebar.UltimateBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class ResultActivity extends Activity {
    ArrayList<TImage> images;
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private Button submit,cancel;
    private mHandler mHandler;
    private static final int SUCCESS= 1;
    private static final int UPLOADSUCCESS= 2;
    private String module_typed="0";
    private  JSONArray modulejsonArray;

    private String UUID,ResultUUID,imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_layout);
        images= (ArrayList<TImage>) getIntent().getSerializableExtra("images");
        showImg();
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar(true);
        init();
        mHandler = new mHandler();
    }




    private  void init(){
        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upLoadImage();
            }
        });



        spinner = (Spinner) findViewById(R.id.spinner);

        //数据
//        data_list = new ArrayList<String>();
//
//
//        data_list.add("阴影");
//        data_list.add("雕塑");
//        data_list.add("雅黑");
//
//        //适配器
//        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
//        //设置样式
//        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //加载适配器
//        spinner.setAdapter(arr_adapter);


        getData();


    }
    private void showImg() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llImages);
        for (int i = 0, j = images.size(); i < j - 1; i += 2) {
            View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
            ImageView imageView1 = (ImageView) view.findViewById(R.id.imgShow1);
//            ImageView imageView2 = (ImageView) view.findViewById(R.id.imgShow2);
            Glide.with(this).load(new File(images.get(i).getCompressPath())).into(imageView1);
//            Glide.with(this).load(new File(images.get(i + 1).getCompressPath())).into(imageView2);
            linearLayout.addView(view);
        }
        if (images.size() % 2 == 1) {
            View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
            ImageView imageView1 = (ImageView) view.findViewById(R.id.imgShow1);
            Glide.with(this).load(new File(images.get(images.size() - 1).getCompressPath())).into(imageView1);
            linearLayout.addView(view);
        }
    }


    private void getData(){


        RequestParams params = new RequestParams("http://monet.daandu.com/v1/list_model");
        x.http().post(params, new Callback.CommonCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray result) {

                if(result!=null){

                        modulejsonArray = result;

                    mHandler.sendEmptyMessage(SUCCESS);
                }


                //解析result
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("err",ex.toString());
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });


    }

    private void upLoadImage(){
        UUID = java.util.UUID.randomUUID().toString();
        RequestParams params = new RequestParams("http://monet.daandu.com/v1/upload_pic");
        params.addBodyParameter("action_id",UUID);
        params.addBodyParameter("model_id",module_typed);
        params.setMultipart(true);
        params.addBodyParameter("file",new File(images.get(0).getOriginalPath()));
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {

                if(result!=null){
                    try {
                        ResultUUID = result.getString("success");
                        imageUrl = result.getString("image_url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(UPLOADSUCCESS);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ResultActivity.this,ex.toString(),Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });



    }




    private class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS:
                    List<CItem> lst = new ArrayList<CItem>();
                    for (int i = 0; i < modulejsonArray.length(); i++) {
                        /*
                         * 首先新建一个list,赋值
                         * ID为序号
                         */
                        int id = -1;
                        String value = null;
                        try {
                            id = Integer.valueOf(modulejsonArray.getJSONObject(i).getString("model_id"));
                            value = modulejsonArray.getJSONObject(i).getString("model_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CItem item = new CItem(id, value);
                        lst.add(item);
                    }
                    ArrayAdapter<CItem> myaAdapter = new ArrayAdapter<CItem>(ResultActivity.this, android.R.layout.simple_spinner_item, lst);
                    spinner.setAdapter(myaAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            /*
                             * ids是刚刚新建的list里面的ID
                             */
                            int ids = ((CItem) spinner.getSelectedItem()).GetID();
                            module_typed = String.valueOf(ids);
                            System.out.println(ids);
//                            Toast.makeText(getApplicationContext(), String.valueOf(ids), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });


                    break;
                case UPLOADSUCCESS:

                    if(UUID.equals(ResultUUID)){
                        Intent intent = new Intent(ResultActivity.this,PicturePreview.class);
                        intent.putExtra("imageUrl",imageUrl);
                        intent.putExtra("uuid",UUID);
                        startActivity(intent);
                    }else{
                        Toast.makeText(ResultActivity.this,"UUID does not match. Please try again.",Toast.LENGTH_LONG).show();
                    }


                    break;
            }
        }

    }


}
