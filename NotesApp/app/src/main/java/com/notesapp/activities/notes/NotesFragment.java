package com.notesapp.activities.notes;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kinvey.android.Client;
import com.notesapp.App;
import com.notesapp.IO.Toaster;
import com.notesapp.R;
import com.notesapp.models.Note;
import com.notesapp.services.NoteService;
import com.notesapp.services.NoteServiceImpl;
import com.notesapp.services.listeners.OnDataDeleted;
import com.notesapp.services.listeners.OnDataListener;
import com.notesapp.services.listeners.OnItemClickListener;
import com.notesapp.utils.NotesAdapter;
import com.notesapp.utils.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gery on 12Sep2017.
 */

public class NotesFragment extends Fragment implements
        OnDataListener, OnDataDeleted, OnItemClickListener, View.OnClickListener {
    private List<Note> notes;
    private RecyclerView recyclerView;
    private FloatingActionButton btnAddNote;
    private NoteService noteService;
    private NotesAdapter adapter;

    public NotesFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        btnAddNote = (FloatingActionButton)view.findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(this);
        this.notes = new ArrayList<>();
        Client client = ((App)getActivity().getApplicationContext()).getSharedClient();
        noteService = new NoteServiceImpl(client);
        noteService.setOnDataListener(this);
        this.noteService.setOnDataDeletedListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NotesAdapter(this.notes, this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDataReceived(List<Note> noteList) {
        for (Note note : noteList) {
            this.notes.add(note);
        }
        if(this.notes.size() == 0) {
            Toaster.toastShort(getActivity().getApplicationContext(),
                    getString(R.string.no_notes_to_show));
        }
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onDataError(Throwable t, String message) {
        Toaster.toastLong(getActivity().getApplicationContext(), message);
    }

    @Override
    public void onDeleteSuccess(Note note) {
        Toaster.toastShort(getActivity().getApplicationContext(), String.format("'%s' deleted successfully!", note.getTitle()));
        this.notes.remove(note);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteFailure(Throwable t, String message) {
        Toaster.toastShort(getActivity().getApplicationContext(), message);
    }

    @Override
    public void onNoteSelected(View v, int position) {
        ((NotesActivity)getActivity())
                .replaceWithViewFragment(this.notes.get(position));
    }

    @Override
    public void onNoteLongClicked(View v, int position) {
        final Note noteToDelete = this.notes.get(position);
        if(!noteToDelete.getCreator().equals(Session.getActiveUser().getUsername())) {
            Toaster.toastShort(getActivity().getApplicationContext(),
                    getString(R.string.cannot_delete_note_other_user));
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(String.format(getString(R.string.do_you_want_to_delete_note), noteToDelete.getTitle()))
                .setCancelable(true)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.deleting_note)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote(noteToDelete);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.noteService.findAll();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnAddNote) {
            ((NotesActivity)getActivity()).replaceWithAddNoteActivity();
        }
    }

    private void deleteNote(Note noteToDel) {
        this.noteService.delete(noteToDel);
    }
}
