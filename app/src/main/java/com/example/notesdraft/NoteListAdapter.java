package com.example.notesdraft;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder> {


    //Make notes about what an adapter does


    private ArrayList<Note> noteList;
    private final MainActivity mainAct;


    public NoteListAdapter(ArrayList<Note> noteList, MainActivity ma) {
        this.noteList = noteList;
        mainAct = ma;

    }

    //Inflates and creates cell
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NoteViewHolder(itemView);
    }


    //Binds variable values to cell

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note n = noteList.get(position);
        holder.name.setText(n.getName());
        //holder.description.setText(n.getDescription());
        holder.dateTime.setText(n.getTime());

        String desc = n.getDescription();
        if(desc.length() > 80){
            String shortenedDesc = desc.substring(0,80) + "...";
            holder.description.setText(shortenedDesc);
        }
        else {
            holder.description.setText(n.getDescription());
        }

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
