package com.example.meplusplus.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meplusplus.DataSets.PostItem;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.Utils.CommentDetailActivity;
import com.example.meplusplus.R;
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


// RFP
/*
       CREATED DATE: 8/25/2022
       UPDATED DATE: 8/25/2022
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    //Utilitati
    PostItem p;
    User u;
    Context context;
    List<PostItem> items;

    //Firebase
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference referenceHearts;
    DatabaseReference referenceStrikes;
    FirebaseAuth auth;


    public PostAdapter(List<PostItem> items, Context context) {
        this.items = items;
        this.context = context;
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.social_page_post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        init();
        p = items.get(position);
        String ID=p.getPostid();
        SetDetails(p,holder);

        // Number of hearts
        referenceHearts.child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.social_page_number_of_likes.setText(snapshot.getChildrenCount()+"  ");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        postLike(holder.social_page_like,ID);

        //Number of strikes
      referenceStrikes.child(ID).child(p.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.social_page_number_of_comments.setText(snapshot.getChildrenCount()+ "   ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      referenceStrikes.child(ID).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              holder.social_page_number_of_comments.setText(snapshot.getChildrenCount()+ "");
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

        holder.social_page_place_a_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(context, CommentDetailActivity.class);
                intent.putExtra("postid",ID);
                intent.putExtra("authorid",ID);
                context.startActivity(intent);
            }
        });

    }

    private void postLike(ImageView img,String ID) {
        referenceHearts.child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    img.setImageResource(R.drawable.ic_baseline_favorite);
                    img.setTag("NotFail");
                } else {
                    img.setImageResource(R.drawable.ic_baseline_favorite_24);
                    img.setTag("heart");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void SetDetails(PostItem p,@NonNull ViewHolder holder){
        reference.child(p.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u = snapshot.getValue(User.class);
                String imgURL = u.getImageurl().toString();
                if (!imgURL.equals("default")) {
                    holder.social_page_image_profile.setImageResource(R.drawable.ic_baseline_person_pin_24);

                    holder.social_page_username.setText(u.getUsername());
                    holder.social_page_image_description.setText(p.getDescription());
                    Picasso.get().load(p.getImageurl()).into(holder.social_page_image_content);
                    Picasso.get().load(u.getImageurl()).into(holder.social_page_image_profile);

                    holder.social_page_strike.setImageResource(R.drawable.ic_baseline_bolt_24);
                } else {
                    if(firebaseUser.getPhotoUrl()==null){
                        holder.social_page_image_profile.setImageResource(R.drawable.ic_baseline_person_pin_24);

                    }
                    else
                        Glide.with(context).load(firebaseUser.getPhotoUrl()).into( holder.social_page_image_profile);

                    holder.social_page_username.setText(u.getUsername());
                    holder.social_page_image_description.setText(p.getDescription());
                    Picasso.get().load(p.getImageurl()).into(holder.social_page_image_content);
                    holder.social_page_strike.setImageResource(R.drawable.ic_baseline_bolt_24);

                }

                holder.social_page_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.social_page_like.getTag().equals("heart")) {
                            referenceHearts
                                    .child(p.getPostid()).child(firebaseUser.getUid()).setValue(true);
                        } else {
                            referenceHearts
                                    .child(p.getPostid()).child(firebaseUser.getUid()).removeValue();
                        }
                    }
                });
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
        referenceHearts=database.getReference().child("hearts");
        referenceStrikes=database.getReference().child("strikes");

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView social_page_image_profile;
        TextView social_page_username;
        TextView social_page_image_description;
        ImageView social_page_image_content;
        TextView social_page_place_a_comment;
        TextView social_page_number_of_likes, social_page_number_of_comments;
        ImageView social_page_like,social_page_strike;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            social_page_image_profile = itemView.findViewById(R.id.social_page_image_profile);
            social_page_username = itemView.findViewById(R.id.social_page_username);
            social_page_image_description = itemView.findViewById(R.id.social_page_image_description);
            social_page_image_content = itemView.findViewById(R.id.social_page_image_content);
            social_page_place_a_comment = itemView.findViewById(R.id.social_page_place_a_comment);
            social_page_number_of_likes = itemView.findViewById(R.id.social_page_number_of_likes);
            social_page_number_of_comments = itemView.findViewById(R.id.social_page_number_of_comments);
            social_page_like=itemView.findViewById(R.id.social_page_like);
            social_page_strike=itemView.findViewById(R.id.social_page_strike);
        }
    }
}
