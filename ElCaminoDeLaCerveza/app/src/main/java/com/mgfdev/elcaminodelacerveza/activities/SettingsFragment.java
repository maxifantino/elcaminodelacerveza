package com.mgfdev.elcaminodelacerveza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.adapter.PassportListAdapter;
import com.mgfdev.elcaminodelacerveza.dto.Brewer;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.FontHelper;
import com.mgfdev.elcaminodelacerveza.helpers.MessageDialogHelper;
import com.mgfdev.elcaminodelacerveza.services.LoginModule;
import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.services.SharedPreferenceManager;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends PreferenceFragment {

    private OnFragmentInteractionListener mListener;
    private Context ctx;
    private User user;
    private SwitchPreference closeSessionBtn;
    private Preference  metersSeekBar;
    private Preference  timeSeekBar;
    private TextView metersText;
    private TextView timeText;
    private View layoutMeters;
    private View layoutMins;
    private SharedPreferenceManager sharedPreferences;
    private HomeActivity activity;
    private SwitchPreference locationButton;
    private View rootView;
    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getContext();
        activity = (HomeActivity) getActivity();
        user = activity.getUser();
        sharedPreferences = SharedPreferenceManager.getInstance(activity);
        addPreferencesFromResource(R.xml.fragment_settings2);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        view.setBackgroundColor(getResources().getColor(R.color.preferenceBackground,null));
        setupLayoutViews(view);
        return view;
    }
    private void setupLayoutViews(View rootView){
        Map<String, String> config = getPreferencesValues();
        setupSessionText();
        setupSeekBars(rootView, config);
        setupCloseSessionButton(rootView);
        setupSwitchButtons(rootView, config);
    }
    private void setupSessionText(){
        Preference sessionText = findPreference("closeSession");
        sessionText.setTitle(MessageFormat.format(getString(R.string.session_description), user.getUsername()));
    }

    private void setupSwitchButtons(View rootView,  Map<String, String> config){
        locationButton = (SwitchPreference) findPreference("location");
        locationButton.setDefaultValue(true);
        locationButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                activity.setLocationUpdates(locationButton.isChecked());
                setLocationLayoutState (locationButton.isChecked());
                return true;
            }
        });
    }

    private void setLocationLayoutState (boolean enabled){
        metersSeekBar.setEnabled(enabled);
     /*   metersSeekBar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Preference  metersText = findPreference("timeText");
                metersText.setTitle(preference.getTitle());
                return true;
            }
        });*/
        timeSeekBar.setEnabled(enabled);
    }

    private Map getPreferencesValues(){
        Map<String, String> result = new HashMap<String, String>();
        result.put("meters",sharedPreferences.getStringValue("meters"));
        result.put("mins",sharedPreferences.getStringValue("mins"));
        result.put("location",sharedPreferences.getStringValue("location"));

        return result;
    }

    private void setupSeekBars(View rootView,  Map<String, String> config){
        // get sharedPreferenceValues
        metersSeekBar = findPreference("meters");
        timeSeekBar= findPreference("time");
        metersSeekBar.setDefaultValue(Integer.parseInt(config.get("meters")));
        timeSeekBar.setDefaultValue(Integer.parseInt(config.get("mins")));
    }

    private void setupCloseSessionButton ( View rootView){
        closeSessionBtn =(SwitchPreference)findPreference("closeSession");
        closeSessionBtn.setDefaultValue(false);
        closeSessionBtn.setEnabled(StringUtils.isNotBlank(user.getUsername()));
        closeSessionBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                checkCloseSession(activity, getString(R.string.close_session), getString(R.string.close_session_message));
                return true;
            }
        });
    }

    public void checkCloseSession(Activity act, String title, String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(act);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                doLogout();
                redirectLogin(ctx);
            }
        });

        dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                closeSessionBtn.setChecked(false);            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

    }

    private void redirectLogin(Context ctx){
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(intent);
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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void doLogout(){
        LoginModule module = new LoginModule(ctx);
        module.doLogout(ctx, user);
    }
}
