package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;
import com.example.myfirstaidkit.data.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class create_account extends Fragment {

    private OnFragmentInteractionListener mListener;
    DataBaseOperations us;
    EditText username, email, password, confirm_password;

    //Preferencias de la aplicación
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    /**
     * A simple {@link Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link create_account.OnFragmentInteractionListener} interface
     * to handle interaction events
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View viewCA = inflater.inflate(R.layout.fragment_create_account, container, false);


        us = DataBaseOperations.get_Instance(getContext());

        username = viewCA.findViewById(R.id.txt_username_set);
        email = viewCA.findViewById(R.id.txt_email_set);

        password = viewCA.findViewById(R.id.txt_pwd_set);
        confirm_password = viewCA.findViewById(R.id.txt_pwd_conf_set);

        TextView logIn = viewCA.findViewById(R.id.sign_in_text);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_create_account_to_login);
            }
        });
        Button register = viewCA.findViewById(R.id.btn_confirm_account);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((username.getText().toString().equals(""))||(email.getText().toString().equals(""))
                        ||(password.getText().toString().equals(""))
                        ||(confirm_password.getText().toString().equals("")))
                {
                    //Display Message
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("ALERT!");
                    alertDialog.setMessage("All fields must be filled");
                    alertDialog.show();
                    int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                    TextView tv = alertDialog.findViewById(textViewId);
                    tv.setTextColor(Color.RED);
                    TextView textViewMessage = alertDialog.findViewById(android.R.id.message);
                    textViewMessage.setTextColor(Color.RED);
                }
                else if((password.getText().toString()).equals(confirm_password.getText().toString())) {

                    User user = new User("0", username.getText().toString(),
                            email.getText().toString(), password.getText().toString());

                    new ApiCallThread<User>(new AsyncResponse<User>(){
                        @Override
                        public User apiCall(Object... params) {
                            return us.insertUser((User) params[1]);
                        }

                        @Override
                        public void processFinish(View v, User result){

                            if(result != null) {
                                edit.putString("username", result.getUsername());
                                edit.putString("email", result.getEmail());
                                edit.putString("id", result.getId());
                                edit.apply();
                            }
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setTitle("Successful!");
                            alertDialog.setMessage("Account for " + username.getText().toString() + " created" );
                            alertDialog.show();

                            Intent intent = new Intent(getContext(), LoggedActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }).execute(v, user);
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Something went wrong!");
                    alertDialog.setMessage("The passwords does not match, try again");
                    alertDialog.show();
                    int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                    TextView tv = alertDialog.findViewById(textViewId);
                    tv.setTextColor(Color.RED);
                    TextView textViewMessage = alertDialog.findViewById(android.R.id.message);
                    textViewMessage.setTextColor(Color.RED);
                }
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
