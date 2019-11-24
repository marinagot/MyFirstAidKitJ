package com.example.myfirstaidkit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.Medicine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class MedicinesListAdapter<T> extends ArrayAdapter<T> {

    Context context;
    List<T> data;
    int style;
    private static LayoutInflater inflater = null;

    DataBaseOperations us;

    public MedicinesListAdapter(Context context, int layoutId, List<T> data) {
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
        TextView header = vi.findViewById(R.id.list_item_header);
        TextView text = vi.findViewById(R.id.list_item_text);
        TextView dose = vi.findViewById(R.id.list_item_dose);
        TextView expiryDate = vi.findViewById(R.id.list_item_expiry_date);
        header.setText(((Medicine) data.get(position)).getName());
        text.setText(((Medicine) data.get(position)).getType());
        dose.setText("Dosis restantes: " + ((Medicine) data.get(position)).getDoseNumber().toString());
        String dateText = new SimpleDateFormat("dd MMM yyyy").format(new Date(((Medicine) data.get(position)).getExpirationDate()));
        expiryDate.setText("Caducidad: " + dateText);

        ImageButton editImageView = vi.findViewById(R.id.list_item_edit);
        editImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                add(data.get(position));
                notifyDataSetChanged();
            }
        });

        ImageButton deleteImageView = vi.findViewById(R.id.list_item_delete);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new ApiCallThread<String>(new AsyncResponse<String>(){
                    @Override
                    public String apiCall(Object... params) {
                        return us.deleteMedicine((Medicine) params[1]);
                    }

                    @Override
                    public void processFinish(View v, String result){
                        remove(data.get(position));
                        notifyDataSetChanged();
                    }
                }).execute(v, data.get(position));
            }
        });
        return vi;
    }

    public void refresh() {
        clear();
        notifyDataSetChanged();
    }
}