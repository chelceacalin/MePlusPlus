package com.example.meplusplus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.meplusplus.Fragments.AccountFragment;
import com.example.meplusplus.Fragments.MeFragment;
import com.example.meplusplus.Fragments.SearchPeopleFragment;
import com.example.meplusplus.Fragments.Social_PageFragment;
import com.example.meplusplus.Utils.PostActivity;
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
    Bundle extras;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        bottomNavigationView.setOnItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {
            displayFragment(item.getItemId());
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).addToBackStack(null).commit();
            return true;
        });

         extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("openSocialPageFragment"))
            openF2 = extras.getBoolean("openSocialPageFragment");
        if (openF2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new Social_PageFragment()).addToBackStack(null).commit();
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
    }

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
        } else if (R.id.add_post == itemId) {
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }
    }

    //Initializare
    private void init() {
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        fragment = new Fragment();
    }


    //Cand minimizezi aplicatia
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_up);
    }

    //Cand apas butonul de back de la telefon
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            // moveTaskToBack(true);
            goHome();
            finish();
        } else {
            //Daca nu e MeFragment
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            super.onBackPressed();
        }

    }

    private void goHome() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }


}