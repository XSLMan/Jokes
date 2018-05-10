package com.along.project.jokes.ui.main;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.along.project.jokes.R;
import com.along.project.jokes.base.BaseActivity;
import com.along.project.jokes.utils.MPermissionUtils;
import com.along.project.jokes.view.LoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainContract.View, MainContract.Present<MainContract.View>> implements MainContract.View {

    @BindView(R.id.bt_upload1)
    Button mButtonNormalUpLoad;
    @BindView(R.id.bt_upload2)
    Button mButtonServiceUpload;
    @BindView(R.id.bt_login)
    Button mButtonLogin;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {
        setOrChangeTranslucentColor(toolbar, null, getResources().getColor(R.color.primss));
    }

    @Override
    public void initDataAfter() {
        MPermissionUtils.requestPermissionsResult(this, 2, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied() {
                MPermissionUtils.showTipsDialog(mContext);
            }
        });
    }

    @Override
    public void setListener() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected MainPresent<MainContract.View> createPresent() {
        return new MainPresent<>(mContext);
    }

    @Override
    public void showDialog() {
        LoadingDialog.show(mContext);
    }

    @Override
    public void dismissDialog() {
        LoadingDialog.dismiss(mContext);
    }

    @OnClick(R.id.bt_login)
    public void loginClick() {

    }
}
