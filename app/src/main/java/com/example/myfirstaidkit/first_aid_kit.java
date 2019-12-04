package com.example.myfirstaidkit;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link first_aid_kit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link first_aid_kit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class first_aid_kit extends Fragment {
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
    View viewCA;

    List<Medicine> medicineList = new ArrayList<>();
    //ArrayAdapter<Medicine> adapter;
    MedicinesListAdapter<Medicine> adapterNew;

    public first_aid_kit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment first_aid_kit.
     */
    // TODO: Rename and change types and number of parameters
    public static first_aid_kit newInstance(String param1, String param2) {
        first_aid_kit fragment = new first_aid_kit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Muestra la barra inferior de navegación
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewCA = inflater.inflate(R.layout.fragment_first_aid_kit, container, false);
        us = DataBaseOperations.get_Instance(getContext());

        if (us.userIsLogged()) {
            new ApiCallThread<List<Medicine>>(new AsyncResponse<List<Medicine>>(){
                @Override
                public List<Medicine> apiCall(Object... params) {
                    return us.getMedicine_userId((String) params[1]);
                }

                @Override
                public void processFinish(View v, List<Medicine> result){
                    if (result == null) {
                        /*ListView list = viewCA.findViewById(R.id.list_user_medicines);
                        List error = new ArrayList<>();
                        error.add("Parece que no hay conexión.");
                        ErrorAdapter adapterNew = new ErrorAdapter<>(getContext(), R.layout.error_sync, error);
                        list.setAdapter(adapterNew);*/
                    }
                    else {
                        medicineList = result;
                        ListView list = viewCA.findViewById(R.id.list_user_medicines);
                        adapterNew = new MedicinesListAdapter<>(getContext(), R.layout.row, medicineList);
                        list.setAdapter(adapterNew);
                    }

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
        switch (item.getItemId()) {
            case R.id.action_add:
                Navigation.findNavController(viewCA).navigate(R.id.action_first_aid_kit_to_medicine_edit);
                return true;
        }
        return false;
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

    public void refreshList() {
        adapterNew.refresh();
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
