package com.notesapp.services.listeners;

import com.notesapp.models.Note;

import java.util.List;

/**
 * Created by gery on 05Sep2017.
 */

public interface OnDataListener {
    void onDataReceived(List<Note> notes);
    void onDataError(Throwable t, String message);
}
