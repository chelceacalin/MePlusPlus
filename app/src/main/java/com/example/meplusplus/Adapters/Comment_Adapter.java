package com.example.meplusplus.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.meplusplus.DataSets.Comm;
import com.example.meplusplus.DataSets.PostItem;
import com.example.meplusplus.DataSets.User;
import com.example.meplusplus.Fragments.AccountFragment;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


//RFP
/*

CREATED DATE: 8/28/2022
UPDATED DATE: 8/28/2022


-- UPDATED DATE: 8/29/2022
        Added Glide to put the profile image for Google Sign In users
 */

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHolder> {

    //Controale
    Context context;
    List<Comm> list;
    Comm comm;
    User u;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference reference1;
    String ID;

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
        return new Comment_Adapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        init();
        comm = list.get(position);
        SeeDetails(comm, holder);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog= new AlertDialog.Builder(context).create();
                if(comm.getPublisher().equals(user.getUid())){
                    alertDialog.setTitle("Do you want to delete?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                  database.getReference().child("strikes")
                                    .child(ID).child(comm.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

    }


    private void SeeDetails(Comm comm, ViewHolder holder) {

        reference.child(comm.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.getValue(User.class).getImageurl().equals("default")) {
                    Picasso.get().load(snapshot.getValue(User.class).getImageurl()).into(holder.comment_card_item_people_profile_image);
                    holder.comment_card_item_people_username.setText(snapshot.getValue(User.class).getUsername());
                    holder.comment_card_item_comment_text.setText(comm.getStrike());
                } else {
                    holder.comment_card_item_people_profile_image.setImageResource(R.drawable.ic_baseline_face_24);
                    holder.comment_card_item_people_username.setText(snapshot.getValue(User.class).getUsername());
                    holder.comment_card_item_comment_text.setText(comm.getStrike());
                }

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView comment_card_item_people_profile_image;
        TextView comment_card_item_people_username;
        TextView comment_card_item_comment_text;

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
