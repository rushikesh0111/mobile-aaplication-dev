package com.example.androidnotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private static final String TAG = "NotesAdapter";
    private final List<Note> noteList;
    private final MainActivity mainAct;

    NotesAdapter(List<Note> noteList1, MainActivity ma) {
        this.noteList = noteList1;
        mainAct = ma;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Note " + position);

        Note note = noteList.get(position);

        String sub = note.getNoteText();
        if(sub.length()>80){
            sub = sub.substring(0,80)+"...";
        }

        holder.noteTitle.setText(note.getNoteTitle());
        holder.noteText.setText(sub);
        holder.dateTime.setText(new Date().toString());
    }

    @Override
    public int getItemCount() {return noteList.size();}
}
