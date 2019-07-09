package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.create_account;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link login.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class login extends Fragment {

    private OnFragmentInteractionListener mListener;

    EditText username,password;
    DataBaseOperations us;

    //Preferencias de la aplicación
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    public login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        username = (EditText) v.findViewById(R.id.txt_username);
        password = (EditText) v.findViewById(R.id.txt_pwd);

        us = DataBaseOperations.get_Instance(getContext());

        //Not logged
        prefs.edit().putBoolean("Islogin", false).commit();

        Button btnRegister = v.findViewById(R.id.btn_sign_up);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Crea el nuevo fragmento y la transacción.

                try {
                    Navigation.findNavController(v).navigate(R.id.action_login_to_create_account);
                } catch (Exception e){
                    getFragmentManager().beginTransaction().replace(R.id.content, new create_account()).commit();
                }
                getActivity().setTitle("Create Account");
            }
        });

        Button btnLogin = v.findViewById(R.id.btn_sign_in);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")||password.getText().toString().equals(""))
                {
                    //Display Message
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("ALERT!");
                    alertDialog.setMessage("All fields must be filled");
                    alertDialog.show();
                    int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                    TextView tv = (TextView) alertDialog.findViewById(textViewId);
                    tv.setTextColor(Color.RED);
                    TextView textViewMessage = (TextView) alertDialog.findViewById(android.R.id.message);
                    textViewMessage.setTextColor(Color.RED);
                }
                else {
                    boolean sign_in = us.loginData(username.getText().toString(), password.getText().toString());

                    if (sign_in == true) {
                        edit.putString("username", username.getText().toString());
                        prefs.edit().putBoolean("Islogin", true).commit();

                        try {
                            Navigation.findNavController(v).navigate(R.id.action_login_to_home);
                        } catch (Exception e){
                            getFragmentManager().beginTransaction().replace(R.id.content, new home()).commit();
                        }
                        getActivity().setTitle("Home");

                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        alertDialog.setTitle("Something went wrong!");
                        alertDialog.setMessage("The username or the password, or both, are incorrect, please try again");
                        alertDialog.show();
                        int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                        TextView tv = (TextView) alertDialog.findViewById(textViewId);
                        tv.setTextColor(Color.RED);
                        TextView textViewMessage = (TextView) alertDialog.findViewById(android.R.id.message);
                        textViewMessage.setTextColor(Color.RED);
                    }
                }


            }
        });
        return v;
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
