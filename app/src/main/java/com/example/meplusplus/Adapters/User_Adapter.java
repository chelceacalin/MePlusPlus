package com.example.meplusplus.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.Chatting.MessageActivity;
import com.example.meplusplus.model.User;
import com.example.meplusplus.Fragments.AccountFragment;
import com.example.meplusplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class User_Adapter extends RecyclerView.Adapter<User_Adapter.ViewHolder> {

    final List<User> list;
    final boolean eF;
    //Firebase
    FirebaseAuth auth;
    FirebaseUser user;
    //Controale
    Context context;
    User item;

    public User_Adapter() {
        this.list = null;
        this.eF = false;
    }

    public User_Adapter(Context context, List<User> list, boolean eF) {
        this.context = context;
        this.list = list;
        this.eF = eF;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.people_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        init();
        assert list != null;
        item = list.get(position);
        setdDetails(user, holder);

        holder.sayhiButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("userTargetID", list.get(position).getId());
            intent.putExtra("userTarget", list.get(position).getUsername());
            intent.putExtra("userImgUrl", list.get(position).getImageurl());
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(view -> {
            context.getSharedPreferences("PID", Context.MODE_PRIVATE)
                    .edit().putString("profileId", list.get(position).getId()).apply();
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, new AccountFragment()).commit();
        });
    }

    private void setdDetails(FirebaseUser user, ViewHolder holder) {
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
        assert list != null;
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Button sayhiButton;
        private final CircleImageView circleImageView;
        private final TextView username;
        private final TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.people_profile_image);
            username = itemView.findViewById(R.id.people_username);
            name = itemView.findViewById(R.id.people_name);
            sayhiButton = itemView.findViewById(R.id.people_say_hi);
        }
    }

}
