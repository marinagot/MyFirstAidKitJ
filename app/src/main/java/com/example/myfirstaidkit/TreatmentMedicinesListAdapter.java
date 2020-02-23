package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.MedTretRel;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.Treatment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class TreatmentMedicinesListAdapter<T> extends ArrayAdapter<T> {

    Context context;
    private List<T> treatmentMedicines;
    private List<MedTretRel> relations;
    private List<MedTretRel> removedRelations;
    private List<MedTretRel> editedRelations;
    private List<Medicine> userMedicines;
    private Treatment treatment;
    private boolean isTreatmentEdit;
    private int style;
    private static LayoutInflater inflater = null;

    private Date finalDate;
    private Spinner spinnerMedicines;
    private EditText period;

    DataBaseOperations us;

    public TreatmentMedicinesListAdapter(Context context, int layoutId, List<Object> data) {
        // TODO Auto-generated constructor stub
        super(context, 0 , (List<T>)data.get(0));
        this.context = context;
        this.treatmentMedicines = (List<T>) data.get(0);
        this.relations = (List<MedTretRel>) data.get(1);
        this.removedRelations = (List<MedTretRel>) data.get(2);
        this.editedRelations = (List<MedTretRel>) data.get(3);
        this.userMedicines = (List<Medicine>) data.get(4);
        this.treatment = (Treatment) data.get(5);
        this.isTreatmentEdit = (boolean) data.get(6);
        this.style = layoutId;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return treatmentMedicines.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return treatmentMedicines.get(position);
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
        if (vi == null) {
            vi = inflater.inflate(style, null);
        }

        TextView header = vi.findViewById(R.id.treatment_list_item_header);
        header.setText(((Medicine) treatmentMedicines.get(position)).getName());

        ImageButton editImageView = vi.findViewById(R.id.treatment_list_item_edit);
        editImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Gson gson = new Gson();

                bundle.putBoolean("isMedicineEdit", true);
                bundle.putBoolean("isTreatmentEdit", isTreatmentEdit);
                bundle.putInt("position", position);
                try {
                    bundle.putString("treatment", new JSONObject(gson.toJson(treatment)).toString());
                    bundle.putString("relations", new JSONArray(gson.toJson(relations)).toString());
                    bundle.putString("medicines", new JSONArray(gson.toJson(treatmentMedicines)).toString());
                    bundle.putString("userMedicines", new JSONArray(gson.toJson(userMedicines)).toString());
                } catch (Exception e) {
                    int i = 0;
                }

                Navigation.findNavController(v).navigate(R.id.action_treatment_edit_to_treatment_edit_add_medicine, bundle);
            }
        });

        ImageButton deleteImageView = vi.findViewById(R.id.treatment_list_item_delete);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                remove(treatmentMedicines.get(position));
                removedRelations.add(relations.get(position));
                relations.remove(position);
                notifyDataSetChanged();
            }
        });

        return vi;
    }
}