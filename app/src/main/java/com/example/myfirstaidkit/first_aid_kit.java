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
import android.widget.ListView;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.Medicine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link first_aid_kit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class first_aid_kit extends Fragment {

    SharedPreferences prefs;

    DataBaseOperations us;
    View viewCA;

    List<Medicine> medicineList = new ArrayList<>();
    MedicinesListAdapter<Medicine> adapter;

    public first_aid_kit() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Muestra la barra inferior de navegación
        Objects.requireNonNull(getActivity()).findViewById(R.id.nav_view).setVisibility(View.VISIBLE);

        /*FloatingActionButton fab = view.findViewById(R.id.newMedicine);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_first_aid_kit_to_medicine_edit);
            }
        });*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewCA = inflater.inflate(R.layout.fragment_first_aid_kit, container, false);
        us = DataBaseOperations.get_Instance(Objects.requireNonNull(getContext()));

        if (us.userIsLogged()) {
            new ApiCallThread<List<Medicine>>(new AsyncResponse<List<Medicine>>(){
                @Override
                public List<Medicine> apiCall(Object... params) {
                    return us.getMedicine_userId((String) params[1]);
                }

                @Override
                public void processFinish(View v, List<Medicine> result){
                    if (result != null) {

                        medicineList = result;
                        ListView list = viewCA.findViewById(R.id.list_user_medicines);
                        adapter = new MedicinesListAdapter<>(getContext(), R.layout.medicine_list_item, medicineList);
                        list.setAdapter(adapter);
                    } /*else {
                        ListView list = viewCA.findViewById(R.id.list_user_medicines);
                        List error = new ArrayList<>();
                        error.add("Parece que no hay conexión.");
                        ErrorAdapter adapter = new ErrorAdapter<>(getContext(), R.layout.error_sync, error);
                        list.setAdapter(adapter);
                    }*/

                    /*list.setOnItemClickListener(new ListView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> adapterList, View v, final int pos, long id) {
                            int i = 0;
                        }
                    });*/
                }
            }).execute(viewCA, us.getIdLogged());
        }
        return viewCA;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Navigation.findNavController(viewCA).navigate(R.id.action_first_aid_kit_to_medicine_edit);
            return true;
        }
        return false;
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
        void onFragmentInteraction(Uri uri);
    }
}
