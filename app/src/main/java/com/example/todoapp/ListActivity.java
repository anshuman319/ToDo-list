package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ListActivity extends AppCompatActivity {

    Button addInList,reset;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("list");
        addInList=findViewById(R.id.ListActivity_button_addInList);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reset=findViewById(R.id.ListActivity_button_resetList);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
            }
        });
        loadData();
        addInList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialoge();
            }
        });
    }

    private void resetData() {

        final Dialog d = new Dialog(ListActivity.this);
        d.setCancelable(true);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.resetdialog);

        Button yes,no;
        yes=d.findViewById(R.id.resetdialog_yes);
        no=d.findViewById(R.id.resetdialog_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.removeValue();
                d.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    private void loadData() {
        FirebaseRecyclerOptions<List> options = new FirebaseRecyclerOptions.Builder<List>()
                .setQuery(databaseReference,List.class).build();
        FirebaseRecyclerAdapter<List,ListViewHolder> adapter = new
                FirebaseRecyclerAdapter<List, ListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ListViewHolder holder, int position, @NonNull List model) {

                        holder.lvTitle.setText(model.getTitle());
                        holder.lvDescription.setText(model.getDescription());
                        holder.lvStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog statusDialog=new Dialog(ListActivity.this);
                                statusDialog.setContentView(R.layout.statusdialog);

                                Button yes,no;
                                yes=statusDialog.findViewById(R.id.statusdialog_yes);
                                no=statusDialog.findViewById(R.id.statusdialog_no);

                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        holder.lvStatus.setText("COMPLETE");
                                        statusDialog.dismiss();
                                    }
                                });
                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        statusDialog.dismiss();
                                    }
                                });

                                statusDialog.show();

                            }


                        });

                    }

                    @NonNull
                    @Override
                    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list,parent,false);
                        return new ListViewHolder(view);
                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }



    void showCustomDialoge() {
        final Dialog dialog =new Dialog(ListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.customdialog);
        final EditText Dtitle=dialog.findViewById(R.id.edittext_custom_title);
        final EditText des=dialog.findViewById(R.id.edittext_custom_description);
        Button addList=dialog.findViewById(R.id.button_custom_add);
        Button cancle=dialog.findViewById(R.id.cutom_button_cancel);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title=Dtitle.getText().toString();
                String description=des.getText().toString();
                HashMap<String,Object> map = new HashMap<>();
                map.put("title",title);
                map.put("description",description);
                databaseReference.push().setValue(map)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ListActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ListActivity.this, "Added Succesfully", Toast.LENGTH_SHORT).show();
                            }
                        });

                dialog.dismiss();

            }
        });
        dialog.show();
    }
}