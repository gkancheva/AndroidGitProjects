package com.notesapp.validators;

import android.widget.EditText;

import com.notesapp.App;
import com.notesapp.R;
import com.notesapp.models.Note;
import com.notesapp.models.NoteDto;
import com.notesapp.utils.NoteAppException;
import com.notesapp.utils.Session;

/**
 * Created by gery on 05Sep2017.
 */

public class NoteValidator {
    private Note note;

    public NoteValidator(NoteDto noteDto) {
        this.note = new Note();
        if(null != noteDto.getId()) {
            this.note.setId(noteDto.getId());
        }
        this.setTitle(noteDto.getTitle());
        this.setContent(noteDto.getContent());
    }

    public void setTitle(EditText title) {
        if(title == null || title.getText().toString().isEmpty()) {
            throw new NoteAppException(App.getMessageParser().getMessage(R.string.title_required));
        }
        this.note.setTitle(title.getText().toString());
    }

    public void setContent(EditText content) {
        if(content == null || content.getText().toString().isEmpty()) {
            throw new NoteAppException(App.getMessageParser().getMessage(R.string.content_required));
        }
        this.note.setContent(content.getText().toString());
    }

    public Note getNote() {
        String creatorUsername = Session.getActiveUser().getUsername();
        note.setCreator(creatorUsername);
        return note;
    }
}
