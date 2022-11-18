package com.example.meplusplus.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.Adapters.User_Adapter;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/*
       Status: RFP
       CREATED DATE: 8/24/2022
       UPDATED DATE: 8/24/2022

       1.
       UPDATED DATE: 8/30/2022
         Notes: When you click on someone it redirects you to their page
 */
public class SearchPeopleFragment extends Fragment {

    //Controale
    EditText searchPeopleEditText;

    //RecyclerView
    RecyclerView fragment_search_recyclerView;
    List<User> users;
    User_Adapter user_adapter;
    Query q;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    FirebaseAuth auth;
    FirebaseUser user;

    //Diverse
    String stringText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_people, container, false);
        init();
        initRecycler(view);
        readusers();

        searchPeopleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString().toLowerCase().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }


    private void initRecycler(View view) {
        fragment_search_recyclerView = view.findViewById(R.id.fragment_search_recyclerView);
        searchPeopleEditText = view.findViewById(R.id.fragment_search_search_edit_text);
        fragment_search_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragment_search_recyclerView.setAdapter(user_adapter);
    }

    private void init() {
        //Initiailizare
        users = new ArrayList<>();
        user_adapter = new User_Adapter(getContext(), users, true);

        // Firebase ( for reading the users)
        database = FirebaseDatabase.getInstance("https://applicenta-8582b-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference().child("users");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }


    private void readusers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stringText = searchPeopleEditText.getText().toString();
                if (stringText.equals("")) {
                    users.clear();
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        if (!i.getValue(User.class).getId().equals(user.getUid()))
                            users.add(i.getValue(User.class));
                    }
                    user_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void searchUser(String usr) {
        q = databaseReference.orderByChild("username").startAt(usr).endAt(usr + "\uf8ff");
        q.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot i : snapshot.getChildren()) {
                    users.add(i.getValue(User.class));
                }
                user_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}