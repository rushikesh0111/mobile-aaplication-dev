package com.example.androidnotes;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Note implements Serializable {

    private String noteTitle;
    private String noteText;
    private String date;


    public Note(String noteTitle, String noteText, String date) {
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.date = date;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public String getNoteDate() {
        return noteText;
    }
    @NonNull
    @Override
    public String toString() {
        return noteTitle + noteText;
    }
}
