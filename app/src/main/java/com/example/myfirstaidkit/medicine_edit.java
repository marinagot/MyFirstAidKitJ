package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myfirstaidkit.data.UsersSQLiteHelper;


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

        Button btnOk = view.findViewById(R.id.btn_edit_medicine);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.content, new first_aid_kit()).commit();
                getActivity().setTitle("My kit");
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

    UsersSQLiteHelper us;
    EditText treatmentName, expirationDate, dosisAmount;
    Spinner medicine_list;
    ImageView avatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewCA = inflater.inflate(R.layout.fragment_medicine_edit, container, false);

        us = new UsersSQLiteHelper(getActivity());

        treatmentName = (EditText) viewCA.findViewById(R.id.txt_treatment_name);
        medicine_list = (Spinner) viewCA.findViewById(R.id.list_medicine);
        dosisAmount = (EditText) viewCA.findViewById(R.id.txt_edit_medicine_num);
        expirationDate = (EditText) viewCA.findViewById(R.id.medicine_edit_expire_date);
        Button btnDone = (Button) viewCA.findViewById(R.id.btn_edit_medicine);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if((treatmentName.getText().toString().equals(""))||
//                        (birthday.getText().toString().equals(""))||(password.getText().toString().equals(""))
//                        ||(confirm_password.getText().toString().equals("")))
//                {
//                    //Display Message
//                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
//                    alertDialog.setTitle("ALERT!");
//                    alertDialog.setMessage("All fields must be filled");
//                    alertDialog.show();
//                    int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
//                    TextView tv = (TextView) alertDialog.findViewById(textViewId);
//                    tv.setTextColor(Color.RED);
//                    TextView textViewMessage = (TextView) alertDialog.findViewById(android.R.id.message);
//                    textViewMessage.setTextColor(Color.RED);
//                }
//                else if((password.getText().toString()).equals(confirm_password.getText().toString())) {
//                    us.insertData(treatmentName.getText().toString(), email.getText().toString(), birthday.getText().toString(),
//                            avatar.toString(), password.getText().toString(), confirm_password.getText().toString());
//
//                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
//                    alertDialog.setTitle("Successful!");
//                    alertDialog.setMessage("Account for " + treatmentName.getText().toString() + " created" );
//                    alertDialog.show();
//                    getFragmentManager().beginTransaction().replace(R.id.content, new login()).commit();
//                }
//                else{
//                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
//                    alertDialog.setTitle("Something went wrong!");
//                    alertDialog.setMessage("The passwords does not match, try again");
//                    alertDialog.show();
//                    int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
//                    TextView tv = (TextView) alertDialog.findViewById(textViewId);
//                    tv.setTextColor(Color.RED);
//                    TextView textViewMessage = (TextView) alertDialog.findViewById(android.R.id.message);
//                    textViewMessage.setTextColor(Color.RED);
//
//                }

            }
        });


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
