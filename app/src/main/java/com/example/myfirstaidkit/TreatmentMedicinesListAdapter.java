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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.MedTretRel;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.Treatment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class TreatmentMedicinesListAdapter<T> extends ArrayAdapter<T> {

    Context context;
    private List<T> listItems;
    private List<MedTretRel> relations;
    private List<MedTretRel> removedRelations;
    private List<MedTretRel> editedRelations;
    private List<Medicine> medicineList;
    private int style;
    private static LayoutInflater inflater = null;

    private Date finalDate;
    private Spinner listMedicines;
    private EditText period;

    DataBaseOperations us;

    public TreatmentMedicinesListAdapter(Context context, int layoutId, List<List<T>> data) {
        // TODO Auto-generated constructor stub
        super(context, 0 , data.get(0));
        this.context = context;
        this.listItems = data.get(0);
        this.relations = ((List<MedTretRel>) data.get(1));
        this.removedRelations = ((List<MedTretRel>) data.get(2));
        this.editedRelations = ((List<MedTretRel>) data.get(3));
        this.medicineList = ((List<Medicine>) data.get(4));
        this.style = layoutId;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listItems.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return listItems.get(position);
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
        header.setText(((Medicine) listItems.get(position)).getName());

        ImageButton editImageView = vi.findViewById(R.id.treatment_list_item_edit);
        editImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final MedTretRel oldRelation = relations.get(position);

                View alert = LayoutInflater.from(getContext()).inflate(R.layout.popup_treatment_edit_add_new_medicine, null);

                listMedicines = alert.findViewById(R.id.list_medicines);

                ArrayAdapter<Medicine> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, medicineList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listMedicines.setAdapter(dataAdapter);

                listMedicines.setSelection(position);

                period = alert.findViewById(R.id.txt_edit_medicine_num);
                period.setText(oldRelation.getFrequency().toString());

                final TextView finalDateField = alert.findViewById(R.id.final_date);
                finalDateField.setText(new SimpleDateFormat("dd/MM/yyyy").format(oldRelation.getFinalDate()));

                finalDate = new Date(oldRelation.getFinalDate());

                // Se construye la interfaz

                Button btnFinDate = alert.findViewById(R.id.btn_date_cale);
                btnFinDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int day) {
                                        try {
                                            month += 1;
                                            finalDate = new SimpleDateFormat("dd/MM/yyyy").parse(day + "/" + month + "/" + year);
                                            finalDateField.setText(day + "/" + month + "/" + year);
                                        } catch (Exception e){ finalDate = null; }
                                    }
                                }, year, month, dayOfMonth);
                        datePickerDialog.show();
                    }
                });

                new AlertDialog.Builder(getContext()).setView(alert)
                        .setTitle("Editar medicina")
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Logica de guardado en la lista general
                                // String a = period.getText().toString();
                                dialog.dismiss();

                                // this line adds the data of your Spinner and puts in your array
                                ((List<Medicine>) listItems).add((Medicine) listMedicines.getSelectedItem());

                                MedTretRel auxRel = new MedTretRel();
                                auxRel.setFrequency(Integer.parseInt(period.getText().toString()));
                                auxRel.setInitialDate(oldRelation.getInitialDate());
                                auxRel.setFinalDate(finalDate.getTime());
                                auxRel.setIdMedicine(((Medicine) listMedicines.getSelectedItem()).getId());
                                auxRel.setisEdited(true);

                                relations.set(position, auxRel);

                                // next thing you have to do is check if your adapter has changed
                                notifyDataSetChanged();
                            }
                        }).show();
            }
        });

        ImageButton deleteImageView = vi.findViewById(R.id.treatment_list_item_delete);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                remove(listItems.get(position));
                removedRelations.add(relations.get(position));
                relations.remove(position);
                notifyDataSetChanged();
            }
        });

        return vi;
    }
}