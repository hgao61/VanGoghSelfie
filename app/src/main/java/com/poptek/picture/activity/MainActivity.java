package com.poptek.picture.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.poptek.picture.R;

import org.zackratos.ultimatebar.UltimateBar;

import java.util.ArrayList;

public class MainActivity extends TakePhotoActivity {
    private CustomHelper customHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView= LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        setContentView(contentView);
        customHelper=CustomHelper.of(contentView);
//        UltimateBar ultimateBar = new UltimateBar(this);
//        ultimateBar.setColorStatusBar(ContextCompat.getColor(this, R.color.white));
        UltimateBar ultimateBar = new UltimateBar(this);
        ultimateBar.setImmersionBar(true);
    }




    public void onClick(View view) {
        customHelper.onClick(view,getTakePhoto());
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());

    }

    private void showImg(ArrayList<TImage> images) {
        Intent intent=new Intent(this,ResultActivity.class);
        intent.putExtra("images",images);
        startActivity(intent);
    }
}
