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

import java.util.List;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.Viewholder> {

    Context context;
    String Uname;
    List<Message> list;
    public boolean isSender;
    int send;
    int received;

    public Message_Adapter(Context context,String uname, List<Message> list) {
        Uname = uname;
        this.context=context;
        this.list = list;
        isSender=false;
        send=1;
        received=2;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if(viewType==send){
            view=LayoutInflater.from(context).inflate(R.layout.message_sent,parent,false);
        }
        else{
            view=LayoutInflater.from(context).inflate(R.layout.message_received,parent,false);
        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.textView.setText(list.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public  class Viewholder extends RecyclerView.ViewHolder{
        TextView textView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            if(isSender){
                textView=itemView.findViewById(R.id.message_sent_message);
            }
            else
            {
                textView=itemView.findViewById(R.id.message_received_message);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getWhosentit().equals(Uname))
        {
            isSender = true;
            return send;
        }
        else
        {
            isSender = false;
            return received;
        }

    }
}
