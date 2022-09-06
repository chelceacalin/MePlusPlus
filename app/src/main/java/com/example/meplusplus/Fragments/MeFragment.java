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

import com.example.meplusplus.CalorieCalculator.CalculateMetabolismActivity;
import com.example.meplusplus.Chatting.ChattingActivity;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.FoodTracking.CaloriesActivity;
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


    //CALORIE TRACKING
    Button fragment_me_add_food_item;


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

    //Calories stuff
    TextView fragment_me_calories;
    TextView fragment_me_protein;
    TextView fragment_me_carbs;
    TextView fragment_me_fats;
    TextView fragment_me_sugar;

    //Maximum values
    TextView fragment_me_max_calories;
    TextView fragment_me_max_protein;
    TextView fragment_me_max_carbs;
    TextView fragment_me_max_fats;
    TextView frament_me_max_sugar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        setCalorieCount();
        setMaxValuesCount();
        setDrawerUserDetails();

        fragment_me_open_drawer.setOnClickListener(view1 -> drawerLayout.openDrawer(GravityCompat.START));
        fragment_me_add_post.setOnClickListener(view12 -> {
            getContext().startActivity(new Intent(getContext(), PostActivity.class));
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
        });


        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        header_for_drawer_button_view_profile.setOnClickListener(view14 -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new AccountFragment()).commit();
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
        });

        header_for_drawer_text_username.setOnClickListener(view13 -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new AccountFragment()).commit();
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
        });


        drawer_circle_image_view_profile.setOnClickListener(view1 -> {
                    startActivity(new Intent(getContext(), EditProfile.class));
                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                }
        );

        fragment_me_chatActivity.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), ChattingActivity.class));
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
        });


        fragment_me_add_food_item.setOnClickListener(view1 -> {

            startActivity(new Intent(getContext(), CaloriesActivity.class));
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            getActivity().finish();
        });
        return view;
    }

    private void setMaxValuesCount() {

        String maxCalories = getActivity().getIntent().getStringExtra("maxCalories");
        fragment_me_max_calories.setText(maxCalories);

        String maxProtein = getActivity().getIntent().getStringExtra("maxProtein");
        fragment_me_max_protein.setText(maxProtein);

        String maxCarbs = getActivity().getIntent().getStringExtra("maxCarbs");
        fragment_me_max_carbs.setText(maxCarbs);

        String maxFats = getActivity().getIntent().getStringExtra("maxFats");
        fragment_me_max_fats.setText(maxFats);


        String maxSugar = getActivity().getIntent().getStringExtra("maxSugar");
        frament_me_max_sugar.setText(maxSugar);
    }


    private void initView(View view) {
        //Controale
        fragment_me_open_drawer = view.findViewById(R.id.fragment_me_open_drawer);
        drawerLayout = view.findViewById(R.id.drawerNavTest);
        fragment_me_chatActivity = view.findViewById(R.id.fragment_me_chatActivity);
        fragment_me_add_post = view.findViewById(R.id.fragment_me_add_post);


        //CALORIE TRACKING
        fragment_me_add_food_item = view.findViewById(R.id.fragment_me_add_food_item);
        fragment_me_calories = view.findViewById(R.id.fragment_me_calories);
        fragment_me_protein = view.findViewById(R.id.fragment_me_protein);
        fragment_me_carbs = view.findViewById(R.id.fragment_me_carbs);
        fragment_me_fats = view.findViewById(R.id.fragment_me_fats);
        fragment_me_sugar = view.findViewById(R.id.fragment_me_sugar);

        //max values
        fragment_me_max_calories = view.findViewById(R.id.fragment_me_max_calories);
        fragment_me_max_protein = view.findViewById(R.id.fragment_me_max_protein);
        fragment_me_max_carbs = view.findViewById(R.id.fragment_me_max_carbs);
        fragment_me_max_fats = view.findViewById(R.id.fragment_me_max_fats);
        frament_me_max_sugar = view.findViewById(R.id.frament_me_max_sugar);


        navigationView = view.findViewById(R.id.drawerNavigationView);
        //Sa putem modifica imaginea din header - S.O
        View header = navigationView.getHeaderView(0);
        //Drawrs items
        drawer_circle_image_view_profile = header.findViewById(R.id.drawer_circle_image_view_profile);
        header_for_drawer_text_username = header.findViewById(R.id.header_for_drawer_text_username);
        header_for_drawer_button_view_profile = header.findViewById(R.id.header_for_drawer_button_view_profile);
        //drawer_circle_image_view_profile.setBackgroundResource(R.drawable.ic_baseline_account_circle_24);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("users");
    }

    private void setCalorieCount() {
        String sumCalories = getActivity().getIntent().getStringExtra("sumCalories");
        fragment_me_calories.setText(sumCalories);

        String sumProtein = getActivity().getIntent().getStringExtra("sumProtein");
        fragment_me_protein.setText(sumProtein);

        String sumCarbs = getActivity().getIntent().getStringExtra("sumCarbs");
        fragment_me_carbs.setText(sumCarbs);

        String sumFats = getActivity().getIntent().getStringExtra("sumFats");
        fragment_me_fats.setText(sumFats);


        String sumSugar = getActivity().getIntent().getStringExtra("sumSugar");
        fragment_me_sugar.setText(sumSugar);

    }

    private void setDrawerUserDetails() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(User.class).getImageurl().equals("")) {

                } else {
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
            case R.id.drawer_calorie_calculator:
                startActivity(new Intent(getContext(), CalculateMetabolismActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                break;
            case R.id.drawer_drink:
                Toast.makeText(getContext(), "Drink Water", Toast.LENGTH_SHORT).show();
                break;
            case R.id.drawer_progress:
                Toast.makeText(getContext(), "Progress", Toast.LENGTH_SHORT).show();
                break;
            case R.id.todolist:
                Toast.makeText(getContext(), "To Do List", Toast.LENGTH_SHORT).show();
                break;

            case R.id.timerzenmode:
                Toast.makeText(getContext(), "Zen Mode", Toast.LENGTH_SHORT).show();
                break;

            case R.id.drawer_signout:
                Toast.makeText(getContext(), "Sign Out", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}