package com.example.meplusplus;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.meplusplus.Fragments.AccountFragment;
import com.example.meplusplus.Fragments.MeFragment;
import com.example.meplusplus.Fragments.RecipesFragment;
import com.example.meplusplus.Fragments.SearchPeopleFragment;
import com.example.meplusplus.Fragments.Social_PageFragment;
import com.example.meplusplus.WaterReminder.ReminderBroadcast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*

       Status: RFP
       CREATED DATE: 08/21/2022
       UPDATED DATE: 08/21/2022

       1.
       Updated Date: 09/02/2022
       Notes: Changed logic and redirect fragments when onBackPressed


 */
@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    //Controale
    BottomNavigationView bottomNavigationView;
    Fragment fragment;

    //Diverse
    int ID_fragment;
    boolean openF2;
    boolean isDarkModeOn;
    boolean wantReminders;

    //Shared Preferences
    Bundle extras;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadDarkWhiteModeOnStart();
        createNotificationChannel();

        sharedPreferences = getSharedPreferences("wantReminders", MODE_PRIVATE);
        wantReminders = sharedPreferences.getBoolean("yesToReminders", false);

        if (wantReminders) {
            Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
            PendingIntent pendingIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            }
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            long curr = System.currentTimeMillis();
            long oneMinute = 1000 * 5;
            //long oneMinute=1000*60*60; sa fie din ora in ora
            alarmManager.set(AlarmManager.RTC_WAKEUP, oneMinute, pendingIntent);
        }


        //Daca nu avem niciun fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.main_fragment_container, new MeFragment()).commit();
        }

        bottomNavigationView.setOnItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            displayFragment(item.getItemId());
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).addToBackStack(null).commit();
            return true;
        });

        //Switch from PostActivity to SocialPageFragment
        extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("openSocialPageFragment"))
            openF2 = extras.getBoolean("openSocialPageFragment");
        if (openF2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new Social_PageFragment()).addToBackStack(null).commit();
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
        //Switch from Calories Activity to MeFragment
        if (extras != null && extras.containsKey("MeFragmentPLS")) {
            openF2 = extras.getBoolean("MeFragmentPLS");

        }
        if (openF2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new MeFragment()).addToBackStack(null).commit();
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }
        //Switch from Calories Activity to MeFragment
        if (extras != null && extras.containsKey("MetabolismToMeFragment")) {
            openF2 = extras.getBoolean("MetabolismToMeFragment");

        }
        if (openF2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new MeFragment()).addToBackStack(null).commit();
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }

        //Switch from Drink Water Activity to MeFragment
        if (extras != null && extras.containsKey("MeFragmentPLS2")) {
            openF2 = extras.getBoolean("MeFragmentPLS2");

        }
        if (openF2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new MeFragment()).addToBackStack(null).commit();
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }
    }



    //Switch Between Fragments
    private void displayFragment(int itemId) {
        if (R.id.me_page == itemId) {
            fragment = new MeFragment();
            ID_fragment = 1;
        } else if (R.id.social_page == itemId) {
            fragment = new Social_PageFragment();
            ID_fragment = 2;
        } else if (R.id.search_page == itemId) {
            fragment = new SearchPeopleFragment();
            ID_fragment = 3;
        } else if (R.id.account_page == itemId) {
            fragment = new AccountFragment();
            ID_fragment = 4;
        } else if (R.id.recipes == itemId) {
            fragment = new RecipesFragment();
            ID_fragment = 5;
        }
    }

    //Initializare
    private void init() {
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
    }


    //Cand apas butonul de back de la telefon
    @Override
    public void onBackPressed() {
        //Daca nu e MeFragment
        if (getFragmentManager().getBackStackEntryCount() <= 0)
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        goHome();
        super.onBackPressed();

    }

    private void goHome() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }




    private void loadDarkWhiteModeOnStart() {
        //Load White/Dark Mode on App Start
        sharedPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MePlusPlus";
            String description = "Take another sip...";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyMe", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}