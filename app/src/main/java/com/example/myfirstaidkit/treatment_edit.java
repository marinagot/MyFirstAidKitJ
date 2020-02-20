package com.example.myfirstaidkit;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static com.example.myfirstaidkit.helpers.Utils.removeSchedule;
import static com.example.myfirstaidkit.helpers.Utils.scheduleDose;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link treatment_edit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link treatment_edit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class treatment_edit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Preferencias de la aplicación
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    DataBaseOperations us;
    Treatment treatment = new Treatment();
    List<MedTretRel> relations = new ArrayList<>();
    View viewCA, alert;

    List<Medicine> medicineList;
    Date finalDate;

    Spinner listMedicines;
    EditText period;

    ArrayList<Medicine> listItems = new ArrayList<>();
    ArrayAdapter<Medicine> adapter;

    List<MedTretRel> removedRelations = new ArrayList<>();
    List<MedTretRel> editedRelations = new ArrayList<>();

    boolean isEdit = false;

    final static int RQS_1 = 1;

    public treatment_edit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment treatment_edit.
     */
    // TODO: Rename and change types and number of parameters
    public static treatment_edit newInstance(String param1, String param2) {
        treatment_edit fragment = new treatment_edit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            isEdit = true;

            Gson gson = new Gson();

            treatment = gson.fromJson(getArguments().getString("treatment"), new TypeToken<Treatment>(){}.getType());
            listItems = gson.fromJson(getArguments().getString("medicines"), new TypeToken<List<Medicine>>(){}.getType());
            relations = gson.fromJson(getArguments().getString("relations"), new TypeToken<List<MedTretRel>>(){}.getType());

            ((EditText) viewCA.findViewById(R.id.txt_treatment_name)).setText(treatment.getName());
        }



//        Button btnOk = view.findViewById(R.id.btn_treatment_edit_done);
//
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getFragmentManager().beginTransaction().replace(R.id.content, new treatments()).commit();
//                getActivity().setTitle("Treatments");
//            }
//        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
        try {
            getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
        } catch (Exception e) {}
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewCA = inflater.inflate(R.layout.fragment_treatment_edit, container, false);
        alert = inflater.inflate(R.layout.popup_treatment_edit_add_new_medicine, null, false);
        us = DataBaseOperations.get_Instance(getContext());

        medicineList = new ArrayList<>();
        //Rellenar con campos de base de datos del kit del usuario
        new ApiCallThread<List<Medicine>>(new AsyncResponse<List<Medicine>>(){
            @Override
            public List<Medicine> apiCall(Object... params) {
                return us.getMedicine_userId(((String) params[1]));
            }

            @Override
            public void processFinish(View v, List<Medicine> result){
                medicineList = result;
                ListView list = viewCA.findViewById(R.id.list);
                List adapterData = new ArrayList(){
                    {
                        add(listItems);
                        add(relations);
                        add(removedRelations);
                        add(editedRelations);
                        add(medicineList);
                    }
                };
                adapter = new TreatmentMedicinesListAdapter<>(getContext(), R.layout.treatment_edit_list_item, adapterData);
                list.setAdapter(adapter);

                Button btnAdd = viewCA.findViewById(R.id.btn_treatment_edit_add);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert = LayoutInflater.from(getContext()).inflate(R.layout.popup_treatment_edit_add_new_medicine, null);

                        Button btnFinDate = alert.findViewById(R.id.btn_date_cale);
                        final TextView finalDateField = alert.findViewById(R.id.final_date);
                        btnFinDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view){
                                Calendar calendar = Calendar.getInstance();
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH);
                                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                                try {
                                                    month += 1;
                                                    finalDate = new SimpleDateFormat("dd/MM/yyyy").parse(day + "/" + month + "/" + year);
                                                    finalDateField.setText(day + "/" + month + "/" + year);
                                                } catch (Exception e){ finalDate = null; }
                                            }
                                        },year,month,dayOfMonth);
                                datePickerDialog.show();
                            }
                        });

                        listMedicines = alert.findViewById(R.id.list_medicines);
                        period = alert.findViewById(R.id.txt_edit_medicine_num);

                        ArrayAdapter<Medicine> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, medicineList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        listMedicines.setAdapter(dataAdapter);


                        new AlertDialog.Builder(getContext()).setView(alert)
                                .setTitle("Insertar nueva medicina")
                                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //Logica de guardado en la lista general
                                        dialog.dismiss();

                                        // this line adds the data of your Spinner and puts in your array
                                        listItems.add((Medicine) listMedicines.getSelectedItem());

                                        MedTretRel auxRel = new MedTretRel();
                                        auxRel.setFrequency(Integer.parseInt(period.getText().toString()));
                                        auxRel.setInitialDate(new Date().getTime());
                                        auxRel.setFinalDate(finalDate.getTime());
                                        auxRel.setIdMedicine(((Medicine) listMedicines.getSelectedItem()).getId());
                                        if (isEdit) {
                                            auxRel.setIdTreatment(treatment.getId());
                                            auxRel.setIsNew(true);
                                        }
                                        relations.add(auxRel);

                                        // next thing you have to do is check if your adapter has changed
                                        adapter.notifyDataSetChanged();
                                    }
                                }).show();
                    }
                });
            }
        }).execute(viewCA, us.getIdLogged());

        Button btnDone = viewCA.findViewById(R.id.btn_treatment_edit_done);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                treatment.setName(((EditText) viewCA.findViewById(R.id.txt_treatment_name)).getText().toString());

                treatment.setIdUser(us.getIdLogged());

                if (treatment.getName().equals("") || relations.isEmpty()) {
                    //Display Message
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("ALERT!");
                    alertDialog.setMessage("All fields must be filled correctly");
                    alertDialog.show();
                    int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                    TextView tv = alertDialog.findViewById(textViewId);
                    tv.setTextColor(Color.RED);
                    TextView textViewMessage = alertDialog.findViewById(android.R.id.message);
                    textViewMessage.setTextColor(Color.RED);
                }
                else {
                    new ApiCallThread<Void>(new AsyncResponse<Void>() {
                        @Override
                        public Void apiCall(Object... params) {
                            Treatment treatment = (Treatment) params[1];
                            if (isEdit) {
                                for (MedTretRel rel : relations) {
                                    if (rel.isNew()) {
                                        if (us.insertRelation(rel) != null) {
                                            PersistableBundle bundle = new PersistableBundle();

                                            bundle.putString("rel_id", rel.getId());
                                            bundle.putInt("rel_frequency", rel.getFrequency());
                                            bundle.putString("rel_med_id", rel.getIdMedicine());
                                            bundle.putString("rel_treat_id", rel.getIdTreatment());
                                            bundle.putLong("rel_end_date", rel.getFinalDate());

                                            scheduleDose(getContext(), bundle);
                                        }
                                    }
                                    if (rel.isEdited()) {
                                        if (us.updateRelation(rel) != null) {
                                            PersistableBundle bundle = new PersistableBundle();

                                            bundle.putString("rel_id", rel.getId());
                                            bundle.putInt("rel_frequency", rel.getFrequency());
                                            bundle.putString("rel_med_id", rel.getIdMedicine());
                                            bundle.putString("rel_treat_id", rel.getIdTreatment());
                                            bundle.putLong("rel_end_date", rel.getFinalDate());

                                            scheduleDose(getContext(), bundle);
                                        }
                                    }
                                }
                                for (MedTretRel rel : removedRelations) {
                                    if (us.deleteRelation(rel) != null) {
                                        removeSchedule(getContext(), rel.getId().hashCode());
                                    }

                                }
                                us.updateTreatment(treatment,  null);
                            }
                            else {
                                treatment.setId(us.insertTreatment(treatment));
                                for (MedTretRel rel : relations) {
                                    rel.setIdTreatment(treatment.getId());
                                    if (us.insertRelation(rel) != null) {
                                        PersistableBundle bundle = new PersistableBundle();

                                        bundle.putString("rel_id", rel.getId());
                                        bundle.putInt("rel_frequency", rel.getFrequency());
                                        bundle.putString("rel_med_id", rel.getIdMedicine());
                                        bundle.putString("rel_treat_id", rel.getIdTreatment());
                                        bundle.putLong("rel_end_date", rel.getFinalDate());

                                        scheduleDose(getContext(), bundle);
                                    }
                                }
                            }
                            return null;
                        }

                        @Override
                        public void processFinish(View v, Void result) {
                            Navigation.findNavController(v).navigate(R.id.action_treatment_edit_to_treatments);
                        }
                    }).execute(v, treatment);
                }
            }
        });

        // Calendar cal = Calendar.getInstance();
        // cal.set(Calendar.HOUR_OF_DAY, 17);
        // cal.set(Calendar.MINUTE, 20);
        // cal.set(Calendar.SECOND, 0);
        // cal.set(Calendar.MILLISECOND, 0);
        // setAlarm(cal);

        return viewCA;


    }

    public void setAlarm(Calendar targetCal) {

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;

            prefs = getContext().getSharedPreferences("UserLogged",Context.MODE_PRIVATE);
            edit = prefs.edit();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
