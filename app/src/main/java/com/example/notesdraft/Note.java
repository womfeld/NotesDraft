package com.example.notesdraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Note implements Serializable {

    private final String name;
    private final String description;
    private final String timestamp;




    public Note() {
        DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        String hourString = dateFormat.format(new Date());
        SimpleDateFormat dF = new SimpleDateFormat("EEE, dd/MM/yyyy");
        String date = dF.format(new Date());
        this.name = "Note " ;//+ ctr;
        this.description = "Description " ;//+ ctr;
        this.timestamp = date + " " + hourString;


    }


    public Note(String n, String d) {
        DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
        String hourString = dateFormat.format(new Date());
        SimpleDateFormat dF = new SimpleDateFormat("EEE, dd/MM/yyyy");
        String date = dF.format(new Date());
        this.name = n;
        this.description = d;
        this.timestamp = date + " " + hourString;


    }


    public Note(String n, String d, String t){

        this.name = n;
        this.description = d;
        this.timestamp =  t;


    }



    String getDescription() {

        return description;
    }

    String getName() {

        return name;
    }

    String getTime() {

        return timestamp;
    }





    @NonNull
    public String toString() {

        try {

            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("   ");
            jsonWriter.beginObject();
            jsonWriter.name("Name").value(getName());
            jsonWriter.name("Description").value(getDescription());
            jsonWriter.name("Timestamp").value(getTime());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();


        } catch (IOException e) {
            e.printStackTrace();

        }


        return "";

    }




}
