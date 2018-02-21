package com.notesapp.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by gery on 12Sep2017.
 */

public class MessageParser {
    private static Resources resources;

    public MessageParser(Context appCtx) {
        resources = appCtx.getResources();
    }

    public String getMessage(int messageId) {
        return resources.getString(messageId);
    }
}
