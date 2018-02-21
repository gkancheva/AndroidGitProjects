package com.notesapp.services.listeners;

import android.view.View;

/**
 * Created by gery on 08Sep2017.
 */

public interface OnItemClickListener {
    void onNoteSelected(View v, int position);
    void onNoteLongClicked(View v, int position);
}
