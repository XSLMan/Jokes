package com.along.project.jokes.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.LongSparseArray;

import com.along.project.jokes.utils.ContentManager;
import com.along.project.jokes.utils.IOUtils;
import com.along.project.jokes.utils.InstallUtil;
import com.along.project.jokes.utils.LogUtils;
import com.along.project.jokes.utils.SystemManager;

import java.io.File;

/**
 * Created by 16419 on 2018/5/10.
 */

public class DownloadService extends Service {
    private DownloadManager mDownloadManager;
    private DownloadBinder mBinder = new DownloadBinder();
    private LongSparseArray<String> mApkPaths;
    private boolean mIsRoot = false;
    private DownloadFinishReceiver mReceiver;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        mApkPaths = new LongSparseArray<>();
        //注册下载完成的广播
        mReceiver = new DownloadFinishReceiver();
        registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);//取消注册广播接收者
        super.onDestroy();
    }

    public class DownloadBinder extends Binder {

        /**
         * 下载
         *
         * @param apkUrl 下载的url
         */
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public long startDownload(String apkUrl) {
            //点击下载
            //删除原有的APK
            IOUtils.clearApk(DownloadService.this, ContentManager.APK_NAME);
            //使用DownLoadManager来下载
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
            //将文件下载到自己的Download文件夹下,必须是External的
            //这是DownloadManager的限制
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), ContentManager.APK_NAME);
            request.setDestinationUri(Uri.fromFile(file));


            //添加请求 开始下载
            long downloadId = mDownloadManager.enqueue(request);
            LogUtils.d("DownloadBinder", file.getAbsolutePath());
            mApkPaths.put(downloadId, file.getAbsolutePath());
            return downloadId;
        }

        public void setInstallMode(boolean isRoot) {
            mIsRoot = isRoot;
        }

        /**
         * 获取进度信息
         *
         * @param downloadId 要获取下载的id
         * @return 进度信息 max-100
         */
        public int getProgress(long downloadId) {
            //查询进度
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
            Cursor cursor = null;
            int progress = 0;
            try {
                cursor = mDownloadManager.query(query);//获得游标
                if (cursor != null && cursor.moveToFirst()) {
                    //当前的下载量
                    int downloadSoFar = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    //文件总大小
                    int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    progress = (int) (downloadSoFar * 1.0f / totalBytes * 100);
                }
            } finally {
                if (cursor != null) {

                    cursor.close();
                }
            }

            return progress;
        }

    }

    //下载完成的广播
    private class DownloadFinishReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            //下载完成的广播接收者
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String apkPath = mApkPaths.get(completeDownloadId);
            LogUtils.d("DownloadFinishReceiver", apkPath);
            if (!apkPath.isEmpty()) {
                SystemManager.setPermission(apkPath);//提升读写权限,否则可能出现解析异常
                InstallUtil.install(context, apkPath, mIsRoot);
            } else {
                LogUtils.e("DownloadFinishReceiver", "apkPath is null");
            }
        }
    }
}
