package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.MedTretRel;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.TakeHours;
import com.example.myfirstaidkit.data.Treatment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.myfirstaidkit.helpers.Utils.getNextDose;
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
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Preferencias de la aplicación
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    DataBaseOperations dbo;
    Treatment treatment = new Treatment();
    List<MedTretRel> relations = new ArrayList<>();
    View viewCA;

    List<Medicine> userMedicines;

    List<Medicine> treatmentMedicines = new ArrayList<>();
//    List<TakeHours> medicineHours;
    ArrayAdapter<Medicine> adapter;

    List<MedTretRel> removedRelations = new ArrayList<>();
    List<MedTretRel> editedRelations = new ArrayList<>();

    boolean isTreatmentEdit = false;

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
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

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
        dbo = DataBaseOperations.get_Instance(getContext());

        if (getArguments() != null) {
            Gson gson = new Gson();

            if (getArguments().getBoolean("isTreatmentEdit")) {
                isTreatmentEdit = true;
            }
            treatment = gson.fromJson(getArguments().getString("treatment"), new TypeToken<Treatment>(){}.getType());
            treatmentMedicines = gson.fromJson(getArguments().getString("medicines"), new TypeToken<List<Medicine>>(){}.getType());
            relations = gson.fromJson(getArguments().getString("relations"), new TypeToken<List<MedTretRel>>(){}.getType());
//            medicineHours = gson.fromJson(getArguments().getString("hours"), new TypeToken<List<TakeHours>>(){}.getType());

            ((EditText) viewCA.findViewById(R.id.txt_treatment_name)).setText(treatment.getName());
//            adapter.notifyDataSetChanged();
        }

        userMedicines = new ArrayList<>();
        //Rellenar con campos de base de datos del kit del usuario
        new ApiCallThread<List<Medicine>>(new AsyncResponse<List<Medicine>>(){
            @Override
            public List<Medicine> apiCall(Object... params) {
                return dbo.getMedicine_userId(((String) params[1]));
            }

            @Override
            public void processFinish(View v, List<Medicine> result){
                userMedicines = result;
                ListView list = viewCA.findViewById(R.id.list);
                List adapterData = new ArrayList(){
                    {
                        add(treatmentMedicines);
                        add(relations);
                        add(removedRelations);
                        add(editedRelations);
                        add(userMedicines);
                        add(treatment);
                        add(isTreatmentEdit);
//                        add(medicineHours);
                    }
                };
                adapter = new TreatmentMedicinesListAdapter<>(getContext(), R.layout.treatment_edit_list_item, adapterData);
                list.setAdapter(adapter);

                Button btnAdd = viewCA.findViewById(R.id.btn_treatment_edit_add);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        treatment.setName(((EditText) viewCA.findViewById(R.id.txt_treatment_name)).getText().toString());

                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();

                        bundle.putBoolean("isMedicineEdit", false);
                        bundle.putBoolean("isTreatmentEdit", isTreatmentEdit);
                        try {
                            bundle.putString("treatment",  new JSONObject(gson.toJson(treatment)).toString());
                            bundle.putString("relations", new JSONArray(gson.toJson(relations)).toString());
                            bundle.putString("medicines", new JSONArray(gson.toJson(treatmentMedicines)).toString());
                            bundle.putString("userMedicines", new JSONArray(gson.toJson(userMedicines)).toString());
                        } catch (Exception e) {
                        }

                        Navigation.findNavController(v).navigate(R.id.action_treatment_edit_to_treatment_edit_add_medicine, bundle);
                    }
                });
            }
        }).execute(viewCA, dbo.getIdLogged());

        Button btnDone = viewCA.findViewById(R.id.btn_treatment_edit_done);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                treatment.setName(((EditText) viewCA.findViewById(R.id.txt_treatment_name)).getText().toString());

                treatment.setIdUser(dbo.getIdLogged());

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
                            if (isTreatmentEdit) {
                                for (MedTretRel rel : relations) {
                                    if (rel.isNew()) {
                                        if (dbo.insertRelation(rel) != null) {
                                            PersistableBundle bundle = new PersistableBundle();

                                            bundle.putString("rel_id", rel.getId());
                                            bundle.putLong("rel_hour", getNextDose(rel.getHours()));
                                            bundle.putString("rel_med_id", rel.getIdMedicine());
                                            bundle.putString("rel_treat_id", rel.getIdTreatment());
                                            bundle.putLong("rel_end_date", rel.getFinalDate());

                                            scheduleDose(getContext(), bundle);
                                        }
                                    }
                                    if (rel.isEdited()) {
                                        if (dbo.updateRelation(rel) != null) {
                                            PersistableBundle bundle = new PersistableBundle();

                                            bundle.putString("rel_id", rel.getId());
                                            bundle.putLong("rel_hour", getNextDose(rel.getHours()));
                                            bundle.putString("rel_med_id", rel.getIdMedicine());
                                            bundle.putString("rel_treat_id", rel.getIdTreatment());
                                            bundle.putLong("rel_end_date", rel.getFinalDate());

                                            scheduleDose(getContext(), bundle);
                                        }
                                    }
                                }
                                for (MedTretRel rel : removedRelations) {
                                    if (dbo.deleteRelation(rel) != null) {
                                        removeSchedule(getContext(), rel.getId().hashCode());
                                    }
                                }
                                dbo.updateTreatment(treatment,  null);
                            }
                            else {
                                treatment.setId(dbo.insertTreatment(treatment));
                                for (MedTretRel rel : relations) {
                                    rel.setIdTreatment(treatment.getId());
                                    if (dbo.insertRelation(rel) != null) {
                                        PersistableBundle bundle = new PersistableBundle();

                                        bundle.putString("rel_id", rel.getId());
                                        bundle.putLong("rel_hour", getNextDose(rel.getHours()));
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
