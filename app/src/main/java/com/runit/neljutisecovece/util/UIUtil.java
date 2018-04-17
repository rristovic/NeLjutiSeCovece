package com.runit.neljutisecovece.util;

import android.content.Context;
import android.widget.Toast;

/**
 * UI Utils class for showing dialogs and toasts.
 */
public class UIUtil {
    public static void showToast(Context context, String msg) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, String msg) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
