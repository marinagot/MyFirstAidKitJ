package com.example.myfirstaidkit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.Medicine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class ErrorAdapter<T> extends ArrayAdapter<T> {

    Context context;
    List<T> data;
    int style;
    private static LayoutInflater inflater = null;

    DataBaseOperations us;

    public ErrorAdapter(Context context, int layoutId, List<T> data) {
        // TODO Auto-generated constructor stub
        super(context, 0 , data);
        this.context = context;
        this.data = data;
        this.style = layoutId;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        us = DataBaseOperations.get_Instance(getContext());
        if (vi == null)
            vi = inflater.inflate(style, null);
        TextView message = vi.findViewById(R.id.error_message);
        message.setText((String) data.get(position));

        return vi;
    }
}