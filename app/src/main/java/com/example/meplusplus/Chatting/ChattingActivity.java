package com.example.meplusplus.Chatting;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.meplusplus.Adapters.User_Adapter;
import com.example.meplusplus.Adapters.User_Adapter_Chatting;
import com.example.meplusplus.DataSets.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*

Status: TC- add itemclicklistener

        CREATED DATE: 09/04/2022
        UPDATED DATE: 09/04/2022

 */

public class ChattingActivity extends AppCompatActivity {

    //RecyclerView
    RecyclerView recyclerView;
    List<User> list;
    User_Adapter_Chatting adapter;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        init();
        readusers();
    }

    private void init() {
        recyclerView=findViewById(R.id.activity_chatting_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChattingActivity.this));
        list=new ArrayList<>();
        adapter=new User_Adapter_Chatting(ChattingActivity.this,list);
        recyclerView.setAdapter(adapter);

        // Firebase ( for reading the users)
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference().child("users");
    }



    private void readusers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        list.add(i.getValue(User.class));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}