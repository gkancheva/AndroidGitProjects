package com.notesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by gery on 05Sep2017.
 */

public class Note extends GenericJson implements Parcelable {
    @Key("_id")
    private String id;
    @Key("title")
    private String title;
    @Key("content")
    private String content;
    @Key("creator_username")
    private String creator;

    public Note() {
    }

    protected Note(Parcel in) {
        id = in.readString();
        title = in.readString();
        content = in.readString();
        creator = in.readString();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Note note = (Note) o;

        return id != null ? id.equals(note.id) : note.id == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(creator);
    }
}
