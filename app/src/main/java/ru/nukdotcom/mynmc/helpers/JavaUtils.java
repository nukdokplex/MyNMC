package ru.nukdotcom.mynmc.helpers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class JavaUtils {
    public static void backgroundToast(final Context context,
                                       final String msg,
                                       int length) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, length).show();
                }
            });
        }
    }
}
