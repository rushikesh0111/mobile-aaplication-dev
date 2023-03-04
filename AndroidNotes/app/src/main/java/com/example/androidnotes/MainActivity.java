package com.example.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private final List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView; // Layout's recyclerview
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private final String TAG = getClass().getSimpleName();
    private NotesAdapter mAdapter;
    private JSONArray noteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Android Notes ("+noteList.size()+")");

        recyclerView = findViewById(R.id.recycler);
        // Data to recyclerview adapter
        mAdapter = new NotesAdapter(noteList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        for (int i = 0; i < 10; i++) {
//            noteList.add(new Note());
//        }

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::getNoteResult);
    }

    public void addNote(MenuItem v) {
        Intent intent = new Intent(this, NoteActivity.class);
        activityResultLauncher.launch(intent);
    }

    public void openAbout(MenuItem v) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void getNoteResult(ActivityResult activityResult) {
        Log.d(TAG, "getENoteResult: ");
        if (activityResult.getResultCode() == RESULT_OK) {
            Intent data = activityResult.getData();
            if (data == null)
                return;
            if (data.hasExtra("NEW_NOTE")) {
                Note newNote = (Note) data.getSerializableExtra("NEW_NOTE");
                noteList.add(0, newNote);
                mAdapter.notifyItemInserted(0);
            } else if (data.hasExtra("UPDATE_NOTE")) {
                Note editNote = (Note) data.getSerializableExtra("UPDATE_NOTE");
                int pos = data.getIntExtra("UPDATE_POS", 0);

                Note toUpdate = noteList.get(pos);
                toUpdate.setNoteTitle(editNote.getNoteTitle());
                toUpdate.setNoteText(editNote.getNoteText());
                noteList.remove(pos);
                noteList.add(0, toUpdate);
                mAdapter.notifyItemInserted(0);
            }
        }
        getSupportActionBar().setTitle("Android Notes ("+noteList.size()+")");
    }

    // From OnClickListener
    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        Note n = noteList.get(pos);

        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("EDIT_NOTE", n);
        intent.putExtra("EDIT_POS", pos);

        activityResultLauncher.launch(intent);
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        int pos = recyclerView.getChildLayoutPosition(v);
        Note n = noteList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noteList.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                getSupportActionBar().setTitle("Android Notes ("+noteList.size()+")");
                saveNote();
            }
        });
        builder.setNegativeButton("NO", (dialogInterface, i) -> {
        });
        builder.setTitle("Do you want to delete this note?");
        builder.setMessage("Delete note '" + n.getNoteTitle() + "'?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    public String saveToJSON() {
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jw = new JsonWriter(sw);
            jw.setIndent("  ");
            jw.beginArray();

            for(int i=0; i<(noteList.size()); i++){
                jw.beginObject();
                jw.name("title").value((noteList.get(i)).getNoteTitle());
                jw.name("description").value((noteList.get(i)).getNoteText());
                jw.name("date").value(String.valueOf((noteList.get(i)).getNoteDate()));
                jw.endObject();
            }
            jw.endArray();
            jw.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onResume() { // After Pause or Stop

        noteArray = loadFromJSON();
        Log.d(TAG, "onResume: noteArray: "+noteArray);
        mAdapter = new NotesAdapter(noteList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("Android Notes (" + String.valueOf(noteList.size()) + ")");
        saveNote();
        super.onResume();
    }

    private JSONArray loadFromJSON() {

        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());

            int lengthOfNoteList = noteList.size();
            for (int i = lengthOfNoteList; i < jsonArray.length(); i++) {
                JSONObject jb = jsonArray.getJSONObject(i);
                String title = jb.getString("title");
                String text = jb.getString("description");
                String date = jb.getString("date");
                noteList.add(new Note(title, text, date));
                Log.d(TAG, "loadFile: jsonobject" + title);
            }
            return jsonArray;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPause() {
        saveNote();
        super.onPause();
    }

    private void saveNote() {
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter pf = new PrintWriter(fos);
            pf.print(saveToJSON());


            pf.close();
            fos.close();
            //saveNote();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}