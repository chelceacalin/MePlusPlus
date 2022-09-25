package com.example.meplusplus.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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

        holder.workout_split_item_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getMuscles_worked().equals("")) {
                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDetails(ViewHolder holder, int position) {


        if (list.get(position).getMuscles_worked().equals("")) {
            holder.workout_split_item_layoutbackgroundcolor.setBackgroundColor(context.getResources().getColor(R.color.DimGray));
            holder.workout_split_split_muscles_worked.setVisibility(View.GONE);
            holder.workout_split_item_textview_muscles.setVisibility(View.GONE);
            holder.workout_split_split_name.setGravity(Gravity.CENTER_HORIZONTAL);
            holder.workout_split_split_name.setTextColor(context.getResources().getColor(R.color.DarkOrange));
            holder.workout_split_split_name.setText(list.get(position).getSplit_name());
            holder.workout_split_split_name.setPadding(10, 10, 10, 10);
        } else {
            holder.workout_split_item_layoutbackgroundcolor.setBackgroundColor(context.getResources().getColor(R.color.black));

            holder.workout_split_split_muscles_worked.setVisibility(View.VISIBLE);
            holder.workout_split_item_textview_muscles.setVisibility(View.VISIBLE);
            holder.workout_split_split_muscles_worked.setText(list.get(position).getMuscles_worked());
            holder.workout_split_split_name.setText(list.get(position).getSplit_name());
        }
    }

    private void init() {
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("workout");
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
