package com.example.meplusplus.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.meplusplus.R;
import com.example.meplusplus.Utils.PostActivity;

public class MeFragment extends Fragment {

    //Controale
    ImageView fragment_me_open_drawer;
    ImageView fragment_me_chatActivity;
    ImageView drawer_circle_image_view_profile;
    ImageView fragment_me_add_post;
    //Drawer
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);


        fragment_me_open_drawer.setOnClickListener(view1 -> drawerLayout.openDrawer(GravityCompat.START));

        fragment_me_add_post.setOnClickListener(view12 -> {
            getContext().startActivity(new Intent(getContext(), PostActivity.class));
            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.slide_out);
        });

        return view;
    }


    private void initView(View view) {
        //Controale
        fragment_me_open_drawer = view.findViewById(R.id.fragment_me_open_drawer);
        drawerLayout = view.findViewById(R.id.drawerNavTest);
        fragment_me_chatActivity=view.findViewById(R.id.fragment_me_chatActivity);
        fragment_me_add_post=view.findViewById(R.id.fragment_me_add_post);


        //Din drawer header
        drawer_circle_image_view_profile=view.findViewById(R.id.drawer_circle_image_view_profile);

    }

}