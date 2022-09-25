package com.example.meplusplus.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.DataSets.Message;
import com.example.meplusplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.Viewholder> {

    //Controale
    final Context context;
    final String Uname;
    final List<Message> list;
    public boolean isSender;
    Message item;


    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;


    public Message_Adapter(Context context, String uname, List<Message> list) {
        Uname = uname;
        this.context = context;
        this.list = list;
        isSender = false;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 2) {
            view = LayoutInflater.from(context).inflate(R.layout.message_received, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false);
        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        item = list.get(position);
        holder.textView.setText(item.getMessage());
        init();


    }

    private void init() {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getWhosentit().equals(Uname)) {
            isSender = true;
            return 1;
        } else {
            isSender = false;
            return 2;
        }

    }

    public class Viewholder extends RecyclerView.ViewHolder {
        final TextView textView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            if (!isSender)
                textView = itemView.findViewById(R.id.message_received_message);
            else
                textView = itemView.findViewById(R.id.message_sent_message);

        }
    }
}
