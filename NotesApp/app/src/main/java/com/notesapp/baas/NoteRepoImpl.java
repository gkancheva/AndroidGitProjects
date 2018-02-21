package com.notesapp.baas;

import android.util.Log;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.android.store.DataStore;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.store.StoreType;
import com.notesapp.App;
import com.notesapp.R;
import com.notesapp.models.Note;
import com.notesapp.services.listeners.OnDataDeleted;
import com.notesapp.services.listeners.OnDataListener;
import com.notesapp.services.listeners.OnDataSaved;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gery on 07Sep2017.
 */

public class NoteRepoImpl implements NoteRepo {
    private DataStore<Note> dataStore;
    private OnDataListener onDataListener;
    private OnDataSaved onDataSaved;
    private OnDataDeleted onDataDeleted;

    public NoteRepoImpl(Client client) {
        this.dataStore = DataStore.collection(
                App.getMessageParser().getMessage(R.string.collection_name),
                Note.class,
                StoreType.NETWORK, client);
    }

    @Override
    public void setOnDataListener(OnDataListener listener) {
        this.onDataListener = listener;
    }

    @Override
    public void setOnDataSavedListener(OnDataSaved listener) {
        this.onDataSaved = listener;
    }

    @Override
    public void setOnDataDeleted(OnDataDeleted onDataDeleted) {
        this.onDataDeleted = onDataDeleted;
    }

    @Override
    public void save (Note note) {
        if(this.onDataSaved != null) {
            this.dataStore.save(note, new KinveyClientCallback<Note>() {
                @Override
                public void onSuccess(Note note) {
                    onDataSaved.onDataSaved(note, App.getMessageParser().getMessage(R.string.successfully_saved_note));
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.i("ERROR NOTE SAVE", t.getMessage());
                    onDataSaved.onDataError(t, App.getMessageParser().getMessage(R.string.error_saving_note));
                }
            });
        }
    }

    @Override
    public void findAll() {
        if(this.onDataListener != null) {
            this.dataStore.find(new KinveyListCallback<Note>() {
                @Override
                public void onSuccess(List<Note> list) {
                    onDataListener.onDataReceived(list);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.i("ERROR NOTE FIND_ALL", t.getMessage());
                    onDataListener.onDataError(t, App.getMessageParser().getMessage(R.string.error_fetching_data));
                }
            });
        }
    }

    @Override
    public void findOne(String id) {
        if(this.onDataListener != null) {
            this.dataStore.find(id, new KinveyClientCallback<Note>() {
                @Override
                public void onSuccess(Note note) {
                    List<Note> notes = new ArrayList<Note>();
                    notes.add(note);
                    onDataListener.onDataReceived(notes);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.i("ERROR NOTE FIND ONE", t.getMessage());
                    onDataListener.onDataError(t, App.getMessageParser().getMessage(R.string.error_fetching_data));
                }
            });
        }
    }

    @Override
    public void delete(final Note note) {
        this.dataStore.delete(note.getId(), new KinveyDeleteCallback() {
            @Override
            public void onSuccess(Integer integer) {
                onDataDeleted.onDeleteSuccess(note);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("ERROR NOTE DELETE", t.getMessage());
                onDataDeleted.onDeleteFailure(t, App.getMessageParser().getMessage(R.string.error_deleting_note));
            }
        });
    }
}
