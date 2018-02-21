package com.notesapp.baas;

import com.notesapp.models.Note;
import com.notesapp.services.listeners.OnDataDeleted;
import com.notesapp.services.listeners.OnDataListener;
import com.notesapp.services.listeners.OnDataSaved;

/**
 * Created by gery on 07Sep2017.
 */

public interface NoteRepo {
    void setOnDataListener(OnDataListener callBack);
    void setOnDataSavedListener(OnDataSaved callBack);
    void setOnDataDeleted(OnDataDeleted onDataDeleted);
    void save(Note note);
    void findAll();
    void findOne(String id);
    void delete(Note note);
}
