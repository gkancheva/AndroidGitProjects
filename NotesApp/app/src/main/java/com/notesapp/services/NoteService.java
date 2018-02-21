package com.notesapp.services;

import com.notesapp.models.Note;
import com.notesapp.models.NoteDto;
import com.notesapp.services.listeners.OnDataDeleted;
import com.notesapp.services.listeners.OnDataListener;
import com.notesapp.services.listeners.OnDataSaved;

/**
 * Created by gery on 05Sep2017.
 */

public interface NoteService {
    void setOnDataSavedListeners(OnDataSaved onDataSavedListener);
    void setOnDataListener(OnDataListener listener);
    void setOnDataDeletedListener(OnDataDeleted listener);
    void save(NoteDto noteDto);
    void findAll();
    void find(String noteId);
    void delete(Note note);
}
