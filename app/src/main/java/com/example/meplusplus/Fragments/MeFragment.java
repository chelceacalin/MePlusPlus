package com.example.meplusplus.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.meplusplus.Chatting.ChattingActivity;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.R;
import com.example.meplusplus.Utils.EditProfile;
import com.example.meplusplus.Utils.PostActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/*

 Status: TC- we need to add the activities

 CREATED DATE: 09/03/2022
 UPDATED DATE: 09/03/2022


 */
public class MeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    //Controale
    ImageView fragment_me_open_drawer;
    ImageView fragment_me_chatActivity;
    ImageView fragment_me_add_post;
    //Drawer
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    //Drawer Items
    CircleImageView drawer_circle_image_view_profile;
    TextView header_for_drawer_text_username;
    Button header_for_drawer_button_view_profile;

    //Firebase -- to set drawer picture and username
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        setDrawerUserDetails();

        fragment_me_open_drawer.setOnClickListener(view1 -> drawerLayout.openDrawer(GravityCompat.START));
        fragment_me_add_post.setOnClickListener(view12 -> {
            getContext().startActivity(new Intent(getContext(), PostActivity.class));
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
        });


        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        header_for_drawer_button_view_profile.setOnClickListener(view14 -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,new AccountFragment()).commit();
            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.slide_out);
        });

        header_for_drawer_text_username.setOnClickListener(view13 -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,new AccountFragment()).commit();
            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.slide_out);
        });


        drawer_circle_image_view_profile.setOnClickListener(view1 ->{
                    startActivity(new Intent(getContext(), EditProfile.class));
            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.slide_out);
                }
        );

        fragment_me_chatActivity.setOnClickListener(view1 ->startActivity(new Intent(getContext(), ChattingActivity.class)) );

        return view;
    }




    private void initView(View view) {
        //Controale
        fragment_me_open_drawer = view.findViewById(R.id.fragment_me_open_drawer);
        drawerLayout = view.findViewById(R.id.drawerNavTest);
        fragment_me_chatActivity = view.findViewById(R.id.fragment_me_chatActivity);
        fragment_me_add_post = view.findViewById(R.id.fragment_me_add_post);


        navigationView = view.findViewById(R.id.drawerNavigationView);

        //Sa putem modifica imaginea din header - S.O
        View header=navigationView.getHeaderView(0);
        //Drawrs items
        drawer_circle_image_view_profile = header.findViewById(R.id.drawer_circle_image_view_profile);
        header_for_drawer_text_username=header.findViewById(R.id.header_for_drawer_text_username);
        header_for_drawer_button_view_profile=header.findViewById(R.id.header_for_drawer_button_view_profile);
      //drawer_circle_image_view_profile.setBackgroundResource(R.drawable.ic_baseline_account_circle_24);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("users");
    }


    private void setDrawerUserDetails() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(User.class).getImageurl().equals(""))
                {

                }
                else{
                    Picasso.get().load(snapshot.getValue(User.class).getImageurl()).into(drawer_circle_image_view_profile);
                 //   Glide.with(getContext()).load(snapshot.getValue(User.class).getImageurl()).into(drawer_circle_image_view_profile);
                    header_for_drawer_text_username.setText(Objects.requireNonNull(snapshot.getValue(User.class)).getUsername());
                }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.drawer_workout:
                Toast.makeText(getContext(), "Workout ", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.drawer_calorie_calculator:
                Toast.makeText(getContext(), "Calorie Calculator", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.drawer_drink:
                Toast.makeText(getContext(), "Drink Water", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.drawer_progress:
                Toast.makeText(getContext(), "Progress", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.todolist:
                Toast.makeText(getContext(), "To Do List", Toast.LENGTH_SHORT).show();
                break;

            case  R.id.timerzenmode:
                Toast.makeText(getContext(), "Zen Mode", Toast.LENGTH_SHORT).show();
                break;

            case  R.id.drawer_signout:
                Toast.makeText(getContext(), "Sign Out", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}