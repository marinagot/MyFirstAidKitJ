package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.MedTretRel;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.TakeHours;
import com.example.myfirstaidkit.data.Treatment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link treatment_edit_add_medicine.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link treatment_edit_add_medicine#newInstance} factory method to
 * create an instance of this fragment.
 */
public class treatment_edit_add_medicine extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    DataBaseOperations dbo;

    Treatment treatment;
    ArrayList<Medicine> treatmentMedicines = new ArrayList<>();
    List<MedTretRel> relations = new ArrayList<>();

    View viewCA;

    List<Medicine> userMedicines;
    List<TakeHours> medicineHours;
    List<TakeHours> removedHours = new ArrayList<>();
    Date finalDate;

    Spinner spinnerMedicines;

    MedTretRel oldRelation;

    int position;

    boolean isTreatmentEdit = false;
    boolean isMedicineEdit = false;

    private OnFragmentInteractionListener mListener;

    public treatment_edit_add_medicine() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment treatment_edit_add_medicine.
     */
    // TODO: Rename and change types and number of parameters
    public static treatment_edit_add_medicine newInstance(String param1, String param2) {
        treatment_edit_add_medicine fragment = new treatment_edit_add_medicine();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                             Bundle savedInstanceState) {

        viewCA = inflater.inflate(R.layout.fragment_treatment_edit_add_medicine, container, false);
        dbo = DataBaseOperations.get_Instance(getContext());

        if (getArguments() != null) {
            Gson gson = new Gson();

            isTreatmentEdit = getArguments().getBoolean("isTreatmentEdit");
            isMedicineEdit = getArguments().getBoolean("isMedicineEdit");
            relations = gson.fromJson(getArguments().getString("relations"), new TypeToken<List<MedTretRel>>(){}.getType());
            if (isMedicineEdit) {
                position = getArguments().getInt("position");
                oldRelation = relations.get(position);
                medicineHours = gson.fromJson(getArguments().getString("hours"), new TypeToken<List<TakeHours>>(){}.getType());
            } else {
                medicineHours = new ArrayList<>();
            }
            treatment = gson.fromJson(getArguments().getString("treatment"), new TypeToken<Treatment>(){}.getType());
            userMedicines = gson.fromJson(getArguments().getString("userMedicines"), new TypeToken<List<Medicine>>(){}.getType());
            treatmentMedicines = gson.fromJson(getArguments().getString("medicines"), new TypeToken<List<Medicine>>(){}.getType());
            Collections.sort(medicineHours, new Comparator<TakeHours>(){
                public int compare(TakeHours hour1, TakeHours hour2) {
                    // ## Ascending order
                    return hour1.getHour().compareTo(hour2.getHour());
                }
            });
        }

        final Spinner choseDate = viewCA.findViewById(R.id.treatment_edit_add_medicine_final_date);

        choseDate.setOnTouchListener(new View.OnTouchListener() {
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
                                        setTime(choseDate, new SimpleDateFormat("dd MMM yyyy").format(calendar.getTime()));
                                        finalDate = new GregorianCalendar(year, month, day).getTime();
                                    } catch (Exception e){ finalDate = null; }
                                }
                            },year,month,dayOfMonth);
                    datePickerDialog.show();
                }
                return true;
            }
        });

        spinnerMedicines = viewCA.findViewById(R.id.list_medicines);

        ArrayAdapter<Medicine> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, userMedicines);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicines.setAdapter(dataAdapter);

        final ListView listOfHours = viewCA.findViewById(R.id.list_of_hours);
        final HoursListAdapter<TakeHours> hoursAdapter = new HoursListAdapter<>(getContext(), R.layout.hour_list_item, medicineHours, removedHours);
        listOfHours.setAdapter(hoursAdapter);

        if (isMedicineEdit) {
            int a = userMedicines.indexOf(treatmentMedicines.get(position));
            spinnerMedicines.setSelection(a);
            setTime(choseDate, new SimpleDateFormat("dd MMM yyyy").format(oldRelation.getFinalDate()));
            finalDate = new Date(oldRelation.getFinalDate());
        } else {
            Date d = new Date();
            setTime(choseDate, new SimpleDateFormat("dd MMM yyyy").format(d.getTime()));
            finalDate = new Date();
        }

        Button layoutAddHour = viewCA.findViewById(R.id.layout_add_hour);
        layoutAddHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    TakeHours hour = new TakeHours(new SimpleDateFormat("HH:mm").parse(hourOfDay + ":" + minute).getTime());
                                    if (isMedicineEdit) {
                                        hour = new TakeHours(oldRelation.getId(), new SimpleDateFormat("HH:mm").parse(hourOfDay + ":" + minute).getTime());
                                        hour.setIsNew(true);
                                    }
                                    medicineHours.add(hour);
                                    Collections.sort(medicineHours, new Comparator<TakeHours>(){
                                        public int compare(TakeHours hour1, TakeHours hour2) {
                                            // ## Ascending order
                                            return hour1.getHour().compareTo(hour2.getHour());
                                        }
                                    });
                                    hoursAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                }
                            }
                        }, 0, 0, false);
                timePickerDialog.show();
            }
        });


        Button btnAdd = viewCA.findViewById(R.id.btn_add_medicine_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hoursAdapter.isEmpty()) {
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
                    MedTretRel auxRel = new MedTretRel();
                    if (isTreatmentEdit) {
                        auxRel.setIdTreatment(treatment.getId());
                    }
                    auxRel.setFinalDate(finalDate.getTime());
                    if (isMedicineEdit) {
                        auxRel = oldRelation;
                        auxRel.setInitialDate(oldRelation.getInitialDate());
                        auxRel.setIdMedicine(((Medicine) spinnerMedicines.getSelectedItem()).getId());
                        auxRel.setisEdited(true);
                        // this line adds the data of your Spinner and puts in your array
                        ((List<Medicine>) treatmentMedicines).set(position, (Medicine) spinnerMedicines.getSelectedItem());

                        List<TakeHours> takeHoursList = medicineHours;
                        if (isTreatmentEdit) {
                            for (TakeHours hour : removedHours) {
                                hour.setIsRemoved(true);
                                takeHoursList.add(hour);
                            }
                        }
                        auxRel.setHours(takeHoursList);

                        relations.set(position, auxRel);
                    } else {
                        auxRel.setIdMedicine(((Medicine) spinnerMedicines.getSelectedItem()).getId());
                        auxRel.setIsNew(true);
                        auxRel.setInitialDate(new Date().getTime());
                        auxRel.setHours(medicineHours);
                        relations.add(auxRel);
                        // this line adds the data of your Spinner and puts in your array
                        treatmentMedicines.add((Medicine) spinnerMedicines.getSelectedItem());

                    }

                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();

                    try {
                        bundle.putString("relations", new JSONArray(gson.toJson(relations)).toString());
                        bundle.putString("medicines", new JSONArray(gson.toJson(treatmentMedicines)).toString());
                        bundle.putString("hours", new JSONArray(gson.toJson(medicineHours)).toString());
                        bundle.putString("treatment", new JSONObject(gson.toJson(treatment)).toString());
                        bundle.putBoolean("isTreatmentEdit", isTreatmentEdit);
                    } catch (Exception e) {
                    }

                    Navigation.findNavController(v).navigate(R.id.action_treatment_edit_add_medicine_to_treatment_edit, bundle);
                }
            }
        });

        return viewCA;
    }

    private void setTime(Spinner choseDate, String finalDate) {
        final List<String> plantsList = new ArrayList<>();
        plantsList.add(finalDate);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, plantsList);
        choseDate.setAdapter(spinnerArrayAdapter);
        choseDate.setSelection(0);
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
