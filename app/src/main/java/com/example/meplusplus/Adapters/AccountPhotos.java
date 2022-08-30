package com.example.meplusplus.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.DataSets.PostItem;
import com.example.meplusplus.R;
import com.squareup.picasso.Picasso;

import java.util.List;

// RFP
/*
       CREATED DATE: 8/29/2022
       UPDATED DATE: 8/29/2022
 */
public class AccountPhotos extends RecyclerView.Adapter<AccountPhotos.ViewHolder> {

     Context context;
     List<PostItem> items;
     PostItem post;

    public AccountPhotos(Context mContext, List<PostItem> mPosts) {
        this.context = mContext;
        this.items = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountPhotos.ViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        post = items.get(position);
        setDetails(post,holder);

    }

    private void setDetails(PostItem post,ViewHolder holder) {
        Picasso.get().load(post.getImageurl()).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.account_photos_image);
        }
    }

}
