package com.example.administrator.utils;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @ClassName ToastUtil
 * @Date 2019-06-22 18:46
 **/

public class ToastUtil {
    private static Toast toast = null;
    public static void showToast(Context context, String text) {
        Looper myLooper = Looper.myLooper();
        if (myLooper == null) {
            Looper.prepare();
            myLooper = Looper.myLooper();
        }

        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.show();
        if ( myLooper != null) {
            Looper.loop();
            myLooper.quit();
        }

    }
}
