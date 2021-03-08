package com.example.notesdraft;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView description;
    TextView dateTime;

    public NoteViewHolder(@NonNull View v) {
        super(v);
        name = v.findViewById(R.id.name);
        description = v.findViewById(R.id.description);
        dateTime = v.findViewById(R.id.dateTime);
    }






}
