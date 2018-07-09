package com.poptek.picture.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.poptek.picture.R;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;
import org.zackratos.ultimatebar.UltimateBar;

import java.io.File;
import java.util.List;

import static android.os.Environment.getDataDirectory;

/**
 * Created by PopTek on 2017/11/17.
 */

public class PicturePreview extends Activity{

    private WebView webview;
    private Button retry,save,cancel;

    private String url = "";//http://img.1985t.com/uploads/attaches/2017/11/136746-FEBNlm3.jpg
    private String UUID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picture_preview);

        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar(true);

        Intent intent = getIntent();
        url = intent.getStringExtra("imageUrl");
        UUID = intent.getStringExtra("uuid");
        if(url==""){
            Toast.makeText(PicturePreview.this,"Return link is empty. Please try again.",Toast.LENGTH_LONG);
            finish();
        }

        init();
    }


    private void init(){
        webview = findViewById(R.id.webview);

        //声明WebSettings子类
        WebSettings webSettings = webview.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
//如果不设置WebViewClient，请求会跳转系统浏览器
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                view.loadUrl(url);
                return true;

            }

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
//            {
//                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
//                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    if (request.getUrl().toString().contains("fsm.gov.mo")){
//                        view.loadUrl(Url);
//                        return true;
//                    }
//                }
//
//                return false;
//            }

        });
        webview.loadUrl(url);



        retry = findViewById(R.id.retry);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PicturePreview.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downImage();
            }
        });



    }


    private void downImage(){
             /*
       获取保存路径：手机SD卡1存储 storage/sdcard/Android/data/应用的包名/files
       Genymotion模拟器的路径：/storage/emulated/0/Android/data/com.atguigu.zhuatutu/files
        */


        File filesDir = Environment.getExternalStoragePublicDirectory("");
        //获取文件名:/february_2016-001.jpg
        String fileName = url.substring(url.lastIndexOf("/"));
        //存到本地的绝对路径
        final String filePath = filesDir + "/Picture" + fileName;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"Picture");
        //如果不存在
        if (!file.exists()) {
            //创建
            file.mkdirs();
        }

        RequestParams entity = new RequestParams(url);
        entity.setSaveFilePath(filePath);
        x.http().get(entity, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
//                filesPath.add(result.getAbsolutePath());
                LogUtil.e("onSuccess：" + result.getAbsolutePath());
                Toast.makeText(x.app(), "Download successfully,In the folder named Picture" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError ");
                Toast.makeText(x.app(),"Download failed:"+ex.toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled ");
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished ");
//                Toast.makeText(x.app(), "下载成功" , Toast.LENGTH_SHORT).show();
            }
        });


    }

}
