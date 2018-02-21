package com.notesapp.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.notesapp.R;
import com.notesapp.models.Note;
import com.notesapp.services.listeners.OnItemClickListener;

import java.util.List;

/**
 * Created by gery on 05Sep2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>{
    private List<Note> notes;
    private OnItemClickListener listener;

    public NotesAdapter(List<Note> notes, OnItemClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_row, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.getTitle().setText(note.getTitle());
        holder.getContent().setText(note.getContent());
        holder.getCreator().setText(note.getCreator());
    }

    @Override
    public int getItemCount() {
        return this.notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private TextView title;
        private TextView content;
        private TextView creator;

        public NoteViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.txtTitle);
            content = (TextView)view.findViewById(R.id.txtContent);
            creator = (TextView)view.findViewById(R.id.txtCreator);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getContent() {
            return content;
        }

        public TextView getCreator() {
            return creator;
        }

        @Override
        public void onClick(View v) {
            listener.onNoteSelected(v, getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onNoteLongClicked(v, getLayoutPosition());
            return true;
        }
    }

}
