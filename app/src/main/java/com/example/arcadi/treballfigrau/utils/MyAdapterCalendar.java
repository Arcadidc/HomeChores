package com.example.arcadi.treballfigrau.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.arcadi.treballfigrau.R;
import com.example.arcadi.treballfigrau.general.Task;

import java.util.List;

public class MyAdapterCalendar extends ArrayAdapter<Task> {

    private List<Task> list;
    private LayoutInflater mInflater;

    public MyAdapterCalendar(Context context, List<Task> list) {
        super(context, R.layout.activity_calendar_task, list);
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    static class ViewHolder {
        TextView text;

    }

    public void addItems(List<Task> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.activity_calendar_task, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.label);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setText(list.get(position).getName());

        return convertView;
    }
}