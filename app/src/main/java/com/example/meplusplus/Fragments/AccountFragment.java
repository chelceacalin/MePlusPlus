package com.example.meplusplus.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meplusplus.Adapters.AccountPhotos;
import com.example.meplusplus.DataSets.PostItem;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.LoginActivity;
import com.example.meplusplus.R;
import com.example.meplusplus.Utils.EditProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// RFP

/*
       CREATED DATE: 8/29/2022
       UPDATED DATE: 8/29/2022
 */
public class AccountFragment extends Fragment {

    //Firebase
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference referencePosts;

    //Controale
    TextView fragment_account_username, fragment_account_number_of_posts, fragment_account_description;
    Button fragment_account_edit_button;
    ImageView fragment_account_options, fragment_account_image_profile;

    //Recycler
    RecyclerView fragment_account_recyclerView_posts;
    AccountPhotos adapter;
    GridLayoutManager manager;
    List<PostItem> items;
    String pID;
    int contor;
    String received;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        setDetails();


        //No Of Posts
        referencePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot i : snapshot.getChildren()) {
                    if (i.getValue(PostItem.class).getPublisher().equals(pID)) {
                        contor++;
                    }
                    fragment_account_number_of_posts.setText(contor + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        getposts();
        return view;
    }

    private void init(View view) {
        //Controale
        fragment_account_username = view.findViewById(R.id.fragment_account_username);
        fragment_account_number_of_posts = view.findViewById(R.id.fragment_account_number_of_posts);
        fragment_account_description = view.findViewById(R.id.fragment_account_description);
        fragment_account_edit_button = view.findViewById(R.id.fragment_account_edit_button);
        fragment_account_options = view.findViewById(R.id.fragment_account_options);
        fragment_account_image_profile = view.findViewById(R.id.fragment_account_image_profile);
        fragment_account_recyclerView_posts = view.findViewById(R.id.fragment_account_recyclerView_posts);

        //Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("users");
        referencePosts = database.getReference("posts");
        pID = user.getUid();

        received= getContext().getSharedPreferences("PID", Context.MODE_PRIVATE).getString("profileId", "empty");

        if (!received.equals("empty")) {
            //DACA PRIMESC CEVA
            pID = received;
            getContext().getSharedPreferences("PID", Context.MODE_PRIVATE).edit().clear().apply();
            fragment_account_edit_button.setText("Say Hi");

            //De completat cand fac partea de chat app

        } else {
            pID = user.getUid();
         //   fragment_account_edit_button.setText("Edit");
            fragment_account_edit_button.setText("Edit");
            fragment_account_edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), EditProfile.class));
                }
            });


        }


        manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanCount(2);
        items = new ArrayList<>();
        adapter = new AccountPhotos(getContext(), items);
        fragment_account_recyclerView_posts.setLayoutManager(manager);
        fragment_account_recyclerView_posts.setAdapter(adapter);

        fragment_account_recyclerView_posts.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int spanCount = 2;
                int spacing = 10;//spacing between views in grid

                int column = position % spanCount; // item column
                outRect.left = 0; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = 0; // (column + 1) * ((1f / spanCount) * spacing)
                outRect.top = 30;
                outRect.bottom = 30; // item bottom
            }
        });

        //Diverse
        contor = 0;
    }


    private void setDetails() {
        reference.child(pID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fragment_account_username.setText(snapshot.getValue(User.class).getUsername());
                fragment_account_description.setText(snapshot.getValue(User.class).getBio());
                if (snapshot.getValue(User.class).getImageurl().equals("default")) {
                    //fragment_account_image_profile.setImageResource(R.drawable.ic_baseline_person_pin_24);
                    Glide.with(getContext()).load(user.getPhotoUrl()).into(fragment_account_image_profile);

                } else{
                   Picasso.get().load(snapshot.getValue(User.class).getImageurl()).into(fragment_account_image_profile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Pop up menu
        fragment_account_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getContext(),
                        fragment_account_options);
                Menu menu = popup.getMenu();

                popup.getMenuInflater().inflate(R.menu.menu_options_profile, menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        if(menuItem.getTitle().equals("Sign Out")){
                            auth.signOut();
                            Intent intent=new Intent(getContext(),LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(getContext(), "You've been signed out", Toast.LENGTH_SHORT).show();

                        }
                        else  if(menuItem.getTitle().equals("Dark Mode")){
                            Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

    }

    private void getposts() {
        referencePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot i : snapshot.getChildren()) {
                    if (i.getValue(PostItem.class).getPublisher().equals(pID)) {
                        items.add(i.getValue(PostItem.class));
                    }
                }
                Collections.reverse(items);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
            MenuInflater inflater= getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_options_profile,menu);
     return true;
    }
}