package com.example.meplusplus.ToDo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToDoActivity extends AppCompatActivity {

    //controale
    FloatingActionButton activity_to_do_fab;
    EditText actvity_to_do_edittext;
ImageButton activity_to_do_complete_all;
ImageButton activity_to_do_delete_all;


    //Listview
    ListView activity_to_do_listview;

    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> adapter;

    //Diverse
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        init();

        items = WriteToFileClass.read(ToDoActivity.this, "todolistitems.txt");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, items);
        activity_to_do_listview.setAdapter(adapter);

        activity_to_do_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                item = actvity_to_do_edittext.getText().toString();
                if (item.length() > 0) {
                    items.add(item);
                    actvity_to_do_edittext.setText("");
                    WriteToFileClass.write(ToDoActivity.this, items, "todolistitems.txt");
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ToDoActivity.this, "You must introduce a task! ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        activity_to_do_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder BUILDER = new AlertDialog.Builder(ToDoActivity.this);
                BUILDER.setCancelable(false);
                BUILDER.setTitle("DELETE");
                BUILDER.setMessage("Do you want to DELETE? ");


                BUILDER.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {

                        items.remove(i);
                        adapter.notifyDataSetChanged();
                        WriteToFileClass.write(ToDoActivity.this, items, "todolistitems.txt");

                    }
                });

                BUILDER.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = BUILDER.create();
                alertDialog.show();
                return true;
            }
        });


        activity_to_do_complete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0;i<activity_to_do_listview.getCount();i++){
                    activity_to_do_listview.setItemChecked(i,true);
                }
            }
        });

        activity_to_do_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.clear();
                adapter.notifyDataSetChanged();
                WriteToFileClass.write(ToDoActivity.this, items, "todolistitems.txt");
            }
        });

      /*  activity_to_do_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder BUILDER = new AlertDialog.Builder(ToDoActivity.this);
                BUILDER.setCancelable(false);
                BUILDER.setTitle("EDIT");
                BUILDER.setMessage("Do you want to EDIT? ");


                BUILDER.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                     *//*   items.remove(position);
                        adapter.notifyDataSetChanged();
                        FileHelper.writeData(items, MainActivity.this, "fisierText.txt");*//*
                      *//*  items.add(position,"ceva");
                        adapter.notifyDataSetChanged();*//*

                    }
                });

                BUILDER.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = BUILDER.create();
                alertDialog.show();

            }
        });*/
    }

    private void init() {
        activity_to_do_listview = findViewById(R.id.activity_to_do_listview);
        activity_to_do_fab = findViewById(R.id.activity_to_do_fab);
        actvity_to_do_edittext = findViewById(R.id.actvity_to_do_edittext);
        activity_to_do_complete_all=findViewById(R.id.activity_to_do_complete_all);
        activity_to_do_delete_all=findViewById(R.id.activity_to_do_delete_all);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listview_tasks,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.item_finished){
            StringBuilder sel= new StringBuilder("Selected Items");
            for(int i=0;i<activity_to_do_listview.getCount();i++){
                if(activity_to_do_listview.isItemChecked(i)){
                    sel.append(activity_to_do_listview.getItemAtPosition(i)).append("\n");
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }
}