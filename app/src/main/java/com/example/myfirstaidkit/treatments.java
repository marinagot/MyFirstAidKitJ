package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
    // ArrayAdapter<Treatment> adapter;
    TreatmentsListAdapter<Treatment> adapter;
    ArrayAdapter<Medicine> adapterMed;


    List<Medicine> list_med = new ArrayList<>();
    List<MedTretRel> listrel = new ArrayList<>();


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

        /*FloatingActionButton fab = view.findViewById(R.id.newTreatment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_treatments_to_treatment_edit);
                *//*getFragmentManager().beginTransaction().replace(R.id.content, new treatment_edit()).commit();
                getActivity().setTitle("New treatment");*//*
            }
        });*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Muestra la barra inferior de navegación
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        viewCA = inflater.inflate(R.layout.fragment_treatments, container, false);
        us = DataBaseOperations.get_Instance(getContext());

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.treatment_types, R.layout.spinner_item);

        final Spinner spinner = viewCA.findViewById(R.id.spinner);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               new ApiCallThread<List<Treatment>>(new AsyncResponse<List<Treatment>>(){
                   @Override
                   public List<Treatment> apiCall(Object... params) {
                       switch (((String) params[2])) {
                           case "Activos":
                               return getActiveTreatments(us.getTreatment_userId((String) params[1]));
                           case "Terminados":
                               return getEndedTreatments(us.getTreatment_userId((String) params[1]));
                           default:
                               List<Treatment> aux = us.getTreatment_userId((String) params[1]);
                               Collections.reverse(aux);
                               return aux;
                       }
                   }

                   @Override
                   public void processFinish(View v, List<Treatment> result){
                       treatmentList = result;
                       ExpandableListView list = viewCA.findViewById(R.id.list_user_treatments);
                       adapter = new TreatmentsListAdapter<>(getContext(), R.layout.treatment_list_group, R.layout.treatment_list_child, treatmentList, getTreatmentsMap(treatmentList));
                       // adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, treatmentList);
                       list.setAdapter(adapter);
                   }
               }).execute(view, us.getIdLogged(), spinner.getSelectedItem());
           }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }
       });

//        new ApiCallThread<List<Treatment>>(new AsyncResponse<List<Treatment>>(){
//            @Override
//            public List<Treatment> apiCall(Object... params) {
//                return getActiveTreatments(us.getTreatment_userId((String) params[1]));
//            }
//
//            @Override
//            public void processFinish(View v, List<Treatment> result){
//                treatmentList = result;
//                adapter = new TreatmentsListAdapter<>(getContext(), R.layout.treatment_list_group, treatmentList, getTreatmentsMap(treatmentList));
//                list.setAdapter(adapter);
//
//            }
//        }).execute(viewCA, us.getIdLogged());


        ExpandableListView list = viewCA.findViewById(R.id.list_user_treatments);
        list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                final Treatment t = treatmentList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflaterl = getActivity().getLayoutInflater();
                alert = inflaterl.inflate(R.layout.popup_treatment_medicine_list, null);
                ListView lv_med = alert.findViewById(R.id.list_treat_medicines);
                listrel = us.getRelations_treatmentId(t.getId());
                for (MedTretRel mtr : listrel ) {
                    list_med.add(us.getMedicine_medicineId(mtr.getIdMedicine()));
                }

                adapterMed = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list_med);
                lv_med.setAdapter(adapterMed);

                lv_med.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Medicine m = list_med.get(position);
                        MedTretRel relation = listrel.get(position);
                        AlertDialog.Builder builderM = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflaterM = getActivity().getLayoutInflater();
                        alert2 = inflaterM.inflate(R.layout.medicine_treatment_popup, null);

                        TextView title = alert2.findViewById(R.id.medicineTitlePopup);
                        TextView dateIn = alert2.findViewById(R.id.medicineStartDatePopup);
                        TextView dateEnd = alert2.findViewById(R.id.medicineEndDatePopup);
                        TextView frequency = alert2.findViewById(R.id.medicineFrequencyPopup);

                        title.setText(m.getName());
                        dateIn.setText(new SimpleDateFormat("dd/MM/yyyy").format(relation.getInitialDate()));
                        dateEnd.setText(new SimpleDateFormat("dd/MM/yyyy").format(relation.getFinalDate()));
                        // TODO poner aqui las horas del dia
                        frequency.setText("No se que poner");

                        builderM.setView(alert2);
                        builderM.setTitle(t.getName())
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                    });
                builder.setView(alert);
                builder.setTitle(t.getName())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                list_med = new ArrayList<>();
                                dialog.cancel();
                            }
                        }).show();
            }
        });

        return viewCA;
    }

    public HashMap<Treatment, List<Medicine>> getTreatmentsMap(List<Treatment> list) {
        HashMap<Treatment, List<Medicine>> map = new HashMap<>();

        for (Treatment treatment : list) {
            List<Medicine> mapMedicineList = new ArrayList<>();
            listrel = us.getRelations_treatmentId(treatment.getId());
            for (MedTretRel mtr : listrel) {
                mapMedicineList.add(us.getMedicine_medicineId(mtr.getIdMedicine()));
            }
            map.put(treatment, mapMedicineList);
        }
        return map;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Navigation.findNavController(viewCA).navigate(R.id.action_treatments_to_treatment_edit);
                return true;
        }
        return false;
    }

    public List<Treatment> getActiveTreatments(List<Treatment> input) {
        List<Treatment> resp = new ArrayList<>();
        if (!input.isEmpty()) {
            for (Treatment t : input) {
                if (getEndDate(us.getRelations_treatmentId(t.getId())).getTime() >= new Date().getTime()) {
                    resp.add(t);
                }
            }
            Collections.reverse(resp);
        }
        return resp;
    }

    public List<Treatment> getEndedTreatments(List<Treatment> input) {
        List<Treatment> resp = new ArrayList<>();
        if (!input.isEmpty()) {
            for (Treatment t : input) {
                if (getEndDate(us.getRelations_treatmentId(t.getId())).getTime() < new Date().getTime()) {
                    resp.add(t);
                }
            }
            Collections.reverse(resp);
        }
        return resp;
    }

    public Date getEndDate(List<MedTretRel> relations) {
        Date lastDate = new Date(relations.get(0).getFinalDate());
        for (MedTretRel r : relations) {
            if (lastDate.getTime() < r.getFinalDate())
                lastDate = new Date(r.getFinalDate());
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
