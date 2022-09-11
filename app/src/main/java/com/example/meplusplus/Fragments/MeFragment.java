package com.example.meplusplus.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.meplusplus.DataSets.Food;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.FoodTracking.CaloriesActivity;
import com.example.meplusplus.R;
import com.example.meplusplus.ToDo.ToDoActivity;
import com.example.meplusplus.Utils.EditProfile;
import com.example.meplusplus.Utils.PostActivity;
import com.example.meplusplus.WaterReminder.DrinkWater;
import com.example.meplusplus.ZenMode.ZenModeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import de.hdodenhof.circleimageview.CircleImageView;

/*

 Status: TC- we need to add the activities

 CREATED DATE: 09/03/2022
 UPDATED DATE: 09/03/2022


 */
@SuppressLint("SetTextI18n")
public class MeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    //Controale
    ImageView fragment_me_open_drawer;
    ImageView fragment_me_chatActivity;
    ImageView fragment_me_add_post;


    //CALORIE TRACKING
    Button fragment_me_add_food_item;
    Button fragment_me_reset_macros;

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
    DatabaseReference foodsReference;
    FirebaseAuth auth;
    FirebaseUser user;

    //Calories stuff
    TextView fragment_me_calories;
    TextView fragment_me_protein;
    TextView fragment_me_carbs;
    TextView fragment_me_fats;
    TextView fragment_me_sugar;
    TextView fragment_me_water;
    //Maximum values
    TextView fragment_me_max_calories;
    TextView fragment_me_max_protein;
    TextView fragment_me_max_carbs;
    TextView fragment_me_max_fats;
    TextView frament_me_max_sugar;
    TextView fragment_me_max_water;


    String usserID;
    Float sumCalories = 0f;
    Float sumProtein = 0f;
    Float sumCarbs = 0f;
    Float sumFat = 0f;
    Float sumSugar = 0f;



    // DRINK WATER
    CircularProgressIndicator circularProgress;
    Button activity_drink_water_add_200ml;
    int waterDrank=0;
    int wdr=0;
    int current = 0;


    //sumtotal
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        setMaxValuesCount();
        setDrawerUserDetails();
        getcaloriesForTheDay();
        setWaterMaxValue();
        setMaxProgress();


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
        });
        fragment_me_chatActivity.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), ChattingActivity.class));
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
        });
        fragment_me_add_food_item.setOnClickListener(view1 -> {

            startActivity(new Intent(getContext(), CaloriesActivity.class));
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            getActivity().finish();
        });
        fragment_me_reset_macros.setOnClickListener(view1 -> resetMacros());


        activity_drink_water_add_200ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current += 200;
                circularProgress.setCurrentProgress(current);

                if(current>waterDrank){
                    circularProgress.setProgressColor(Color.GREEN);
                    circularProgress.setDotColor(Color.GRAY);
                }
            }
        });

        return view;
    }


    private void initView(@NonNull View view) {
        //Controale
        fragment_me_open_drawer = view.findViewById(R.id.fragment_me_open_drawer);
        drawerLayout = view.findViewById(R.id.drawerNavTest);
        fragment_me_chatActivity = view.findViewById(R.id.fragment_me_chatActivity);
        fragment_me_add_post = view.findViewById(R.id.fragment_me_add_post);
        fragment_me_reset_macros = view.findViewById(R.id.fragment_me_reset_macros);

        //CALORIE TRACKING
        fragment_me_add_food_item = view.findViewById(R.id.fragment_me_add_food_item);
        fragment_me_calories = view.findViewById(R.id.fragment_me_calories);
        fragment_me_protein = view.findViewById(R.id.fragment_me_protein);
        fragment_me_carbs = view.findViewById(R.id.fragment_me_carbs);
        fragment_me_fats = view.findViewById(R.id.fragment_me_fats);
        fragment_me_sugar = view.findViewById(R.id.fragment_me_sugar);
        fragment_me_water=view.findViewById(R.id.fragment_me_water);

        //max values
        fragment_me_max_calories = view.findViewById(R.id.fragment_me_max_calories);
        fragment_me_max_protein = view.findViewById(R.id.fragment_me_max_protein);
        fragment_me_max_carbs = view.findViewById(R.id.fragment_me_max_carbs);
        fragment_me_max_fats = view.findViewById(R.id.fragment_me_max_fats);
        frament_me_max_sugar = view.findViewById(R.id.frament_me_max_sugar);
        fragment_me_max_water=view.findViewById(R.id.fragment_me_max_water);

        //Circular Progress Bar
        circularProgress = view.findViewById(R.id.circular_progress);
        circularProgress.setCurrentProgress(0);
        circularProgress.setTextColor(Color.WHITE);
        activity_drink_water_add_200ml = view.findViewById(R.id.activity_drink_water_add_200ml);

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
        foodsReference = database.getReference("foods");
    }

    private void getcaloriesForTheDay() {
        //  itemId=getActivity().getIntent().getStringExtra("itemID");
        //Daca avem ceva
        usserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        foodsReference.child(usserID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    Food food = (item.getValue(Food.class));
                    // Toast.makeText(getContext(), food.getItemID()+"", Toast.LENGTH_SHORT).show();
                    sumCalories += food.getSumCalories();
                    sumProtein += food.getSumProtein();
                    sumCarbs += food.getSumCarbs();
                    sumFat += food.getSumFats();
                    sumSugar += food.getSumSugar();
                }
                if (sumCalories != null)
                    fragment_me_calories.setText(Math.round(sumCalories) + "");
                else
                    fragment_me_calories.setText("0");


                if(sumProtein!=null)
                    fragment_me_protein.setText(Math.round(sumProtein)+"");
                else
                fragment_me_protein.setText("0");


                if(sumCarbs!=null)
                    fragment_me_carbs.setText(Math.round(sumCarbs)+"");
                else
                    fragment_me_carbs.setText("0");


                if(sumFat!=null)
                    fragment_me_fats.setText(Math.round(sumFat)+"");
                else
                    fragment_me_fats.setText("0");

                if(sumSugar!=null)
                    fragment_me_sugar.setText(Math.round(sumSugar)+"");
                else
                    fragment_me_sugar.setText("0");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void resetMacros() {
        usserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        foodsReference.child(usserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sumCalories = 0f;
                sumProtein=0f;
                sumCarbs=0f;
                sumFat=0f;
                sumSugar=0f;
                Toast.makeText(getContext(), "Calories have been reset", Toast.LENGTH_SHORT).show();
                fragment_me_calories.setText(sumCalories + "");
                fragment_me_protein.setText(sumProtein+"");
                fragment_me_carbs.setText(sumCarbs+"");
                fragment_me_fats.setText(sumFat+"");
                fragment_me_sugar.setText(sumSugar+"");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Err", Toast.LENGTH_SHORT).show();
            }
        });


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
                startActivity(new Intent(getContext(), DrinkWater.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                break;
            case R.id.drawer_progress:
                Toast.makeText(getContext(), "Progress", Toast.LENGTH_SHORT).show();
                break;
            case R.id.todolist:
                startActivity(new Intent(getContext(), ToDoActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                break;

            case R.id.timerzenmode:
                startActivity(new Intent(getContext(), ZenModeActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                break;
            case R.id.drawer_signout:
                Toast.makeText(getContext(), "Sign Out", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    private void setMaxValuesCount() {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = prefs.getString("sendItemsList", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> items = gson.fromJson(json, type);

        if(items!=null){
            fragment_me_max_calories.setText(items.get(0));
            fragment_me_max_protein.setText(items.get(1));
            fragment_me_max_carbs.setText(items.get(2));
            fragment_me_max_fats.setText(items.get(3));
            frament_me_max_sugar.setText(items.get(4));
        }
        else
        {
            fragment_me_max_calories.setText(0+"");
            fragment_me_max_protein.setText(0+"");
            fragment_me_max_carbs.setText(0+"");
            fragment_me_max_fats.setText(0+"");
            frament_me_max_sugar.setText(0+"");
        }



    }

    private  void setWaterMaxValue(){

        SharedPreferences sharedpref1 = getContext().getSharedPreferences("msp", Context.MODE_PRIVATE);
        waterDrank = sharedpref1.getInt("waterDrank", 0);
        if(waterDrank>0){

            fragment_me_max_water.setText(waterDrank+"");
        }
        else
        {
            fragment_me_max_water.setText(0+"");
        }

    }

    private void setMaxProgress(){
        SharedPreferences sharedpref1 =getContext().getSharedPreferences("msp", Context.MODE_PRIVATE);
        wdr = sharedpref1.getInt("waterDrank", 0);
        circularProgress.setMaxProgress(Math.max(wdr, 0));
    }
}