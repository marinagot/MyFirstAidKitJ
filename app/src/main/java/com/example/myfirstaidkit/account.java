package com.example.myfirstaidkit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


import com.example.myfirstaidkit.data.ApiCallThread;
import com.example.myfirstaidkit.data.AsyncResponse;
import com.example.myfirstaidkit.data.DataBaseOperations;

import java.util.Objects;

import static com.example.myfirstaidkit.helpers.Utils.showError;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link account.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class account extends Fragment {

    EditText old_password, new_password, confirm_password, del_password;
    DataBaseOperations us;

    //Preferencias de la aplicación
    SharedPreferences prefs;

    public account() {
        // Required empty public constructor
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        old_password = v.findViewById(R.id.old_password);
        new_password = v.findViewById(R.id.new_password);
        confirm_password = v.findViewById(R.id.confirm_password);

        Button btnChP = v.findViewById(R.id.btn_change_password);
        btnChP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
                                alertDialog.setMessage("Password changed");
                                alertDialog.show();
                            }
                            else {
                                showError(getContext(), R.string.apiErrorTitle, R.string.apiResetPasswordErrorMessage);
                            }
                        }
                    }).execute(v, us.getEmailLogged(), old_password.getText().toString(), new_password.getText().toString());
                }
                // Si las contraseñas nuevas no coinciden
                else {
                    showError(getContext(), R.string.apiErrorTitle, R.string.apiResetPasswordMatchErrorMessage);
                }
            }
        });

        Button btnDel = v.findViewById(R.id.btn_delete_account);
        btnDel.setOnClickListener(new View.OnClickListener() {

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

                            prefs.edit().clear().apply();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else {
                            showError(getContext(), R.string.apiErrorTitle, R.string.apiResetPasswordMatchErrorMessage);
                        }
                    }
                }).execute(v, us.getIdLogged(), del_password.getText().toString());
            }
        });

    return v;

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
