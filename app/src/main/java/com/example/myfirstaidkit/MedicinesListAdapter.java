package com.example.myfirstaidkit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.Medicine;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
        ImageView doseWarning = vi.findViewById(R.id.dose_warning);
        TextView expiryDate = vi.findViewById(R.id.list_item_expiry_date);
        ImageView expirationWarning = vi.findViewById(R.id.expiration_warning);
        LinearLayout rowContainer = vi.findViewById(R.id.row_container);
        header.setText(((Medicine) data.get(position)).getName());
        text.setText(((Medicine) data.get(position)).getType());
        String dateText = new SimpleDateFormat("dd MMM yyyy").format(new Date(((Medicine) data.get(position)).getExpirationDate()));

        if (((Medicine)data.get(position)).getDoseNumber() <= 5) {
            doseWarning.setVisibility(View.VISIBLE);
        }
        else {
            doseWarning.setVisibility(View.GONE);
        }

        Date dateAfter = new Date(System.currentTimeMillis() + 7 * 24 * 3600 * 1000 );
        if (((Medicine) data.get(position)).getExpirationDate() <= dateAfter.getTime()) {
            expirationWarning.setVisibility(View.VISIBLE);
        }
        else {
            expirationWarning.setVisibility(View.GONE);
        }

        if (((Medicine)data.get(position)).getDoseNumber() < 1 || ((Medicine) data.get(position)).getExpirationDate() <= System.currentTimeMillis() - 24 * 3600 * 1000) {
            rowContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.list_item_disabled));
            if (((Medicine)data.get(position)).getDoseNumber() < 1 ) {
                dose.setTextColor(ContextCompat.getColor(context, R.color.alert));
                dose.setText("AGOTADO");
                doseWarning.setVisibility(View.GONE);
                expirationWarning.setVisibility(View.GONE);
            } else {
                dose.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText));
                dose.setText("Dosis restantes: " + ((Medicine) data.get(position)).getDoseNumber().toString());
            }


            if (((Medicine) data.get(position)).getExpirationDate() <= System.currentTimeMillis()) {
                expiryDate.setTextColor(ContextCompat.getColor(context, R.color.alert));
                expiryDate.setText("CADUCADO");
                doseWarning.setVisibility(View.GONE);
                expirationWarning.setVisibility(View.GONE);
            } else {
                expiryDate.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText));
                expiryDate.setText("Caducidad: " + dateText);
            }
        } else {
            rowContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.list_item));
            expiryDate.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText));
            dose.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText));
            expiryDate.setText("Caducidad: " + dateText);
            dose.setText("Dosis restantes: " + ((Medicine) data.get(position)).getDoseNumber().toString());
        }

        ImageButton editImageView = vi.findViewById(R.id.list_item_edit);
        editImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Gson gson = new Gson();
                String medicine = null;
                try {
                    medicine = new JSONObject(gson.toJson(data.get(position))).toString();
                } catch (Exception e) {}

                Bundle bundle = new Bundle();
                bundle.putString("medicine", medicine);

                Navigation.findNavController(v).navigate(R.id.action_first_aid_kit_to_medicine_edit, bundle);

                /*first_aid_kitDirections.ActionFirstAidKitToMedicineEdit action =
                        first_aid_kitDirections.actionFirstAidKitToMedicineEdit(medicine);
                Navigation.findNavController(v).navigate(action);*/

                /*add(data.get(position));
                notifyDataSetChanged();*/
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