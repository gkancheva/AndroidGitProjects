package com.notesapp.IO;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by gery on 07Sep2017.
 */

public class Toaster {

    private Toaster() {
    }

    public static void toastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void toastShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
