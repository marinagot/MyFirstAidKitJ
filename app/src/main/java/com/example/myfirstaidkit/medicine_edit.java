package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link medicine_edit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link medicine_edit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class medicine_edit extends Fragment {
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
    Medicine med = new Medicine();
    View viewCA;
    
    public medicine_edit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment medicine_edit.
     */
    // TODO: Rename and change types and number of parameters
    public static medicine_edit newInstance(String param1, String param2) {
        medicine_edit fragment = new medicine_edit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String med = getArguments().getString("medicine");

        Gson gson = new Gson();
        Type medicineType = new TypeToken<Medicine>(){}.getType();
        Medicine medicine = gson.fromJson(med, medicineType);

        ((EditText) viewCA.findViewById(R.id.txt_medicine_name)).setText(medicine.getName());
        ((EditText) viewCA.findViewById(R.id.txt_edit_medicine_num)).setText(String.valueOf(medicine.getDoseNumber()));

        Long date = medicine.getExpirationDate();
        String dateString = new SimpleDateFormat("dd MMM yyyy").format(date);
        setTime((Spinner) viewCA.findViewById(R.id.chosen_date), dateString);
        ((Spinner) viewCA.findViewById(R.id.list_medicine)).setSelection(getSelectedMedicineIndex(medicine.getType()));

//        Button btnOk = view.findViewById(R.id.btn_edit_medicine);
//
//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getFragmentManager().beginTransaction().replace(R.id.content, new first_aid_kit()).commit();
//                getActivity().setTitle("My kit");
//            }
//        });
    }

    private int getSelectedMedicineIndex(String type) {
        switch (type) {
            case "Jarabe":
                return 1;
            default: return 0;
        }
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewCA = inflater.inflate(R.layout.fragment_medicine_edit, container, false);

        us = DataBaseOperations.get_Instance(getContext());

        final Spinner chosDate = viewCA.findViewById(R.id.chosen_date);
        Date d = new Date();
        med.setExpirationDate(d.getTime());
        setTime(chosDate, new SimpleDateFormat("dd MMM yyyy").format(d.getTime()));
        chosDate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    try {
                                        Calendar calendar = new GregorianCalendar(year, month, day);
                                        String finalDate = new SimpleDateFormat("dd MMM yyyy").format(calendar.getTime());
                                        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
                                        med.setExpirationDate(timestamp.getTime());
                                        setTime(chosDate, finalDate);
                                    }
                                    catch (Exception e) { med.setExpirationDate(null);  }
                                }
                            }, year, month, dayOfMonth);
                    datePickerDialog.show();
                }
                return true;
            }
        });

        Button btnDone = viewCA.findViewById(R.id.btn_create_medicine);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                med.setName(((EditText) viewCA.findViewById(R.id.txt_medicine_name)).getText().toString());
                med.setType(((Spinner) viewCA.findViewById(R.id.list_medicine)).getSelectedItem().toString());
                try {
                    med.setDoseNumber(Integer.parseInt(((EditText) viewCA.findViewById(R.id.txt_edit_medicine_num)).getText().toString()));
                }
                catch(Exception e){
                    med.setDoseNumber(-1);
                }

                if(med.getName().equals("") || med.getExpirationDate() == null || med.getDoseNumber().equals(-1)) {
                    //Display Message
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("ERROR!");
                    alertDialog.setMessage("Debes rellenar todos los campos correctamente");
                    alertDialog.show();
                    int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                    TextView tv = alertDialog.findViewById(textViewId);
                    tv.setTextColor(Color.RED);
                    TextView textViewMessage = alertDialog.findViewById(android.R.id.message);
                    textViewMessage.setTextColor(Color.RED);
                }
                else {

                    med.setIdUser(us.getIdLogged(prefs));

                    new ApiCallThread<String>(new AsyncResponse<String>(){
                        @Override
                        public String apiCall(Object... params) {
                            return us.insertMedicine((Medicine) params[1]);
                        }

                        @Override
                        public void processFinish(View v, String result){
                            Navigation.findNavController(v).navigate(R.id.action_medicine_edit_to_first_aid_kit);
                        }
                    }).execute(viewCA, med);
                }
            }
        });

        return viewCA;
    }

    private void setTime(Spinner chosDate, String finalDate) {
        final List<String> plantsList = new ArrayList<>();
        plantsList.add(finalDate);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, plantsList);
        chosDate.setAdapter(spinnerArrayAdapter);
        chosDate.setSelection(0);
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
