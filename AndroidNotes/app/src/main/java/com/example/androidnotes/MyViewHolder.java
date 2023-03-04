package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView noteTitle;
    TextView noteText;
    TextView dateTime;

    MyViewHolder(View view) {
        super(view);
        noteTitle = view.findViewById(R.id.noteTitle);
        noteText = view.findViewById(R.id.noteText);
        dateTime = view.findViewById(R.id.dateTime);
    }

}
