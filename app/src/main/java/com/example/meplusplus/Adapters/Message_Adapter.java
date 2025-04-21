package com.example.meplusplus.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.R;
import com.example.meplusplus.context.DbContext;
import com.example.meplusplus.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.Viewholder> {

    //Controale
    final Context context;
    final String Uname;
    final List<Message> list;
    public boolean isSender;
    Message item;
    Message mesaj;

    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    int pozitie;

    public Message_Adapter(Context context, String uname, List<Message> list) {
        Uname = uname;
        this.context = context;
        this.list = list;
        isSender = false;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 2)
            view = LayoutInflater.from(context).inflate(R.layout.message_received, parent, false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.message_sent, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        item = list.get(position);
        holder.textView.setText(item.getMessage());
        init();
        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override

            public boolean onLongClick(View view) {
                if (list.get(position).getWhosentit().equals(user.getUid())) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Do you want to delete?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", (dialog, which) -> dialog.dismiss());
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", (dialog, which) ->
                    {
                        pozitie = position;
                        Intent intent = new Intent("refresh_adapter");
                        intent.putExtra("pozitie", pozitie);
                        intent.putExtra("messageHolder", user.getUid());
                        intent.putExtra("messageReceiver", list.get(position).getToWhom());
                        intent.putExtra("refreshPLS", list.get(position).getMessageID());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        alertDialog.dismiss();
                    });
                    alertDialog.show();
                }
                return true;
            }
        });
    }

    private void init() {
        DbContext dbContext = DbContext.getInstance();
        reference = dbContext.getReference().child("messages");
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
