package com.example.meplusplus.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meplusplus.DataSets.PostItem;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    final Context context;
    final List<PostItem> items;
    //Firebase
    final FirebaseUser firebaseUser;
    final FirebaseAuth auth;
    //Utilitati
    PostItem p;
    User u;
    FirebaseDatabase database;
    DatabaseReference reference;
    String ID;
    //Zoom in
    PhotoViewAttacher mAttacher;

    public RecipeAdapter(List<PostItem> items, Context context) {
        this.items = items;
        this.context = context;
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.recipepostitem, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        init();
        //Photoview to be full scale
        holder.recipepostitem_content.setScaleType(ImageView.ScaleType.FIT_XY);
        p = items.get(position);
        ID = p.getPostid();
        SetDetails(p, holder);
    }


    private void SetDetails(PostItem p, @NonNull RecipeAdapter.ViewHolder holder) {
        reference.child(p.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u = snapshot.getValue(User.class);
                assert u != null;
                String imgURL = u.getImageurl();
                if (!imgURL.equals("default")) {
                    holder.recipepostitem_image_profile.setImageResource(R.drawable.ic_baseline_person_pin_24);
                    Picasso.get().load(u.getImageurl()).into(holder.recipepostitem_image_profile);
                } else {
                    assert firebaseUser != null;
                    if (firebaseUser.getPhotoUrl() == null) {
                        holder.recipepostitem_image_profile.setImageResource(R.drawable.ic_baseline_person_pin_24);

                    } else {
                        Glide.with(context).load(firebaseUser.getPhotoUrl()).into(holder.recipepostitem_image_profile);
                    }
                    mAttacher = new PhotoViewAttacher(holder.recipepostitem_content);
                }
                Picasso.get().load(p.getImageurl()).into(holder.recipepostitem_content);
                if (!(p.getDescription().equals(""))) {
                    holder.recipepostitem_description.setText(p.getDescription());
                } else {
                    holder.recipepostitem_description.setVisibility(View.GONE);
                }
                holder.recipepostitem_ingredients.setText(p.getIngredients());
                holder.recipepostitem_username.setText(u.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void init() {

        //FirebaseDatabase
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference().child("users");
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView recipepostitem_image_profile;
        TextView recipepostitem_username;
        TextView recipepostitem_description;
        PhotoView recipepostitem_content;
        TextView recipepostitem_ingredients;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipepostitem_image_profile = itemView.findViewById(R.id.recipepostitem_image_profile);
            recipepostitem_username = itemView.findViewById(R.id.recipepostitem_username);
            recipepostitem_description = itemView.findViewById(R.id.recipepostitem_image_description);
            recipepostitem_content = itemView.findViewById(R.id.recipepostitem_image_content);
            recipepostitem_ingredients = itemView.findViewById(R.id.recipepostitem_ingredients);
        }
    }


}
