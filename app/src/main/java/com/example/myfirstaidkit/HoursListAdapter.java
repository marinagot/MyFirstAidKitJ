package com.example.myfirstaidkit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myfirstaidkit.data.TakeHours;

import java.text.SimpleDateFormat;
import java.util.List;

class HoursListAdapter<T> extends ArrayAdapter<T> {

    private List<TakeHours> medicineHours;
    private List<TakeHours> removedHours;
    private int layoutId;
    private static LayoutInflater inflater = null;

    HoursListAdapter(Context context, int layoutId, List<TakeHours> medicineHours, List<TakeHours> removedHours) {
        super(context, 0 , (List<T>) medicineHours);
        this.medicineHours = medicineHours;
        this.removedHours = removedHours;
        this.layoutId = layoutId;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return medicineHours.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return ((List<T>)medicineHours).get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, null);
        }

        TextView header = convertView.findViewById(R.id.hour_list_item_header);
        header.setText(new SimpleDateFormat("HH:mm").format(medicineHours.get(position).getHour()));

        ImageButton deleteImageView = convertView.findViewById(R.id.hour_list_item_delete);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TakeHours hour = medicineHours.get(position);
                removedHours.add(hour);
                remove((T)hour);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}