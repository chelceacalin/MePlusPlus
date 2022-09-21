package com.example.meplusplus.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.bumptech.glide.Glide;
import com.example.meplusplus.CalorieCalculator.CalculateMetabolismActivity;
import com.example.meplusplus.Chatting.ChattingActivity;
import com.example.meplusplus.DataSets.Food;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.FoodTracking.CaloriesActivity;
import com.example.meplusplus.LoginActivity;
import com.example.meplusplus.ProgressTracking.ActivityProgress;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

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
    TextView fragment_me_citate;
    CircularProgressIndicator circularProgress;
    Button activity_drink_water_add_200ml;
    View header;

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
    DatabaseReference waterReference;
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
    //Diverse
    String usserID;
    String waterUserID;
    Float sumCalories = 0f;
    Float sumProtein = 0f;
    Float sumCarbs = 0f;
    Float sumFat = 0f;
    Float sumSugar = 0f;
    //Diverse Water
    int waterDrank = 0;
    int wdr = 0;
    int current = 0;
    int sumwater = 0;

    //sumtotal
    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    //Timer Quotes
    long START_TIME_IN_MILLIS = (1000 * 10);
    CountDownTimer mCountDownTimer;
    boolean mTimerRunning;
    long mTimeLeftInMillis;
    long mEndTime;
    List<String> listQuotes = new LinkedList<>();


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
        initListQuotes();
        startTimer();

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

        activity_drink_water_add_200ml.setOnClickListener(view1 -> {
            String itemID = waterReference.push().getKey();
            Map<String, Integer> map = new HashMap<>();
            map.put("sumWater", 200);
            waterReference.child(waterUserID).child(itemID).setValue(map);
        });

        //Set water levels
        waterReference.child(waterUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long nrChildren = snapshot.getChildrenCount();
                if (nrChildren > 0) {
                    fragment_me_water.setText((200 * nrChildren) + "");
                    if(waterDrank>0){
                        circularProgress.setProgress((200 * nrChildren), waterDrank);
                    }
                    else
                        circularProgress.setProgress((200 * nrChildren), 2000);


                    if ((200 * nrChildren) >= waterDrank) {
                        circularProgress.setProgressColor(Color.GREEN);
                        circularProgress.setDotColor(Color.GRAY);
                    }
                } else {
                    fragment_me_water.setText(0 + "");
                    circularProgress.setProgress(0, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return view;
    }

    private void initListQuotes() {

        listQuotes.add("“The road to success and the road to failure are almost exactly the same.“");
        listQuotes.add("“It is better to fail in originality than to succeed in imitation.“");
        listQuotes.add("“Success is not final; failure is not fatal: It is the courage to continue that counts.“");
        listQuotes.add("“Success usually comes to those who are too busy looking for it.”");
        listQuotes.add("“I never dreamed about success. I worked for it.”");
        listQuotes.add("“Setting goals is the first step in turning the invisible into the visible.”");
        listQuotes.add("“It’s not about better time management. It’s about better life management”");
        listQuotes.add("“All our dreams can come true, if we have the courage to pursue them.” ");
        listQuotes.add("“The secret of getting ahead is getting started.”");
        listQuotes.add("“The best time to plant a tree was 20 years ago. The second best time is now.”");
        listQuotes.add("“It’s hard to beat a person who never gives up.”");
        listQuotes.add("“If people are doubting how far you can go, go so far that you can’t hear them anymore.”");
        String[] listStr = new String[]{"“Fairy tales are more than true: not because they tell us that dragons exist, but because they tell us that dragons can be beaten.”",
                "“Everything you can imagine is real.”",
                "“Do one thing every day that scares you.”",
                "“It’s no use going back to yesterday, because I was a different person then.”",
                "“Smart people learn from everything and everyone, average people from their experiences, stupid people already have all the answers.” ",
                "“Do what you feel in your heart to be right―for you’ll be criticized anyway.”",
                "“Happiness is not something ready made. It comes from your own actions.”",
                "“Whatever you are, be a good one.”",
                "“You can either experience the pain of discipline or the pain of regret. The choice is yours.”",
                "“If we have the attitude that it’s going to be a great day it usually is.” ",
                "“Impossible is just an opinion.”",
                "“Your passion is waiting for your courage to catch up.”",
                "“One day or day one. You decide.”",
        };
        listQuotes.addAll(Arrays.asList(listStr));
    }


    private void initView(@NonNull View view) {
        //Controale
        fragment_me_open_drawer = view.findViewById(R.id.fragment_me_open_drawer);
        drawerLayout = view.findViewById(R.id.drawerNavTest);
        fragment_me_chatActivity = view.findViewById(R.id.fragment_me_chatActivity);
        fragment_me_add_post = view.findViewById(R.id.fragment_me_add_post);
        fragment_me_reset_macros = view.findViewById(R.id.fragment_me_reset_macros);
        fragment_me_citate = view.findViewById(R.id.fragment_me_citate);

        //CALORIE TRACKING
        fragment_me_add_food_item = view.findViewById(R.id.fragment_me_add_food_item);
        fragment_me_calories = view.findViewById(R.id.fragment_me_calories);
        fragment_me_protein = view.findViewById(R.id.fragment_me_protein);
        fragment_me_carbs = view.findViewById(R.id.fragment_me_carbs);
        fragment_me_fats = view.findViewById(R.id.fragment_me_fats);
        fragment_me_sugar = view.findViewById(R.id.fragment_me_sugar);
        fragment_me_water = view.findViewById(R.id.fragment_me_water);

        //max values
        fragment_me_max_calories = view.findViewById(R.id.fragment_me_max_calories);
        fragment_me_max_protein = view.findViewById(R.id.fragment_me_max_protein);
        fragment_me_max_carbs = view.findViewById(R.id.fragment_me_max_carbs);
        fragment_me_max_fats = view.findViewById(R.id.fragment_me_max_fats);
        frament_me_max_sugar = view.findViewById(R.id.frament_me_max_sugar);
        fragment_me_max_water = view.findViewById(R.id.fragment_me_max_water);

        //Circular Progress Bar
        circularProgress = view.findViewById(R.id.circular_progress);
        circularProgress.setCurrentProgress(0);
        circularProgress.setTextColor(Color.WHITE);
        activity_drink_water_add_200ml = view.findViewById(R.id.activity_drink_water_add_200ml);

        navigationView = view.findViewById(R.id.drawerNavigationView);
        //Sa putem modifica imaginea din header - S.O
        header = navigationView.getHeaderView(0);

        //Drawer items
        drawer_circle_image_view_profile = header.findViewById(R.id.drawer_circle_image_view_profile);
        header_for_drawer_text_username = header.findViewById(R.id.header_for_drawer_text_username);
        header_for_drawer_button_view_profile = header.findViewById(R.id.header_for_drawer_button_view_profile);

        //Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("users");
        foodsReference = database.getReference("foods");
        waterReference = database.getReference("water");
        waterUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    private void getcaloriesForTheDay() {
        //Daca avem ceva
        usserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        foodsReference.child(usserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    Food food = (item.getValue(Food.class));
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

                if (sumProtein != null)
                    fragment_me_protein.setText(Math.round(sumProtein) + "");
                else
                    fragment_me_protein.setText("0");

                if (sumCarbs != null)
                    fragment_me_carbs.setText(Math.round(sumCarbs) + "");
                else
                    fragment_me_carbs.setText("0");

                if (sumFat != null)
                    fragment_me_fats.setText(Math.round(sumFat) + "");
                else
                    fragment_me_fats.setText("0");

                if (sumSugar != null)
                    fragment_me_sugar.setText(Math.round(sumSugar) + "");
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
                sumProtein = 0f;
                sumCarbs = 0f;
                sumFat = 0f;
                sumSugar = 0f;
                Toast.makeText(getContext(), "Calories have been reset", Toast.LENGTH_SHORT).show();
                fragment_me_calories.setText(sumCalories + "");
                fragment_me_protein.setText(sumProtein + "");
                fragment_me_carbs.setText(sumCarbs + "");
                fragment_me_fats.setText(sumFat + "");
                fragment_me_sugar.setText(sumSugar + "");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Err", Toast.LENGTH_SHORT).show();
            }
        });

        waterReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                fragment_me_water.setText(0 + "");
                circularProgress.setProgress(0, waterDrank);
                circularProgress.setProgressColor(Color.BLUE);
                circularProgress.setDotColor(Color.BLACK);
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
                    if(!(snapshot.getValue(User.class).getImageurl().equals("default"))){
                        Picasso.get().load(snapshot.getValue(User.class).getImageurl()).into(drawer_circle_image_view_profile);
                    }
                    else{
                        drawer_circle_image_view_profile.setBackgroundResource(R.drawable.ic_baseline_account_circle_24);
                    }
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
                startActivity(new Intent(getContext(), ActivityProgress.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
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
                auth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    private void setMaxValuesCount() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = prefs.getString("sendItemsList", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> items = gson.fromJson(json, type);

        if (items != null) {
            fragment_me_max_calories.setText(items.get(0));
            fragment_me_max_protein.setText(items.get(1));
            fragment_me_max_carbs.setText(items.get(2));
            fragment_me_max_fats.setText(items.get(3));
            frament_me_max_sugar.setText(items.get(4));
        } else {
            fragment_me_max_calories.setText(0 + "");
            fragment_me_max_protein.setText(0 + "");
            fragment_me_max_carbs.setText(0 + "");
            fragment_me_max_fats.setText(0 + "");
            frament_me_max_sugar.setText(0 + "");
        }
    }

    private void setWaterMaxValue() {
        prefs = getContext().getSharedPreferences("msp", Context.MODE_PRIVATE);
        waterDrank = prefs.getInt("waterDrank", 0);
        if (waterDrank > 0) {
            fragment_me_max_water.setText(waterDrank + "");
        } else {
            fragment_me_max_water.setText(2000+"");
        }

    }

    private void setMaxProgress() {
        prefs = getContext().getSharedPreferences("msp", Context.MODE_PRIVATE);
        wdr = prefs.getInt("waterDrank", 0);
        circularProgress.setMaxProgress(Math.max(wdr, 0));
    }


    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;

                String randomStr = listQuotes.get(new Random().nextInt(listQuotes.size()));
                if (randomStr != null) {
                    fragment_me_citate.setText(randomStr);
                } else
                    fragment_me_citate.setText("The greatest power comes from within.");
                startTimer();
            }
        }.start();
        mTimerRunning = true;
    }


    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
            } else {
                startTimer();
            }
        }
    }
}
