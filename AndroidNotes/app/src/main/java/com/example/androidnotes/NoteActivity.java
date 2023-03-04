package com.example.androidnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private EditText title;
    private EditText text;

    public Note editNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        title = findViewById(R.id.note_title);
        text = findViewById(R.id.note_text);
        text.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        if (intent.hasExtra("EDIT_NOTE")) {
            editNote = (Note) intent.getSerializableExtra("EDIT_NOTE");
            title.setText(editNote.getNoteTitle());
            text.setText(editNote.getNoteText());
        }
    }

    public void doSave(MenuItem m) {
        String titleText = title.getText().toString();
        String textText = text.getText().toString();
        String date = String.valueOf(new Date());
        Note n = new Note(titleText, textText, date);

//        if(titleText.isEmpty() && textText.isEmpty()){
//            NoteActivity.super.onBackPressed();
//        }

        if((titleText.trim()).isEmpty() || titleText.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
            //builder.setMessage("Note will not be saved without a title");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    NoteActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setTitle("No Title");
            builder.setMessage("Note will not be saved without a title");
            AlertDialog dialog = builder.create();
            dialog.show();
            Toast.makeText(this, "You cannot save note without a title!", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = "NEW_NOTE";

        Intent intent = getIntent();
        if (intent.hasExtra("EDIT_NOTE")) {
            key = "UPDATE_NOTE";
        }

        Intent data = new Intent();
        data.putExtra(key, n);
        if (intent.hasExtra("EDIT_POS")) {
            int pos = intent.getIntExtra("EDIT_POS", 0);
            data.putExtra("UPDATE_POS", pos);
        }
        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public void onBackPressed() {
        String titleText = title.getText().toString();
        String textText = text.getText().toString();

        if(editNote == null){
            if((titleText.trim()).isEmpty() && (textText.trim()).isEmpty()){
                super.onBackPressed();
                return;
            }
        }

        if (editNote != null) {
            if (editNote.getNoteTitle().equals(titleText) &&
                    editNote.getNoteText().equals(textText)) {
                super.onBackPressed();
                return;
            }
        }

        if((titleText.trim()).isEmpty()){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(NoteActivity.this);
            //builder.setMessage("Note will not be saved without a title");
            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    NoteActivity.super.onBackPressed();
                }
            });
            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder1.setTitle("No Title");
            builder1.setMessage("Note will not be saved without a title");
            AlertDialog dialog1 = builder1.create();
            dialog1.show();
            Toast.makeText(this, "You cannot save note without a title!", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doSave(null);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NoteActivity.super.onBackPressed();
            }
        });
        builder.setTitle("Unsaved Changes");
        builder.setMessage("Do you want to save your changes before exiting?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_notes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}