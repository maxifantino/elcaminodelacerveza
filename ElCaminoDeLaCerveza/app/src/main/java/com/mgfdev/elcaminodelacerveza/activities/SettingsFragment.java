package com.mgfdev.elcaminodelacerveza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SeekBarPreference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.adapter.CustomSwitchPreference;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.AppConstants;
import com.mgfdev.elcaminodelacerveza.helpers.GeofencesConstants;
import com.mgfdev.elcaminodelacerveza.services.LoginModule;
import com.mgfdev.elcaminodelacerveza.services.SharedPreferenceManager;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements CustomObserver{

    private OnFragmentInteractionListener mListener;
    private Context ctx;
    private User user;
    private SwitchPreferenceCompat closeSessionBtn;
    private SeekBarPreference metersSeekBar;
    private SeekBarPreference  timeSeekBar;
    private TextView metersText;
    private TextView timeText;
    private View layoutMeters;
    private View layoutMins;
    private SharedPreferenceManager sharedPreferences;
    private HomeActivity activity;
    private SwitchPreferenceCompat locationButton;
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

    private void forceStorePreferences(){
        sharedPreferences.storeValue("meters", Integer.toString(metersSeekBar.getValue()));
        sharedPreferences.storeValue("time", Integer.toString(timeSeekBar.getValue()));

    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        view.setBackgroundColor(getResources().getColor(R.color.preferenceBackground,null));
        setupLayoutViews(view);
        // TODO: VER porque no actualiza sharedPreferences... Fuerzo actualizacion
        forceStorePreferences();
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
        locationButton = (SwitchPreferenceCompat) findPreference("location");
        locationButton.setChecked(Boolean.parseBoolean(SharedPreferenceManager.getInstance(ctx).getStringValue("location")));
        setLocationLayoutState (locationButton.isChecked());
        activity.attachSettingsObserver(this);
        locationButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                activity.setLocationUpdates(locationButton.isChecked());
                setLocationLayoutState (locationButton.isChecked());
                return true;
            }
        });
    }


    public void enableLocationFrame (boolean enabled){
        setLocationLayoutState(enabled);
        locationButton.setChecked(enabled);
    }

    private void setLocationLayoutState (boolean enabled){
        metersSeekBar.setEnabled(enabled);
        timeSeekBar.setEnabled(enabled);
    }

    private Map getPreferencesValues(){
        Map<String, String> result = new HashMap<String, String>();
        result.put("meters",StringUtils.defaultString(sharedPreferences.getStringValue("meters"),Integer.toString(GeofencesConstants.GEOFENCE_RADIUS_IN_METERS)));
        result.put("time",StringUtils.defaultString(sharedPreferences.getStringValue("time"),"0"));
        result.put("location",StringUtils.defaultString(sharedPreferences.getStringValue("location"), "false"));

        return result;
    }

    private void setupSeekBars(View rootView,  Map<String, String> config){
        // get sharedPreferenceValues
        metersSeekBar = (SeekBarPreference) findPreference("meters");
        timeSeekBar= (SeekBarPreference) findPreference("time");
    //    metersSeekBar.setDefaultValue(Integer.parseInt(config.get("meters")));
    //    timeSeekBar.setDefaultValue(Integer.parseInt(config.get("time")));
    }

    private void setupCloseSessionButton ( View rootView){
        closeSessionBtn =(SwitchPreferenceCompat) findPreference("closeSession");
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

    @Override
    public void notifyEvent(int eventId) {
        if (eventId == AppConstants.SETTINGS_LOCATION_OFF){
            enableLocationFrame(false);
        }
        else if (eventId == AppConstants.SETTINGS_LOCATION_ON){
            enableLocationFrame(true);
        }
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
