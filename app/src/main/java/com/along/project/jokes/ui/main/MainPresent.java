package com.along.project.jokes.ui.main;

import android.content.Context;

/**
 * Created by 16419 on 2018/5/10.
 */

public class MainPresent<T> extends MainContract.Present<MainContract.View> {

    private MainContract.Model mModel;
    private Context mContext;

    public MainPresent(Context mContext){
        this.mContext = mContext;
        mModel = new MainModel();
    }

    @Override
    public void fetch() {

    }

    @Override
    public void onDestroy() {

    }
}
