package com.notesapp.models;

import android.widget.EditText;

/**
 * Created by gery on 07Sep2017.
 */

public class NoteDto {
    private String id;
    private EditText title;
    private EditText content;

    public NoteDto(String id, EditText title, EditText content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public EditText getTitle() {
        return title;
    }

    public EditText getContent() {
        return content;
    }

    public String getId() {
        return id;
    }
}
