package com.example.myfirstaidkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.MedTretRel;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.Treatment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link treatments.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class treatments extends Fragment {
    SharedPreferences prefs;

    DataBaseOperations us;
    View viewCA;

    List<Treatment> treatmentList = new ArrayList<>();
    TreatmentsListAdapter<Treatment> adapter;
    List<MedTretRel> listrel = new ArrayList<>();

    public treatments() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Muestra la barra inferior de navegación
        Objects.requireNonNull(getActivity()).findViewById(R.id.nav_view).setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        viewCA = inflater.inflate(R.layout.fragment_treatments, container, false);
        us = DataBaseOperations.get_Instance(Objects.requireNonNull(getContext()));

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
                       adapter = new TreatmentsListAdapter<>(Objects.requireNonNull(getContext()), R.layout.treatment_list_group, R.layout.treatment_list_child, treatmentList, getTreatmentsMap(treatmentList));
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
        if (item.getItemId() == R.id.action_add) {
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            prefs = Objects.requireNonNull(getContext()).getSharedPreferences("UserLogged",Context.MODE_PRIVATE);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
