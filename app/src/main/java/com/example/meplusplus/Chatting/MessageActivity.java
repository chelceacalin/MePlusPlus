package com.example.meplusplus.Chatting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meplusplus.Adapters.Message_Adapter;
import com.example.meplusplus.DataSets.Message;
import com.example.meplusplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;

/*

        Status: TC- add itemclicklistener

        CREATED DATE: 09/04/2022
        UPDATED DATE: 09/04/2022

        */
public class MessageActivity extends AppCompatActivity {

    //Controale
    ImageView activity_message_close;
    CircleImageView activity_message_profile_pic;
    TextView activity_message_username;
    EditText activity_message_edit_text;
    Button activity_message_button;

    //RecyclerView
    RecyclerView activity_message_recyclerView;
    LinearLayoutManager manager;
    List<Message> list;
    Message_Adapter adapter;
    Map<String, Object> map;
    FirebaseAuth auth;
    FirebaseUser user;
    // Messages
    String sender;
    String sendToTargetID;
    String imageurl;
    String message;
    String messageID;
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();
        setdetails();
        readMessages();


        activity_message_close.setOnClickListener(view -> {
            startActivity(new Intent(MessageActivity.this, ChattingActivity.class));
            overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
            finish();
        });

        activity_message_button.setOnClickListener(view -> {
            message = activity_message_edit_text.getText().toString().trim();
            if (!message.equals("")) {
                send(message);
            } else {
                new StyleableToast.Builder(MessageActivity.this)
                        .text("No message to send")
                        .textColor(Color.RED)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_error_outline_24)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MessageActivity.this, ChattingActivity.class));
        overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
        finish();
        super.onBackPressed();
    }

    private void init() {
        //Controale
        activity_message_close = findViewById(R.id.activity_message_close);
        activity_message_profile_pic = findViewById(R.id.activity_message_profile_pic);
        activity_message_username = findViewById(R.id.activity_message_username);
        activity_message_edit_text = findViewById(R.id.activity_message_edit_text);
        activity_message_button = findViewById(R.id.activity_message_button);

        //Diverse
        map = new HashMap<>();


        //Firebase
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference().child("messages");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        activity_message_recyclerView = findViewById(R.id.activity_message_recyclerView);
        manager = new LinearLayoutManager(this);
        activity_message_recyclerView.setLayoutManager(manager);

        list = new ArrayList<>();
        adapter = new Message_Adapter(MessageActivity.this, user.getUid(), list);
        //activity_message_recyclerView.setAdapter(adapter);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void readMessages() {
        reference.child(user.getUid()).child(sendToTargetID).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.add(snapshot.getValue(Message.class));
                adapter.notifyDataSetChanged();
                activity_message_recyclerView.scrollToPosition(list.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        adapter.notifyDataSetChanged();
        activity_message_recyclerView.setAdapter(adapter);
    }

    private void setdetails() {
        sender = getIntent().getStringExtra("userTarget");
        imageurl = getIntent().getStringExtra("userImgUrl");
        sendToTargetID = getIntent().getStringExtra("userTargetID");
        if (!imageurl.equals("")) {
            Glide.with(MessageActivity.this).load(imageurl).into(activity_message_profile_pic);
        } else {
            activity_message_profile_pic.setImageResource(R.drawable.ic_baseline_person_pin_24);
        }

        activity_message_username.setText(sender);
    }


    private void send(String message) {
        messageID = reference.child(user.getUid()).child(sendToTargetID).push().getKey();
        map.put("message", message);
        map.put("whosentit", user.getUid());
        reference.child(user.getUid()).child(sendToTargetID).child(messageID).setValue(map);
        reference.child(sendToTargetID).child(user.getUid()).child(messageID).setValue(map);
        activity_message_edit_text.setText("");
    }


}