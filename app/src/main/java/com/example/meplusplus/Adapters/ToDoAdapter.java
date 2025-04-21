package com.example.meplusplus.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.meplusplus.R;

import java.util.List;

public class ToDoAdapter extends BaseAdapter {

    Context context;
    List<String> list;
    boolean checked = false;

    public ToDoAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.to_do_item, viewGroup, false);

        String item = list.get(i);
        TextView textView = v.findViewById(R.id.to_do_tv);
        CheckBox checkBox = v.findViewById(R.id.to_do_status);


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checked = !checked;
                checkBox.setChecked(checked);

            }
        });

        textView.setText(item);
        return v;
    }
}
