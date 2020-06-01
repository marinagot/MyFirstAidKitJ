package com.example.myfirstaidkit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Spinner;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.Medicine;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import static com.example.myfirstaidkit.helpers.Utils.showError;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link medicine_edit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class medicine_edit extends Fragment {
    //Preferencias de la aplicación
    SharedPreferences prefs;

    DataBaseOperations dbo;
    Medicine med = new Medicine();
    View viewCA;
    boolean isEdit = false;
    
    public medicine_edit() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() != null) {
            isEdit = true;
            Gson gson = new Gson();
            med = gson.fromJson(getArguments().getString("medicine"), new TypeToken<Medicine>(){}.getType());

            ((EditText) viewCA.findViewById(R.id.txt_medicine_name)).setText(med.getName());
            ((EditText) viewCA.findViewById(R.id.txt_edit_medicine_num)).setText(String.valueOf(med.getDoseNumber()));

            Long date = med.getExpirationDate();
            String dateString = new SimpleDateFormat("dd MMM yyyy").format(date);
            setTime((Spinner) viewCA.findViewById(R.id.chosen_date), dateString);
            ((Spinner) viewCA.findViewById(R.id.list_medicine)).setSelection(getSelectedMedicineIndex(med.getType()));
        }

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
        if ("Jarabe".equals(type)) {
            return 1;
        }
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        try {
            Objects.requireNonNull(getActivity()).findViewById(R.id.nav_view).setVisibility(View.GONE);
        } catch (Exception ignored) {}
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

        dbo = DataBaseOperations.get_Instance(Objects.requireNonNull(getContext()));

        final Spinner choseDate = viewCA.findViewById(R.id.chosen_date);
        Date d = new Date();
        med.setExpirationDate(d.getTime());
        setTime(choseDate, new SimpleDateFormat("dd MMM yyyy").format(d.getTime()));
        choseDate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    try {
                                        Calendar calendar = new GregorianCalendar(year, month, day);
                                        String finalDate = new SimpleDateFormat("dd MMM yyyy").format(calendar.getTime());
                                        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
                                        med.setExpirationDate(timestamp.getTime());
                                        setTime(choseDate, finalDate);
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
                    showError(getContext(), R.string.invalidFieldsTitle, R.string.invalidFieldsMessage);
                }
                else {
                    med.setIdUser(dbo.getIdLogged());

                    new ApiCallThread<String>(new AsyncResponse<String>(){
                        @Override
                        public String apiCall(Object... params) {
                            Medicine medAux = (Medicine) params[1];
                            if (isEdit)
                                return dbo.updateMedicine(medAux);
                            return dbo.insertMedicine(medAux);
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
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_dropdown_item_1line, plantsList);
        chosDate.setAdapter(spinnerArrayAdapter);
        chosDate.setSelection(0);
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
