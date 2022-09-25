package com.example.meplusplus.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.Chatting.MessageActivity;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


/*
    STATUS: RFP
  CREATED DATE: 04/09/2022
  UPDATED DATE: 04/09/2022

 */
public class User_Adapter_Chatting extends RecyclerView.Adapter<User_Adapter_Chatting.ViewHolder> {

    final List<User> list;
    //Firebase
    FirebaseAuth auth;
    FirebaseUser user;
    //Controale
    Context context;
    User item;

    //Pt a face tranzitia mai frumoasa intre slide-uri
    Activity activity;

    public User_Adapter_Chatting() {
        this.list = null;
    }

    public User_Adapter_Chatting(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.people_card_item_chatting, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        init();
        assert list != null;
        item = list.get(position);
        setdDetails(user, holder);

        Objects.requireNonNull(holder).itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("userTargetID", list.get(position).getId());
                    intent.putExtra("userTarget", list.get(position).getUsername());
                    intent.putExtra("userImgUrl", list.get(position).getImageurl());
                    context.startActivity(intent);
                    activity = (Activity) context;
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                    activity.finish();
                }
        );

    }

    private void setdDetails(FirebaseUser user, User_Adapter_Chatting.ViewHolder holder) {
        String imageURL = item.getImageurl();
        holder.username.setText(item.getUsername());
        holder.name.setText(item.getName());
        Picasso.get().load(imageURL).into(holder.circleImageView);
    }

    private void init() {
        user = auth.getInstance().getCurrentUser();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView circleImageView;
        private final TextView username;
        private final TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.chatting_people_profile_image);
            username = itemView.findViewById(R.id.chatting_people_username);
            name = itemView.findViewById(R.id.chatting_people_name);
        }
    }
}
