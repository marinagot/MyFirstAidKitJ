package com.example.myfirstaidkit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class settings extends Fragment {
    SharedPreferences prefs;

    public settings() {
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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View vSett = inflater.inflate(R.layout.fragment_settings, container, false);

        final Switch themeSelector = vSett.findViewById(R.id.theme_switch);

        boolean isThemeEdited = prefs.getBoolean("isThemeEdited",false);
        boolean isThemeDark = prefs.getBoolean("isThemeDark",false);

        if (!isThemeEdited) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    themeSelector.setChecked(true);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    themeSelector.setChecked(false);
                    break;
            }
        } else if (isThemeDark) {
            themeSelector.setChecked(true);
        }
        themeSelector.setOnClickListener(new Switch.OnClickListener() {
            public void onClick(View view) {

                if (themeSelector.isChecked()) {
                    Objects.requireNonNull(getActivity()).setTheme(R.style.AppThemeDark);
                    Objects.requireNonNull(getContext()).getTheme().applyStyle(R.style.AppThemeDark, true);
                } else {
                    Objects.requireNonNull(getActivity()).setTheme(R.style.AppTheme);
                    Objects.requireNonNull(getContext()).getTheme().applyStyle(R.style.AppTheme, true);
                }


                Intent intent = getActivity().getIntent();
                getActivity().overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().finish();

                getActivity().overridePendingTransition(0, 0);
                startActivity(intent);

//                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.settings);



                prefs.edit().putBoolean("isThemeEdited", true)
                    .putBoolean("isThemeDark", themeSelector.isChecked())
                    .apply();
            }
        });


//        RadioButton btnDefault = (RadioButton) vSett.findViewById(R.id.btn_rad_def);
//        if ((btnDefault.isChecked()) == true ){
//            btnSaSett.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().setTheme(R.style.AppTheme);
//                }
//            });
//        }
//        RadioButton btnDark = (RadioButton) vSett.findViewById(R.id.btn_rad_dark);
//        if ((btnDark.isChecked()) == true ){
//            btnSaSett.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().setTheme(R.style.AppThemeDark);
//                }
//            });
//        }
//
//        RadioButton btnLight = (RadioButton) vSett.findViewById(R.id.btn_rad_light);
//        if ((btnLight.isChecked()) == true ){
//            btnSaSett.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().setTheme(R.style.AppTheme);
//
//                }
//            });
//        }

        return vSett;
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
