package com.example.meplusplus.Chatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meplusplus.R;

import de.hdodenhof.circleimageview.CircleImageView;

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

    // Messages
    String meUser,sender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();

        activity_message_close.setOnClickListener(view -> {
          startActivity(new Intent(MessageActivity.this,ChattingActivity.class));
            overridePendingTransition(R.anim.slide_left_to_right_transition,R.anim.slide_right_to_left_transition);
            finish();
        });
    }
    private void init(){
        //Controale
        activity_message_close=findViewById(R.id.activity_message_close);
        activity_message_profile_pic=findViewById(R.id.activity_message_profile_pic);
        activity_message_username=findViewById(R.id.activity_message_username);
        activity_message_edit_text=findViewById(R.id.activity_message_edit_text);
        activity_message_button=findViewById(R.id.activity_message_button);

        //Messaje
      /*  meUser=getIntent().getStringExtra("meUser");
        sender=getIntent().getStringExtra("sender");

       activity_message_username.setText(sender);*/


    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_right_to_left_transition,R.anim.slide_left_to_right_transition);
        super.onBackPressed();
    }
}