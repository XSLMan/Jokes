package com.along.project.jokes.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.along.project.jokes.base.MyApplication;

/**
 * Created by 16419 on 2018/5/10.
 */

public class ToastUtils {
    private static Context context = MyApplication.getInstance();
    private static Toast toast;

    public static void showToast(int resID) {
        showToast(context, Toast.LENGTH_SHORT, resID);
    }

    public static void showToast(String text) {
        showToast(context, Toast.LENGTH_SHORT, text);
    }

    public static void showLongToast(int resID) {
        showToast(context, Toast.LENGTH_LONG, resID);
    }


    public static void showLongToast(String text) {
        showToast(context, Toast.LENGTH_LONG, text);
    }

    private static void showToast(Context ctx, String text) {
        showToast(ctx, Toast.LENGTH_LONG, text);
    }

    private static void showToast(Context ctx, int duration, int resID) {
        showToast(ctx, duration, ctx.getString(resID));
    }


    private static void showToast(final Context ctx, final int duration, final String text) {

        if (text == null || text.equals("")) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(ctx, text, duration);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 在UI线程运行弹出
     */
    public static void showToastOnUiThread(final Activity ctx, final String text) {
        if (ctx != null) {
            ctx.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(ctx, text);
                }
            });
        }
    }
}
