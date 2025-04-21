package com.example.meplusplus.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.Adapters.RecipeAdapter;
import com.example.meplusplus.R;
import com.example.meplusplus.context.DbContext;
import com.example.meplusplus.model.PostItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {

    //RecyclerView
    RecyclerView fragment_recipes_recyclerviewRecipes;
    List<PostItem> items;
    RecipeAdapter adapter;
    LinearLayoutManager manager;
    PostItem pItem;

    DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        init(view);
        initRecycler();
        readPosts();
        return view;
    }

    private void init(View view) {

        DbContext dbContext = DbContext.getInstance();
        //Firebase
        reference = dbContext.getReference().child("posts");
        //Controale
        fragment_recipes_recyclerviewRecipes = view.findViewById(R.id.fragment_recipes_recyclerviewRecipes);

    }

    private void initRecycler() {
        manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        fragment_recipes_recyclerviewRecipes.setLayoutManager(manager);

        items = new ArrayList<>();
        adapter = new RecipeAdapter(items, getContext());
        fragment_recipes_recyclerviewRecipes.setAdapter(adapter);
    }

    private void readPosts() {
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot i : snapshot.getChildren()) {
                    if (!(i.getValue(PostItem.class).getIngredients().isEmpty()))
                        items.add(i.getValue(PostItem.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}