package com.notesapp.activities.notes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class AddNoteFragment extends Fragment implements OnDataSaved, View.OnClickListener {
    private EditText etTitle, etContent;
    private Button btnSave;
    private Client client;
    private NoteService noteService;
    private View views;

    public AddNoteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);
        etTitle = (EditText)view.findViewById(R.id.etTitle);
        btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        etTitle = (EditText)view.findViewById(R.id.etTitle);
        etContent = (EditText)view.findViewById(R.id.etContent);
        this.client = ((App)getActivity().getApplicationContext()).getSharedClient();
        noteService = new NoteServiceImpl(client);
        noteService.setOnDataSavedListeners(this);
        return view;
    }

    @Override
    public void onDataError(Throwable t, String message) {
        Toaster.toastShort(getActivity().getApplicationContext(), message);
    }

    @Override
    public void onDataSaved(Note note, String message) {
        Toaster.toastShort(getActivity().getApplicationContext(),
                String.format("'%s' %s", note.getTitle(), message));
        ((NotesActivity)getActivity()).showNotesFragment();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave) {
            try {
                NoteDto noteDto = new NoteDto(null, etTitle, etContent);
                noteService.save(noteDto);
            } catch (Exception e) {
                Toaster.toastShort(getActivity().getApplicationContext(), e.getMessage());
            }
        }
    }
}