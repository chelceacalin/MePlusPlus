package com.example.meplusplus.Chatting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.Adapters.User_Adapter_Chatting;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.example.meplusplus.context.DbContext;
import com.example.meplusplus.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChattingActivity extends AppCompatActivity {

    //RecyclerView
    RecyclerView recyclerView;
    List<User> list;
    User_Adapter_Chatting adapter;

    DatabaseReference databaseReference;
    ImageView activity_chatting_close_button;

    FirebaseAuth auth;
    FirebaseUser user;

    DbContext dbContext = DbContext.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        init();

        activity_chatting_close_button.setOnClickListener(view -> {
            startActivity(new Intent(ChattingActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
            finish();
            super.onBackPressed();
        });

        readusers();
    }

    private void init() {
        //Controale
        activity_chatting_close_button = findViewById(R.id.activity_chatting_close_button);
        recyclerView = findViewById(R.id.activity_chatting_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChattingActivity.this));
        list = new ArrayList<>();
        adapter = new User_Adapter_Chatting(ChattingActivity.this, list);
        recyclerView.setAdapter(adapter);

        // Firebase ( for reading the users)
        databaseReference = dbContext.getReferenceChild("users");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }


    private void readusers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    if (i.getValue(User.class).getId().equals(user.getUid())) {
                        list.add(0, i.getValue(User.class));
                        adapter.notifyItemInserted(0);
                    } else
                        list.add(i.getValue(User.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChattingActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
        finish();
        super.onBackPressed();
    }

}