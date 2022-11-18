package com.example.meplusplus.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meplusplus.Adapters.AccountPhotos;
import com.example.meplusplus.DataSets.PostItem;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.R;
import com.example.meplusplus.Registration.LoginActivity;
import com.example.meplusplus.Utils.EditProfile;
import com.example.meplusplus.Utils.FeedBackActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;


/*

       Status: RFP
       CREATED DATE: 8/29/2022
       UPDATED DATE: 8/29/2022
 */
@SuppressWarnings("ALL")
public class AccountFragment extends Fragment {

    //Controale
    TextView fragment_account_username, fragment_account_number_of_posts, fragment_account_description;
    Button fragment_account_edit_button;
    ImageView fragment_account_options, fragment_account_image_profile;

    //Firebase
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference referencePosts;

    //Recycler
    RecyclerView fragment_account_recyclerView_posts;
    AccountPhotos adapter;
    GridLayoutManager manager;
    List<PostItem> items;

    //Diverse
    String pID;
    int contor;
    boolean wantadsOn=false ;

    //Redirect
    String received;
    //Pop up menu
    PopupMenu popup;
    Menu menu;
    MenuInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        setDetails();

        //No Of Posts
        referencePosts.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    if (Objects.requireNonNull(i.getValue(PostItem.class)).getPublisher().equals(pID)) {
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

    @SuppressLint("SetTextI18n")
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

        received = requireContext().getSharedPreferences("PID", Context.MODE_PRIVATE).getString("profileId", "empty");
        if (!received.equals("empty")) {
            //DACA PRIMESC CEVA
            pID = received;
            requireContext().getSharedPreferences("PID", Context.MODE_PRIVATE).edit().clear().apply();
            fragment_account_edit_button.setVisibility(View.GONE);

            //De completat cand fac partea de chat app

        } else {
            pID = user.getUid();
            fragment_account_edit_button.setText("Edit");
            fragment_account_edit_button.setOnClickListener(view1 -> startActivity(new Intent(getContext(), EditProfile.class)));
        }

        //Recyclerview
        manager = new GridLayoutManager(getContext(), 3);
        // manager.setSpanCount(2);
        items = new ArrayList<>();
        adapter = new AccountPhotos(getContext(), items);
        fragment_account_recyclerView_posts.setLayoutManager(manager);
        fragment_account_recyclerView_posts.setAdapter(adapter);

        fragment_account_recyclerView_posts.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                //spacing between views in grid
                outRect.left = 0;
                outRect.right = 5;
                outRect.top = 10;
                outRect.bottom = 5;
            }
        });

        //Diverse
        contor = 0;
    }


    private void setDetails() {
        reference.child(pID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fragment_account_username.setText(Objects.requireNonNull(snapshot.getValue(User.class)).getUsername());
                fragment_account_description.setText(Objects.requireNonNull(snapshot.getValue(User.class)).getBio());
                if (Objects.requireNonNull(snapshot.getValue(User.class)).getImageurl().equals("default")) {
                    fragment_account_image_profile.setBackgroundResource(R.drawable.ic_baseline_account_circle_24);
                    Glide.with(requireContext()).load(user.getPhotoUrl()).into(fragment_account_image_profile);
                } else {
                    Picasso.get().load(Objects.requireNonNull(snapshot.getValue(User.class)).getImageurl()).into(fragment_account_image_profile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        SharedPreferences S = getContext().getSharedPreferences("pleaseADS", Context.MODE_PRIVATE);
        final SharedPreferences.Editor E = S.edit();
          wantadsOn = S.getBoolean("isWantadsOn", false);

//        if (wantadsOn) {
//            Toast.makeText(getContext(), "da", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getContext(), "nu", Toast.LENGTH_SHORT).show();
//        }


        // Pop up menu
        fragment_account_options.setOnClickListener(view -> {
            popup = new PopupMenu(getContext(), fragment_account_options);
            menu = popup.getMenu();
            //CAM TARANIE, AR TRB MODIFICAT
            popup.getMenuInflater().inflate(R.menu.menu_options_profile, menu);


            popup.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getTitle().equals("Sign Out")) {
                    new StyleableToast.Builder(getContext())
                            .text("You have been signed out")
                            .textColor(Color.BLUE)
                            .backgroundColor(getResources().getColor(R.color.white))
                            .textSize(19)
                            .cornerRadius(25)
                            .show();
                    auth.signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);


                } else if (menuItem.getTitle().equals("Dark Mode")) {

                    if (isDarkModeOn) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        editor.putBoolean("isDarkModeOn", false);
                        editor.apply();
                        new StyleableToast.Builder(getContext())
                                .text("Dark Mode Turned OFF ")
                                .textColor(Color.BLACK)
                                .backgroundColor(getResources().getColor(R.color.WhiteSmoke))
                                .cornerRadius(25)
                                .iconStart(R.drawable.ic_baseline_dark_mode_24)
                                .show();

                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor.putBoolean("isDarkModeOn", true);
                        editor.apply();

                        new StyleableToast.Builder(getContext())
                                .text("Dark Mode Turned ON")
                                .textColor(Color.WHITE)
                                .backgroundColor(getResources().getColor(R.color.Black))
                                .cornerRadius(25)
                                .iconStart(R.drawable.ic_baseline_dark_mode_24)
                                .show();
                    }


                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (menuItem.getTitle().equals("Help / Give FeedbacK")) {
                    Intent i = new Intent(getActivity(), FeedBackActivity.class);
                    startActivity(i);
                    ((Activity) getActivity()).overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                }
                else if(menuItem.getTitle().equals("Support the developer")){

                    if (wantadsOn==true) {
                        wantadsOn=!wantadsOn;
                        E.putBoolean("isWantadsOn", wantadsOn);
                        E.apply();
                       // Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), "Stopped Supporting the Creator", Toast.LENGTH_SHORT).show();
                    } else {
                        wantadsOn=!wantadsOn;
                        E.putBoolean("isWantadsOn", wantadsOn);
                        E.apply();
                        Toast.makeText(getContext(), "Started Supporting the Creator", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            });
            popup.show();
        });

    }

    private void getposts() {
        referencePosts.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot i : snapshot.getChildren()) {
                    if (Objects.requireNonNull(i.getValue(PostItem.class)).getPublisher().equals(pID)) {
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
        inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_options_profile, menu);
        return true;
    }

    @Override
    public void onStart() {
        if (fragment_account_description.getText().toString().toString().equals("")) {
            fragment_account_description.setVisibility(View.GONE);
        } else {
            fragment_account_description.setVisibility(View.VISIBLE);
        }
        super.onStart();

    }
}