package com.example.meplusplus.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.DataSets.Comm;
import com.example.meplusplus.DataSets.User;
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
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;



/*

            Status: RFP
            CREATED DATE: 8/28/2022
            UPDATED DATE: 8/28/2022

        1.
        UPDATED DATE: 8/29/2022
        Added Glide to put the profile image for Google Sign In users
 */

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHolder> {

    //Controale
    final Context context;
    final List<Comm> list;
    Comm comm;
    User u;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;
    final FirebaseUser user;
    final FirebaseAuth auth;
    DatabaseReference reference1;
    final String ID;

    public Comment_Adapter(Context context, String ID, List<Comm> list) {
        this.context = context;
        this.list = list;
        this.ID = ID;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        init();
        comm = list.get(position);
        SeeDetails(comm, holder);

        holder.itemView.setOnClickListener(view -> {
            AlertDialog alertDialog= new AlertDialog.Builder(context).create();
            if(comm.getPublisher().equals(user.getUid())){
                alertDialog.setTitle("Do you want to delete?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", (dialog, which) -> dialog.dismiss());
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", (dialog, which) -> {

                    database.getReference().child("strikes")
                            .child(ID).child(comm.getId()).removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.dismiss();
                });
                alertDialog.show();
            }
        });
    }


    private void SeeDetails(Comm comm, ViewHolder holder) {

        reference.child(comm.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!Objects.requireNonNull(snapshot.getValue(User.class)).getImageurl().equals("default")) {
                    Picasso.get().load(Objects.requireNonNull(snapshot.getValue(User.class)).getImageurl()).into(holder.comment_card_item_people_profile_image);
                } else {
                    holder.comment_card_item_people_profile_image.setImageResource(R.drawable.ic_baseline_face_24);
                }
                holder.comment_card_item_people_username.setText(Objects.requireNonNull(snapshot.getValue(User.class)).getUsername());
                holder.comment_card_item_comment_text.setText(comm.getStrike());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final CircleImageView comment_card_item_people_profile_image;
        final TextView comment_card_item_people_username;
        final TextView comment_card_item_comment_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_card_item_people_profile_image = itemView.findViewById(R.id.comment_card_item_people_profile_image);
            comment_card_item_people_username = itemView.findViewById(R.id.comment_card_item_people_username);
            comment_card_item_comment_text = itemView.findViewById(R.id.comment_card_item_comment_text);
        }
    }

    private void init() {
        //FirebaseDatabase
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference().child("users");


    }
}
