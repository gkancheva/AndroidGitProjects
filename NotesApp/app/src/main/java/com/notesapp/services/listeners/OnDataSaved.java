package com.notesapp.services.listeners;

import com.notesapp.models.Note;

/**
 * Created by gery on 07Sep2017.
 */

public interface OnDataSaved {
    void onDataError(Throwable t, String message);
    void onDataSaved(Note note, String message);
}
