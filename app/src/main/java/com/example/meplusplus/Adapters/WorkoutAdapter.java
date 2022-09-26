package com.example.meplusplus.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.DataSets.Workout_Split;
import com.example.meplusplus.R;
import com.example.meplusplus.Workout.WorkoutActivity;
import com.example.meplusplus.Workout.WorkoutStarted;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {

    //Controale
    Context context;
    List<Workout_Split> list;
    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;


    //Shared Preferences pt padding
    SharedPreferences sharedPreferences;
    boolean wanted;

    public WorkoutAdapter(Context context, List<Workout_Split> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkoutAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.workout_split_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        init();
        setDetails(holder, position);
        sharedPreferences = context.getSharedPreferences("wantPadding", Context.MODE_PRIVATE);
        wanted = sharedPreferences.getBoolean("yes", false);
//Nu stiu de ce, asa ca am facut manual sa nu mai il modifice sa arate ca un Titlu de Workout
        if (list.get(position).getSplit_name().equals("Pull")) {
            holder.workout_split_split_name.setGravity(Gravity.START);
            holder.workout_split_split_name.setTextColor(Color.WHITE);
        }

        if (wanted) { // Daca vor sa fie compact
            holder.workout_split_split_name.setPadding(15, 8, 7, 10);
            holder.workout_split_item_textview_muscles.setVisibility(View.GONE);
        } else {

            if (list.get(position).getMuscles_worked().equals("")) {
                holder.workout_split_split_name.setPadding(15, 15, 15, 15);
            } else {
                holder.workout_split_split_name.setPadding(10, 30, 10, 50);
            }


        }


        holder.workout_split_item_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getMuscles_worked().equals("")) {

                }
                else{
                if(list.get(position).getSplit_name().equals("Push")){
                    Intent intent=new Intent(context, WorkoutStarted.class);
                    context.startActivity(intent);

                }
                }
            }
        });


    }

    private void setDetails(ViewHolder holder, int position) {


        if (list.get(position).getMuscles_worked().equals("")) {

            holder.workout_split_item_layoutbackgroundcolor.setBackgroundColor(context.getResources().getColor(R.color.DimGray));
            holder.workout_split_split_muscles_worked.setVisibility(View.GONE);
            holder.workout_split_item_textview_muscles.setVisibility(View.GONE);
            holder.workout_split_split_name.setTextColor(context.getResources().getColor(R.color.DarkOrange));
            holder.workout_split_split_name.setGravity(Gravity.CENTER_HORIZONTAL);

        } else {
            holder.workout_split_item_layoutbackgroundcolor.setBackgroundColor(context.getResources().getColor(R.color.black));
            holder.workout_split_split_muscles_worked.setVisibility(View.VISIBLE);
            holder.workout_split_item_textview_muscles.setVisibility(View.VISIBLE);
            holder.workout_split_split_muscles_worked.setText(list.get(position).getMuscles_worked());
        }
        holder.workout_split_split_name.setText(list.get(position).getSplit_name());
    }

    private void init() {
        //Firebase
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("workout");

        //Diverse
        wanted = false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView workout_split_split_name;
        TextView workout_split_split_muscles_worked;
        TextView workout_split_item_textview_muscles;
        CardView workout_split_item_cardview;
        LinearLayout workout_split_item_layoutbackgroundcolor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workout_split_split_name = itemView.findViewById(R.id.workout_split_split_name);
            workout_split_split_muscles_worked = itemView.findViewById(R.id.workout_split_split_muscles_worked);
            workout_split_item_cardview = itemView.findViewById(R.id.workout_split_item_cardview);
            workout_split_item_textview_muscles = itemView.findViewById(R.id.workout_split_item_textview_muscles);
            workout_split_item_layoutbackgroundcolor = itemView.findViewById(R.id.workout_split_item_layoutbackgroundcolor);
        }
    }


}
