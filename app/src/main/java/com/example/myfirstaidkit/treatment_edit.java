package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.MedTretRel;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.Treatment;
import com.example.myfirstaidkit.data.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.navigation.Navigation;

import androidx.navigation.Navigation;


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
    MedTretRel relation = new MedTretRel();
    List<MedTretRel> relations = new ArrayList<>();
    View viewCA, alert;

    List<Medicine> medicineList;
    Date finalDate;

    Spinner listMedicines;
    EditText period;
    CalendarView endDate;

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
    }

    ArrayList<Medicine> listItems = new ArrayList<>();
    ArrayAdapter<Medicine> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewCA = inflater.inflate(R.layout.fragment_treatment_edit, container, false);
        alert = inflater.inflate(R.layout.alert_treatments, null, false);
        us = DataBaseOperations.get_Instance(getContext());

        medicineList = new ArrayList<>();
        //Rellenar con campos de base de datos del kit del usuario
        User user = us.getUser_Username(prefs.getString("username",""));
        if (user != null)
            medicineList = us.getMedicine_userId(user.getId());

        ListView list = viewCA.findViewById(R.id.list);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);

        Button btnAdd = viewCA.findViewById(R.id.btn_treatment_edit_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert = LayoutInflater.from(getContext()).inflate(R.layout.alert_treatments, null);

                CalendarView calendar = alert.findViewById(R.id.endingDate);
                calendar.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        try{
                            month += 1;
                            finalDate = new SimpleDateFormat("dd/MM/yyyy").parse(dayOfMonth + "/" + month + "/" + year);
                        }
                        catch(Exception e){ finalDate = null; }

                    }
                });

                listMedicines = alert.findViewById(R.id.list_medicines);
                period = alert.findViewById(R.id.txt_edit_medicine_num);
                endDate = alert.findViewById(R.id.medicine_edit_expire_date);

                ArrayAdapter<Medicine> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, medicineList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                listMedicines.setAdapter(dataAdapter);


                new AlertDialog.Builder(getContext()).setView(alert)
                .setTitle("Insert new medicine")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Logica de guardado en la lista general
                        String a = period.getText().toString();
                        dialog.dismiss();

                        // this line adds the data of your Spinner and puts in your array
                        listItems.add((Medicine) listMedicines.getSelectedItem());

                        MedTretRel auxRel = new MedTretRel();
                        auxRel.setFrequency(Integer.parseInt(period.getText().toString()));
                        auxRel.setInitialDate(new Date());
                        auxRel.setFinalDate(finalDate);
                        auxRel.setIdMedicine(((Medicine) listMedicines.getSelectedItem()).getId());
                        relations.add(auxRel);

                        // next thing you have to do is check if your adapter has changed
                        adapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });

        Button btnDone = viewCA.findViewById(R.id.btn_treatment_edit_done);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                treatment.setName(((EditText) viewCA.findViewById(R.id.txt_treatment_name)).getText().toString());
                String user = prefs.getString("username", null);

                if (user != null) {
                    treatment.setIdUser(us.getUser_Username(user).getId());

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
                        treatment.setId(us.insertTreatment(treatment));
                        for (MedTretRel rel : relations) {
                            rel.setIdTreatment(treatment.getId());
                            us.insertRelation(rel);
                        }
                        getFragmentManager().beginTransaction().replace(R.id.content, new treatments()).commit();
                        getActivity().setTitle("Treatments");
                    }
                }
                else {

//                         No esta logueado
                    getFragmentManager().beginTransaction().replace(R.id.content, new login()).commit();
                    getActivity().setTitle("Login");
                }
            }
        });

        return viewCA;


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
