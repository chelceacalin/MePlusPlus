package com.example.meplusplus.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.DataSets.Exercise;
import com.example.meplusplus.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    //Controale
    Context context;
    List<Exercise> list;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;

    //Shared preferences
    SharedPreferences.Editor editor;



    //Shared Preferences pt padding
    SharedPreferences sharedPreferences;
    boolean wanted;
    public ExerciseAdapter(Context context, List<Exercise> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExerciseAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.workout_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        init();
        setDetails(holder,position);

        sharedPreferences = context.getSharedPreferences("wantPadding", Context.MODE_PRIVATE);
        wanted = sharedPreferences.getBoolean("yes", false);
        if (wanted) { // Daca vor sa fie compact
            holder.workout_exercixe_split_name.setPadding(15, 8, 7, 10);
            holder.workout_exercise_item_textview_muscles.setVisibility(View.GONE);
            holder.workout_exercixe_split_muscles_worked.setTextColor(Color.CYAN);

        } else {

            if (list.get(position).getMuscles_worked().equals("")) {
                holder.workout_exercixe_split_name.setPadding(15, 15, 15, 15);
            } else {
                holder.workout_exercixe_split_name.setPadding(10, 30, 10, 50);
            }


        }

        holder.workout_exercise_cardview.setOnClickListener(view -> {
            Intent intent=new Intent("android.intent.action.VIEW", Uri.parse(list.get(position).getExercise_link()));
            context.startActivity(intent);
        });

        holder.workout_exercise_cardview.setOnLongClickListener(view -> {
            holder.workout_exercie_background.setBackgroundColor(Color.DKGRAY);
            Intent intent = new Intent("custom-message");
            intent.putExtra("schimba","startAgain");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return true;
        });


    }

    private void setDetails(ViewHolder holder, int position) {

        holder.workout_exercixe_split_name.setText(list.get(position).getExercise_name());
        holder.workout_exercixe_split_muscles_worked.setText(list.get(position).getMuscles_worked());

    }

    private void init() {
        //Firebase
        database = FirebaseDatabase.getInstance("https://applicenta-8582b-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("exercise");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView workout_exercixe_split_name;
        TextView workout_exercixe_split_muscles_worked;
        TextView workout_exercise_item_textview_muscles;
        CardView workout_exercise_cardview;
        LinearLayout workout_exercie_background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workout_exercixe_split_name = itemView.findViewById(R.id.workout_exercixe_split_name);
            workout_exercixe_split_muscles_worked = itemView.findViewById(R.id.workout_exercixe_split_muscles_worked);
            workout_exercise_cardview = itemView.findViewById(R.id.workout_exercise_cardview);
            workout_exercie_background=itemView.findViewById(R.id.workout_exercie_background);
            workout_exercise_item_textview_muscles=itemView.findViewById(R.id.workout_exercise_item_textview_muscles);
        }
    }
}
