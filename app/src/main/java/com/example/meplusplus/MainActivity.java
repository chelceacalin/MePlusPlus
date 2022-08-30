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

       Status: NM - change switch logic

       CREATED DATE: 8/21/2022
       UPDATED DATE: 8/21/2022
 */
@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    //Controale
    BottomNavigationView bottomNavigationView;
    Fragment fragment;

    //Diverse
    int ID_fragment;
    boolean openF2;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        bottomNavigationView.setOnItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) item -> {

            int id = item.getItemId();

            switch (id) {
                case R.id.me_page:
                    fragment = new MeFragment();
                    ID_fragment=1;
                    break;

                case R.id.social_page:
                    fragment = new Social_PageFragment();
                    ID_fragment=2;
                    break;

                case R.id.add_post:
                    Intent intent = new Intent(MainActivity.this, PostActivity.class);
                    startActivity(intent);
                    break;

                case R.id.search_page:
                    fragment = new SearchPeopleFragment();
                    ID_fragment=3;
                    break;

                case R.id.account_page:
                    fragment = new AccountFragment();
                    ID_fragment=4;
                    break;
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).addToBackStack(null).commit();
            }
            return true;
        });

        Bundle extras = getIntent().getExtras();
        if(extras!=null && extras.containsKey("openSocialPageFragment"))
            openF2= extras.getBoolean("openSocialPageFragment");
        if(openF2){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, new Social_PageFragment()).addToBackStack(null).commit();
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
    }

    //Initializare
    private void init() {
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        fragment=new Fragment();
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
      if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
            goHome();
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