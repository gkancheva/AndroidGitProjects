package com.notesapp.services;
import com.kinvey.android.Client;
import com.notesapp.baas.NoteRepo;
import com.notesapp.baas.NoteRepoImpl;
import com.notesapp.models.Note;
import com.notesapp.models.NoteDto;
import com.notesapp.services.listeners.OnDataDeleted;
import com.notesapp.services.listeners.OnDataListener;
import com.notesapp.services.listeners.OnDataSaved;
import com.notesapp.validators.NoteValidator;

/**
 * Created by gery on 05Sep2017.
 */

public class NoteServiceImpl implements NoteService {
    private NoteRepo noteRepo;

    public NoteServiceImpl(Client client) {
        this.noteRepo = new NoteRepoImpl(client);
    }


    @Override
    public void setOnDataSavedListeners(OnDataSaved onDataSavedListener) {
        this.noteRepo.setOnDataSavedListener(onDataSavedListener);
    }

    @Override
    public void setOnDataListener(OnDataListener listener) {
        this.noteRepo.setOnDataListener(listener);
    }

    @Override
    public void setOnDataDeletedListener(OnDataDeleted listener) {
        this.noteRepo.setOnDataDeleted(listener);
    }

    @Override
    public void save(NoteDto noteDto) {
        NoteValidator validator = new NoteValidator(noteDto);
        this.noteRepo.save(validator.getNote());
    }

    @Override
    public void findAll() {
        this.noteRepo.findAll();
    }

    @Override
    public void find(String noteId) {
        noteRepo.findOne(noteId);
    }

    @Override
    public void delete(Note note) {
        this.noteRepo.delete(note);
    }

}
