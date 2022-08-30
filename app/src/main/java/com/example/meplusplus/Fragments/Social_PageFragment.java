package com.example.meplusplus.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.Adapters.PostAdapter;
import com.example.meplusplus.DataSets.PostItem;
import com.example.meplusplus.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/*
       CREATED DATE: 8/27/2022
       UPDATED DATE: 8/27/2022
 */
public class Social_PageFragment extends Fragment {


    //RecyclerView
    RecyclerView social_page_recyclerviewPosts;
    List<PostItem> items;
    PostAdapter adapter;
    LinearLayoutManager manager;


    PostItem pItem;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social__page, container, false);
        social_page_recyclerviewPosts = view.findViewById(R.id.social_page_recyclerviewPosts);
        init();
        readPosts();
        return view;
    }

    private void init() {
        //Controale
        manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        social_page_recyclerviewPosts.setLayoutManager(manager);

        //Initializari
        items = new ArrayList<>();
        adapter = new PostAdapter(items, getContext());
        social_page_recyclerviewPosts.setAdapter(adapter);

        //Firebase
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference().child("posts");
    }

    private void readPosts() {
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot i : snapshot.getChildren()) {
                    items.add(i.getValue(PostItem.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
