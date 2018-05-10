package com.along.project.jokes.base;

import android.app.Application;

/**
 * Created by 16419 on 2018/5/10.
 */

public class MyApplication extends Application {

    //全局的Application对象
    public static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }
}
