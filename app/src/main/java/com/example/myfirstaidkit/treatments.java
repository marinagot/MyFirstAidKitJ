package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.MedTretRel;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.Treatment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link treatments.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link treatments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class treatments extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    DataBaseOperations us;
    View viewCA, alert, alert2;

    List<Treatment> treatmentList = new ArrayList<>();
    ArrayAdapter<Treatment> adapter;
    ArrayAdapter<Medicine> adapterMed;


    public treatments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment treatments.
     */
    // TODO: Rename and change types and number of parameters
    public static treatments newInstance(String param1, String param2) {
        treatments fragment = new treatments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.newTreatment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.content, new treatment_edit()).commit();
                //Navigation.findNavController(view).navigate(R.id.action_treatments_to_treatment_edit);
                getActivity().setTitle("New treatment");
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewCA = inflater.inflate(R.layout.fragment_treatments, container, false);
        us = DataBaseOperations.get_Instance(getContext());

        TabLayout tab = viewCA.findViewById(R.id.tabTreatments);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (us.userIsLogged(prefs))
                    if (tab.getText().equals("Active")) {
                        treatmentList = getActiveTreatments(us.getTreatment_userId(us.getUser_Username(us.getUserLogged(prefs)).getId()));
                    }
                    else if (tab.getText().equals("Ended")){
                        treatmentList = getEndedTreatments(us.getTreatment_userId(us.getUser_Username(us.getUserLogged(prefs)).getId()));
                    }
                    else {
                        treatmentList = us.getTreatment_userId(us.getUser_Username(us.getUserLogged(prefs)).getId());
                        Collections.reverse(treatmentList);
                    }

                ListView list = viewCA.findViewById(R.id.list_user_treatments);
                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, treatmentList);
                list.setAdapter(adapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        if (us.userIsLogged(prefs))
            treatmentList = getActiveTreatments(us.getTreatment_userId(us.getUser_Username(us.getUserLogged(prefs)).getId()));

        ListView list = viewCA.findViewById(R.id.list_user_treatments);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                //Cambiar alert_treatments por el layout bueno

                Treatment t = treatmentList.get(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflaterl = getActivity().getLayoutInflater();
                alert = inflaterl.inflate(R.layout.fragment_treatment_medicine_list_pop_up, null);
                final ListView lv_med = alert.findViewById(R.id.list_treat_medicines);
                final List<Medicine> list_med = new ArrayList<>();
                List<MedTretRel> listrel = new ArrayList<>();
                listrel = us.getRelations_treatmentId(t.getId());
                Iterator<MedTretRel> it = listrel.iterator();
                while (it.hasNext()) {
                    MedTretRel mtr = it.next();
                    final Date initDate = mtr.getInitialDate();
                    final Date FinalDate = mtr.getInitialDate();
                    int freq = mtr.getFrequency();
                    long intmed = mtr.getIdMedicine();

                    Medicine m = us.getMedicine_medicineId(intmed);
                    list_med.add(m);
                }
                adapterMed = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list_med);
                lv_med.setAdapter(adapterMed);

                /*lv_med.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Medicine m = list_med.get(position);
                        final AlertDialog.Builder builderm = new AlertDialog.Builder(getActivity());
                        final LayoutInflater inflaterm = getActivity().getLayoutInflater();
                        alert2 = inflaterm.inflate(R.layout.fragment_treatment_medicine_info, null);
                        TextView dateIn = alert2.findViewById(R.id.lbl_edit_medicine_type8);
                        TextView dateEnd = alert2.findViewById(R.id.lbl_edit_medicine_type8);
                        TextView frecuency = alert2.findViewById(R.id.lbl_edit_medicine_type8);
                        dateIn.setText(new SimpleDateFormat("dd/MM/yyyy").parse(initDate));
                        frecuency.setText(freq);
                        builderm.setView(alert2);
                        builderm.setTitle(m.getName())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                    });*/
                builder.setView(alert);
                builder.setTitle(t.getName())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, treatmentList);
        list.setAdapter(adapter);

        return viewCA;
    }

    public List<Treatment> getActiveTreatments(List<Treatment> input) {
        List<Treatment> resp = new ArrayList<>();
        for (Treatment t : input) {
            if (getEndDate(us.getRelations_treatmentId(t.getId())).getTime() >= new Date().getTime()) {
                resp.add(t);
            }
        }
        Collections.reverse(resp);
        return resp;
    }

    public List<Treatment> getEndedTreatments(List<Treatment> input) {
        List<Treatment> resp = new ArrayList<>();
        for (Treatment t : input) {
            if (getEndDate(us.getRelations_treatmentId(t.getId())).getTime() < new Date().getTime()) {
                resp.add(t);
            }
        }
        Collections.reverse(resp);
        return resp;
    }

    public Date getEndDate(List<MedTretRel> relations) {
        Date lastDate = relations.get(0).getFinalDate();
        for (MedTretRel r : relations) {
            if (lastDate.getTime() < r.getFinalDate().getTime())
                lastDate = r.getFinalDate();
        }
        return lastDate;
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
