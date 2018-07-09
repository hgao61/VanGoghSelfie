package com.poptek.picture.activity;

import android.app.Application;

import org.xutils.x;

/**
 * Created by PopTek on 2017/11/17.
 */

public class MyApplication extends Application{


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);

    }
}
