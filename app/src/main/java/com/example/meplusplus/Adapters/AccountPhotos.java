package com.example.meplusplus.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.meplusplus.DataSets.PostItem;
import com.example.meplusplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AccountPhotos extends RecyclerView.Adapter<AccountPhotos.ViewHolder> {

    private final Context context;
    private final List<PostItem> items;
    private final FirebaseAuth auth;
    private PostItem post;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public AccountPhotos(Context mContext, List<PostItem> mPosts) {
        this.context = mContext;
        this.items = mPosts;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        post = items.get(position);
        init();
        setDetails(post, holder);
        initiateImagePopup(holder);

        holder.imageView.setOnLongClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Do you want to delete the post? ");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", (dialog, which) -> dialog.dismiss());
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", (dialog, which) -> {
                user = auth.getCurrentUser();
                reference.child(items.get(position).getPostid()).removeValue();
                Toast.makeText(context, "The Post Has Been Deleted", Toast.LENGTH_SHORT).show();
            });
            alertDialog.show();
            return false;
        });
    }

    private void initiateImagePopup(ViewHolder holder) {
        ImagePopup imagePopup = new ImagePopup(context);
        imagePopup.setWindowHeight(850);
        imagePopup.setWindowWidth(850);
        imagePopup.setBackgroundColor(Color.parseColor("#00000000"));
        imagePopup.setFullScreen(false);
        imagePopup.setHideCloseIcon(true);
        imagePopup.setImageOnClickClose(true);

        imagePopup.initiatePopup(holder.imageView.getDrawable());
        if (!TextUtils.isEmpty(post.getImageurl())) {
            imagePopup.initiatePopupWithPicasso(post.getImageurl());
        }

        holder.imageView.setOnClickListener(view -> imagePopup.viewPopup());
    }

    private void init() {
        database = FirebaseDatabase.getInstance("https://applicenta-8582b-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("posts");
    }

    private void setDetails(PostItem post, ViewHolder holder) {
        Picasso.get().load(post.getImageurl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.account_photos_image);
        }
    }
}
