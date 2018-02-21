package com.notesapp.activities.notes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.notesapp.IO.Toaster;
import com.notesapp.R;
import com.notesapp.models.Note;
import com.notesapp.utils.Session;

/**
 * Created by gery on 12Sep2017.
 */

public class SingleNoteFragment extends Fragment implements View.OnClickListener {
    private TextView txtTitle, txtContent;
    private Note note;

    public SingleNoteFragment() {
    }

    public static SingleNoteFragment newInstance(Note note) {
        SingleNoteFragment fr = new SingleNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable("note", note);
        fr.setArguments(args);
        return fr;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        this.note = args.getParcelable(getString(R.string.note));
        View view = inflater.inflate(R.layout.fragment_note_view, container, false);
        setViews(view);
        return view;
    }

    public void setViews(View view) {
        txtTitle = (TextView)view.findViewById(R.id.txtTitleSingleView);
        txtContent = (TextView)view.findViewById(R.id.txtContentSingleView);
        if(this.note != null) {
            txtTitle.setText(this.note.getTitle());
            txtContent.setText(this.note.getContent());
        }
        txtTitle.setOnClickListener(this);
        txtContent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!this.note.getCreator().equals(Session.getActiveUser().getUsername())) {
            Toaster.toastShort(getActivity().getApplicationContext(), "You can not edit a note created from another user.");
            return;
        }
        Toaster.toastShort(getActivity().getApplicationContext(),
                getString(R.string.enter_editing_mode));
        ((NotesActivity)getActivity())
                .replaceWithEditFragment(this.note);
    }
}