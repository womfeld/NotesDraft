package com.example.notesdraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {


    private static final String TAG = "Main Activity";
    //A request code represents the request for a specfic activity.
    // In this case, only one is needed (since we are only getting back data from the
    // AddNewNote page, but if there were multiple activities, then multiple request
    // codes would be needed.

    private static final int A_REQUEST_CODE = 0;
    private static final int B_REQUEST_CODE = 1;
    private final ArrayList<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteListAdapter nAdapter;

    private Note n;
    private String tempName;
    private String tempDescription;

    //private TextView noteName;


    //Testing purposes, delete later



    //Haven't done onCreate yet
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);


        noteList.addAll(loadFile());
        nAdapter = new NoteListAdapter(noteList, this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setTitle("Android Notes " + "(" + noteList.size() + ")");

    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        noteList.clear();
        noteList.addAll(loadFile());
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        if (item.getItemId()==R.id.add_icon) {

            Intent intent = new Intent(this, AddNewNote.class);
            //startActivity(intent);
            startActivityForResult(intent, A_REQUEST_CODE);

        }

        else if (item.getItemId()==R.id.info_icon) {

            Intent intent = new Intent(this, Info.class);
            startActivity(intent);

        }



        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        n = noteList.get(pos);


        //Preliminary Edit note

        //Brings us to AddNewNote class view on click
        Intent intent = new Intent(this, AddNewNote.class);

        //Saves note name into key "Note Name"
        intent.putExtra("Note", n);


        //Just added
        tempName = n.getName();
        tempDescription = n.getDescription();

        //Might need to add extra intents to send these over to AddNewNote class


        //Saves position in index
        intent.putExtra("Position", pos);

        //Launches AddNewNote activity
        startActivityForResult(intent, B_REQUEST_CODE);

    }




    //Called when we go back to the main activity (the recycle view) from the add new note window


    //Have to account for if edit text has not been changed

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == B_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.hasExtra("Note")) {

                    //Instead of putting the note back at the same index, delete the
                    //one at that index and then put the modified note at the top


                    //nP is the new Note retrieved from the doReturn function in AddNewNote
                    Note nP = (Note) data.getSerializableExtra("Note");



                    //Detects whether the note was changed or not

                    if (nP.getName().equals(tempName) && nP.getDescription().equals(tempDescription)) {

                        ;
                    }

                    else {

                        int idx = noteList.indexOf(n);

                        removeNote(idx);
                        addNoteTop(nP);
                        saveNote();

                    }

                }
            }

            //Shows if note wasn't saved
            if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Changes were not saved.",Toast.LENGTH_SHORT).show();

            }

        }
        //Just added
        else if (requestCode == A_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.hasExtra("Note")) {

                    Note nP = (Note) data.getSerializableExtra("Note");
                    addNoteTop(nP);
                    saveNote();

                }

            }

            //Shows if note wasn't saved
            if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Note was not created.",Toast.LENGTH_SHORT).show();

            }

        }
    }





    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note n = noteList.get(pos);



        //Just added

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Delete?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noteList.remove(n);
                saveNote();
                nAdapter.notifyDataSetChanged();
                setTitle("Android Notes " + "(" + noteList.size() + ")");

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


        return false;
    }


    public void addNoteTop(Note nt) {
        noteList.add(0, nt);
        nAdapter.notifyDataSetChanged();
        setTitle("Android Notes " + "(" + noteList.size() + ")");
    }

    public void addEnd(View v) {
        noteList.add(new Note());
        nAdapter.notifyDataSetChanged();
    }

    public void removeNote(int idx) {
        if (!noteList.isEmpty()) {
            noteList.remove(idx);
            nAdapter.notifyDataSetChanged();
        }
    }

    public void removeEnd(View v) {
        if (!noteList.isEmpty()) {
            noteList.remove(noteList.size()-1);
            nAdapter.notifyDataSetChanged();
        }
    }




    /*


    //When app is resumed, must clear and put file contents into
    //application again
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {




    //When paused, must add contents to noteList then save

    @Override
    protected void onPause() {

        //Makes sure name and description for notes are not empty
        //Before adding to notelist array
        if (!name1.getText().toString().isEmpty() && !description1.toString().isEmpty()) {
            Note p = new Note(name1.getText().toString(), description1.getText().toString());
            noteList.add(p);
        }
        if (!name2.getText().toString().isEmpty() && !description2.toString().isEmpty()) {
            Note p = new Note(name2.getText().toString(), description2.getText().toString());
            noteList.add(p);
        }
        if (!name3.getText().toString().isEmpty() && !description3.toString().isEmpty()) {
            Note p = new Note(name3.getText().toString(), description3.getText().toString());
            noteList.add(p);
        }
        saveNote();
        super.onPause();




    */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<Note> loadFile() {

        ArrayList<Note> noteList = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput("Note.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }


            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("Name");
                String desc = jsonObject.getString("Description");
                String ts = jsonObject.getString("Timestamp");
                //Note note = new Note(name, desc, ts);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    noteList.add(new Note(name,desc, ts));
                }
            }

        } catch (FileNotFoundException e) {
            //Toast.makeText(this, "No JSON Note File Present", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return noteList;
    }




    private void saveNote() {


        //This creates the JSON file to put the name and description contents in


        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(noteList);
            printWriter.close();
            fos.close();




        } catch (Exception e) {
            e.getStackTrace();
        }

    }


}
