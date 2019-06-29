package com.example.myfirstaidkit;

import android.app.ActionBar;
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
import android.widget.TextView;


import androidx.navigation.Navigation;

import com.example.myfirstaidkit.data.UsersSQLiteHelper;



public class create_account extends Fragment {

    private OnFragmentInteractionListener mListener;
    UsersSQLiteHelper us;
    EditText username, email, birthday, password, confirm_password;
    ImageView avatar;


    /**
     * A simple {@link Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link create_account.OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link create_account#newInstance} factory method to
     * create an instance of this fragment.
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

        us = new UsersSQLiteHelper(getActivity());

        username = (EditText) viewCA.findViewById(R.id.txt_username_set);
        email = (EditText) viewCA.findViewById(R.id.txt_email_set);
        birthday = (EditText) viewCA.findViewById(R.id.txt_birth_set);
        password = (EditText) viewCA.findViewById(R.id.txt_pwd_set);
        confirm_password = (EditText) viewCA.findViewById(R.id.txt_pwd_conf_set);
        avatar = (ImageView) viewCA.findViewById(R.id.image_profile_set);
        Button btnDone = (Button) viewCA.findViewById(R.id.btn_confirm_account);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((username.getText().toString().equals(""))||(email.getText().toString().equals(""))||
                        (birthday.getText().toString().equals(""))||(password.getText().toString().equals(""))
                        ||(confirm_password.getText().toString().equals("")))
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
                else if((password.getText().toString()).equals(confirm_password.getText().toString())) {
                    us.insertData(username.getText().toString(), email.getText().toString(), birthday.getText().toString(),
                            avatar.toString(), password.getText().toString(), confirm_password.getText().toString());

                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Successful!");
                    alertDialog.setMessage("Account for " + username.getText().toString() + " created" );
                    alertDialog.show();
                    Navigation.findNavController(v).navigate(R.id.action_create_account_to_home);
                    //getFragmentManager().beginTransaction().replace(R.id.content, new home()).commit();
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Something went wrong!");
                    alertDialog.setMessage("The passwords does not match, try again");
                    alertDialog.show();
                    int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                    TextView tv = (TextView) alertDialog.findViewById(textViewId);
                    tv.setTextColor(Color.RED);
                    TextView textViewMessage = (TextView) alertDialog.findViewById(android.R.id.message);
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
