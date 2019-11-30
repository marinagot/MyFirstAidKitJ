package com.example.myfirstaidkit;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

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

        FloatingActionButton fab = view.findViewById(R.id.newTreatment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_treatments_to_treatment_edit);
                /*getFragmentManager().beginTransaction().replace(R.id.content, new treatment_edit()).commit();
                getActivity().setTitle("New treatment");*/
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
        // Muestra la barra inferior de navegación
        getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        viewCA = inflater.inflate(R.layout.fragment_treatments, container, false);
        us = DataBaseOperations.get_Instance(getContext());

        final Spinner spinner = viewCA.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               new ApiCallThread<List<Treatment>>(new AsyncResponse<List<Treatment>>(){
                   @Override
                   public List<Treatment> apiCall(Object... params) {
                       switch (((String) params[2])) {
                           case "Activos":
                               return getActiveTreatments(us.getTreatment_userId(us.getUser_Email((String) params[1]).getId()));
                           case "Terminados":
                               return getEndedTreatments(us.getTreatment_userId(us.getUser_Email((String) params[1]).getId()));
                           default:
                               List<Treatment> aux = us.getTreatment_userId(us.getUser_Email((String) params[1]).getId());
                               Collections.reverse(aux);
                               return aux;
                       }
                   }

                   @Override
                   public void processFinish(View v, List<Treatment> result){
                       treatmentList = result;
                       ListView list = viewCA.findViewById(R.id.list_user_treatments);
                       adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, treatmentList);
                       list.setAdapter(adapter);
                   }
               }).execute(view, us.getEmailLogged(), spinner.getSelectedItem());
           }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }
       });

        final ListView list = viewCA.findViewById(R.id.list_user_treatments);

        new ApiCallThread<List<Treatment>>(new AsyncResponse<List<Treatment>>(){
            @Override
            public List<Treatment> apiCall(Object... params) {
                return getActiveTreatments(us.getTreatment_userId(us.getUser_Email((String) params[1]).getId()));
            }

            @Override
            public void processFinish(View v, List<Treatment> result){
                treatmentList = result;
                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, treatmentList);
                list.setAdapter(adapter);
            }
        }).execute(viewCA, us.getEmailLogged());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                final Treatment t = treatmentList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflaterl = getActivity().getLayoutInflater();
                alert = inflaterl.inflate(R.layout.fragment_treatment_medicine_list_pop_up, null);
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
                        frequency.setText(relation.getFrequency().toString());

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

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 17);
        cal.set(Calendar.MINUTE, 33);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        setAlarm(cal);


        return viewCA;
    }

    public void setAlarm(Calendar targetCal) {

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);

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
