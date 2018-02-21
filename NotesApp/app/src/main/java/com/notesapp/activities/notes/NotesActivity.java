package com.notesapp.activities.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.notesapp.R;
import com.notesapp.models.Note;
import com.notesapp.utils.ProgrBar;

public class NotesActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ProgrBar progrBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progrBar = new ProgrBar(progressBar);
        progrBar.start();
        showNotesFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showNotesFragment() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        }
        NotesFragment fr = new NotesFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fr_container, fr, "")
                .commit();
    }

    public void replaceWithAddNoteActivity() {
        AddNoteFragment fr = new AddNoteFragment();
        getFragmentManager().popBackStackImmediate();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fr_container, fr)
                .addToBackStack("notes_fragment")
                .commit();
    }

    public void replaceWithEditFragment(Note note) {
        getFragmentManager().popBackStackImmediate();
        EditNoteFragment fr = EditNoteFragment.newInstance(note);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fr_container, fr, "")
                .addToBackStack(null)
                .commit();
    }

    public void replaceWithViewFragment(Note note) {
        SingleNoteFragment fr = SingleNoteFragment.newInstance(note);
        getFragmentManager().popBackStackImmediate();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fr_container, fr, "")
                .addToBackStack(null)
                .commit();
    }
}
