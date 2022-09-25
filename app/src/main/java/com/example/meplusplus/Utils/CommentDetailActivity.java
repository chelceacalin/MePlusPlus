package com.example.meplusplus.Utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.Adapters.Comment_Adapter;
import com.example.meplusplus.DataSets.Comm;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;

/*
       CREATED DATE: 8/27/2022
       UPDATED DATE: 8/27/2022
 */
public class CommentDetailActivity extends AppCompatActivity {
    //Controale
    CircleImageView activity_comment_detail_img;
    EditText activity_comment_detail_edit_Text_comment;
    Button activity_comment_detail_button_post;

    //Recycler
    RecyclerView activity_comment_detail_recyclerview;
    Comment_Adapter adapter;
    List<Comm> list;
    LinearLayoutManager manager;

    //Firebase
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference strikes;
    Map<String, Object> map;
    //Intent
    String postID;

    //Diverse
    String comment = "";
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        init();
        setProfileImage();

        activity_comment_detail_button_post.setOnClickListener(view -> {
            comment = activity_comment_detail_edit_Text_comment.getText().toString().trim();
            if (comment.equals("")) {
                new StyleableToast.Builder(CommentDetailActivity.this)
                        .text("Comment cannot be empty!")
                        .textColor(Color.RED)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_error_outline_24)
                        .show();
            } else {
                strikes.child(postID);
                id = strikes.child(postID).push().getKey();
                map.put("id", id);
                map.put("publisher", user.getUid());
                map.put("strike", comment);
                assert id != null;
                strikes.child(postID).child(id).setValue(map);
                activity_comment_detail_edit_Text_comment.setText("");
            }
        });
        readStrikes();
    }

    private void setProfileImage() {

        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!Objects.requireNonNull(snapshot.getValue(User.class)).getImageurl().equals("default")) {
                    Picasso.get().load(Objects.requireNonNull(snapshot.getValue(User.class)).getImageurl()).into(activity_comment_detail_img);
                } else
                    activity_comment_detail_img.setImageResource(R.drawable.ic_baseline_face_24);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void readStrikes() {

        strikes.child(postID).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot i : snapshot.getChildren()) {
                    list.add(i.getValue(Comm.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    //Initializare
    private void init() {
        postID = getIntent().getStringExtra("postid");

        //Controale
        activity_comment_detail_recyclerview = findViewById(R.id.activity_comment_detail_recyclerview);
        activity_comment_detail_edit_Text_comment = findViewById(R.id.activity_comment_detail_edit_Text_comment);
        activity_comment_detail_button_post = findViewById(R.id.activity_comment_detail_button_post);
        activity_comment_detail_img = findViewById(R.id.activity_comment_detail_img);

        //Recycler
        manager = new LinearLayoutManager(this);
        activity_comment_detail_recyclerview.setLayoutManager(manager);
        list = new ArrayList<>();
        adapter = new Comment_Adapter(this, postID, list);
        activity_comment_detail_recyclerview.setAdapter(adapter);

        //Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference().child("users");
        strikes = database.getReference().child("strikes");

        //Diverse
        map = new HashMap<>();
    }
}
