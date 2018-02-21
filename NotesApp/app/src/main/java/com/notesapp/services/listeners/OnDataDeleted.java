package com.notesapp.services.listeners;

import com.notesapp.models.Note;

/**
 * Created by gery on 11Sep2017.
 */

public interface OnDataDeleted {
    void onDeleteSuccess(Note note);
    void onDeleteFailure(Throwable t, String message);
}
