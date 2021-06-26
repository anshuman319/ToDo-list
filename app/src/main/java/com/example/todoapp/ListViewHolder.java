package com.example.todoapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewHolder extends RecyclerView.ViewHolder {

    TextView lvTitle,lvDescription,lvStatus;

    public ListViewHolder(@NonNull View itemView) {
        super(itemView);

        lvTitle=itemView.findViewById(R.id.list_title);
        lvDescription=itemView.findViewById(R.id.list_description);
        lvStatus=itemView.findViewById(R.id.list_status);
    }
}
