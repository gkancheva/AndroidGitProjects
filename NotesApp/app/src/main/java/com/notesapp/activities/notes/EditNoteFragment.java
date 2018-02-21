package com.notesapp.activities.notes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kinvey.android.Client;
import com.notesapp.App;
import com.notesapp.IO.Toaster;
import com.notesapp.R;
import com.notesapp.models.Note;
import com.notesapp.models.NoteDto;
import com.notesapp.services.NoteService;
import com.notesapp.services.NoteServiceImpl;
import com.notesapp.services.listeners.OnDataSaved;

/**
 * Created by gery on 12Sep2017.
 */

public class EditNoteFragment extends Fragment implements View.OnClickListener, OnDataSaved {
    private EditText etTitle, etContent;
    private FloatingActionButton btnSave;
    private NoteService noteService;
    private Note note;

    public EditNoteFragment() {
    }

    public static EditNoteFragment newInstance(Note note) {
        EditNoteFragment fr = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable("note", note);
        fr.setArguments(args);
        return fr;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Client client = ((App)getActivity().getApplicationContext())
                .getSharedClient();
        this.noteService = new NoteServiceImpl(client);
        this.noteService.setOnDataSavedListeners(this);
        Bundle args = getArguments();
        this.note = args.getParcelable(getString(R.string.note));
        View view = inflater.inflate(R.layout.fragment_note_edit, container, false);
        setViews(view);
        return view;
    }

    private void setViews(View view) {
        etTitle = (EditText)view.findViewById(R.id.etTitleSingleView);
        etContent = (EditText)view.findViewById(R.id.etContentSingleView);
        btnSave = (FloatingActionButton)view.findViewById(R.id.btnSaveEditedNote);
        etTitle.setText(this.note.getTitle());
        etContent.setText(this.note.getContent());
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSaveEditedNote) {
            try {
                NoteDto noteDto = new NoteDto(this.note.getId(), etTitle, etContent);
                noteService.save(noteDto);
            } catch (Exception e) {
                Toaster.toastShort(getActivity().getApplicationContext(),
                        e.getMessage());
            }
        }
    }

    @Override
    public void onDataError(Throwable t, String message) {
        Toaster.toastShort(getActivity().getApplicationContext(), message);
    }

    @Override
    public void onDataSaved(Note note, String message) {
        Toaster.toastShort(getActivity().getApplicationContext(),
                String.format("'%s' %s", note.getTitle(), message));
        ((NotesActivity)getActivity()).replaceWithViewFragment(note);
    }
}
