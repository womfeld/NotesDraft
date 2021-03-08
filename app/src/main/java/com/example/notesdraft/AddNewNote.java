package com.example.notesdraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewNote extends AppCompatActivity {


    private static final String TAG = "AddNewNote" ;
    private Note n;
    private EditText newName;
    private EditText newDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Android Notes");

        setContentView(R.layout.activity_add_new_note);
        newName = findViewById(R.id.editName);
        newDescription = findViewById(R.id.editDescription);


        //Notice this is a get intent
        Intent intent = getIntent();

        if (intent.hasExtra("Note")) {
            //n is the value of the Note with key "Note" we passed in the OnClick intent
            n = (Note) intent.getSerializableExtra("Note");
            if (n != null) {
                //Retrieve name and description from recycler view cell
                String textName = n.getName();
                String textDescription = n.getDescription();

                //Set these names and descriptions in add note/edit activity page
                newName.setText(textName);
                newDescription.setText(textDescription);
            }
            else {
                Toast.makeText(this, "Could not find note.",Toast.LENGTH_SHORT).show();
            }

        }


        //setTitle("Whatever");;

    }


    //When we want to return to the main activity view, we create a new note using
    //the textfields we edited in the AddNewNote acitivity layout.  We then put
    //this note into an intent

    // The new note in this intent is then used in the onActivityResult method in
    //Main activity;
    // The onAcitivity result is responsible for regenerating the main
    //activity page (in this case the recycler view) with the new information received from the
    //AddNewNote acitivity page

    public void doReturn(View v) {


        //Have to account for if edit text has not been changed

        String editedName = newName.getText().toString();
        String editedDescription = newDescription.getText().toString();

        Note newN = new Note(editedName, editedDescription);

        Intent intent = new Intent();
        //This is a different intent than previous intents used
        intent.putExtra("Note", newN);

        setResult(RESULT_OK, intent);
        finish();
    }


    public void discardChanges(){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }


    public void displaySavingError() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Warning - a note without a title cannot be saved.  Discard changes?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                discardChanges();

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    //When save button is hit


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId()==R.id.save_icon) {

            if (newName.getText().toString().isEmpty()) {
                displaySavingError();
            }

            else {
                doReturn(null);
            }

        }


        return super.onOptionsItemSelected(item);

    }





    //Comment below code out for demonstration purposes


    //Put this function into save button later; doing on back pressed for testing purposes

    //Interesting note: all activities that start new activities can also receive data back from those activities



    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Your note is not saved.  Would you like to save it?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (newName.getText().toString().isEmpty()) {
                    displaySavingError();
                }
                else {
                    doReturn(null);
                }

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                discardChanges();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        //Fall back on
        //doReturn(null);
        //super.onBackPressed();
    }


}