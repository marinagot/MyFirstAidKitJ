package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link account.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class account extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText old_password, new_password, confirm_password, del_password;
    DataBaseOperations us;

    //Preferencias de la aplicación
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    public account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment account.
     */
    // TODO: Rename and change types and number of parameters
    public static account newInstance(String param1, String param2) {
        account fragment = new account();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        Button btnChP = v.findViewById(R.id.btn_acc_chpwd);
        btnChP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertView = inflater.inflate(R.layout.fragment_account_change_password_pop_up, null);
                builder.setView(alertView);
                final AlertDialog popUpCP = builder.show();

                old_password = alertView.findViewById(R.id.old_password);
                new_password = alertView.findViewById(R.id.new_password);
                confirm_password = alertView.findViewById(R.id.confirm_password_Ch);

                us = DataBaseOperations.get_Instance(getContext());

                Button btnDoneCh = alertView.findViewById(R.id.btn_ch_pwd_done);
                Button btnCancelCh = alertView.findViewById(R.id.btn_ch_pwd_cancel);

                btnDoneCh.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Si las contraseñas nuevas coinciden
                        if ((new_password.getText().toString()).equals(confirm_password.getText().toString())) {
                            new ApiCallThread<String>(new AsyncResponse<String>(){
                                @Override
                                public String apiCall(Object... params) {
                                    return us.updateUserPassword((String) params[1], (String) params[2], (String) params[3]);
                                }

                                @Override
                                public void processFinish(View v, String result){
                                    if (result != null) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                        alertDialog.setTitle("Successful!");
                                        alertDialog.setMessage("Password changed" );
                                        alertDialog.show();
                                        popUpCP.dismiss();
                                    }
                                    else {
                                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                        alertDialog.setTitle("Something went wrong!");
                                        alertDialog.setMessage("The current password introduced does not match with the one is registered, try again");
                                        alertDialog.show();
                                        int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                                        TextView tv = alertDialog.findViewById(textViewId);
                                        tv.setTextColor(Color.RED);
                                        TextView textViewMessage = alertDialog.findViewById(android.R.id.message);
                                        textViewMessage.setTextColor(Color.RED);
                                    }
                                }
                            }).execute(v, us.getEmailLogged(prefs), old_password.getText().toString(), new_password.getText().toString());
                        }
                        // Si las contraseñas nuevas no coinciden
                        else {
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setTitle("Something went wrong!");
                            alertDialog.setMessage("The new password and the confirm password does not match, try again");
                            alertDialog.show();
                            int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                            TextView tv = alertDialog.findViewById(textViewId);
                            tv.setTextColor(Color.RED);
                            TextView textViewMessage = alertDialog.findViewById(android.R.id.message);
                            textViewMessage.setTextColor(Color.RED);
                        }
                    }
                });

                btnCancelCh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpCP.dismiss();
                    }
                });

            }
        });

        Button btnDel = v.findViewById(R.id.btn_acc_del);
        btnDel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertView = inflater.inflate(R.layout.fragment_account_delete_account_pop_up, null);
                builder.setView(alertView);
                final AlertDialog popUpCP = builder.show();

                del_password = alertView.findViewById(R.id.pwd_del_acc);

                us = DataBaseOperations.get_Instance(getContext());

                Button btnDoneDel = alertView.findViewById(R.id.btn_done_del);
                Button btnCancelDel = alertView.findViewById(R.id.btn_cancel_del);

                btnDoneDel.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                new ApiCallThread<String>(new AsyncResponse<Boolean>(){
                    @Override
                    public Boolean apiCall(Object... params) {
                        return us.deleteUser((String) params[1], (String) params[2]);
                    }

                    @Override
                    public void processFinish(View v, Boolean result){
                        if (result) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setTitle("Successful!");
                            alertDialog.setMessage("Account deleted" );
                            alertDialog.show();
                            popUpCP.dismiss();

                            edit.clear();
                            edit.apply();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            alertDialog.setTitle("Something went wrong!");
                            alertDialog.setMessage("The current password introduced does not match with the one is registered, try again");
                            alertDialog.show();
                            int textViewId = alertDialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                            TextView tv = alertDialog.findViewById(textViewId);
                            tv.setTextColor(Color.RED);
                            TextView textViewMessage = alertDialog.findViewById(android.R.id.message);
                            textViewMessage.setTextColor(Color.RED);
                        }
                    }
                }).execute(v, us.getIdLogged(prefs), del_password.getText().toString());
            }
        });

        btnCancelDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popUpCP.dismiss();
                    }
                });
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
