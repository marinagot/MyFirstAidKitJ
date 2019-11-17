package com.example.myfirstaidkit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.Treatment;

import java.util.List;

class TreatmentsListAdapter<T> extends ArrayAdapter<T> {

    Context context;
    List<T> data;
    int style;
    private static LayoutInflater inflater = null;

    public TreatmentsListAdapter(Context context, int layoutId, List<T> data) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(style, null);
        TextView header = vi.findViewById(R.id.list_item_header);
        TextView text = vi.findViewById(R.id.list_item_text);
        header.setText(((Treatment) data.get(position)).getName());
        text.setText(((Treatment) data.get(position)).getIdUser());
        return vi;
    }
}