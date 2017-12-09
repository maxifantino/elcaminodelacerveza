package com.mgfdev.elcaminodelacerveza.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.FontHelper;
import com.mgfdev.elcaminodelacerveza.helpers.MessageDialogHelper;
import com.mgfdev.elcaminodelacerveza.services.LoginModule;
import com.mgfdev.elcaminodelacerveza.R;
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
public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Context ctx;
    private User user;
    private Switch closeSessionBtn;
    private SeekBar metersSeekBar;
    private SeekBar timeSeekBar;
    private TextView metersText;
    private TextView timeText;
    private View layoutMeters;
    private View layoutMins;
    private SharedPreferenceManager sharedPreferences;
    private HomeActivity activity;
    private Switch locationButton;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FontHelper.overrideFonts(ctx, container
                , "montserrat.ttf");
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        setupLayoutViews(rootView);
        TextView sessionDescription = (TextView) rootView.findViewById(R.id.sessionDescriptionText);
        String descriptionText = user != null && StringUtils.isNotEmpty(user.getUsername()) ? MessageFormat.format(getString(R.string.session_description), user.getUsername()) : getString(R.string.no_session_description);
        sessionDescription.setText(descriptionText);
        sessionDescription.setTextColor(ctx.getColor(R.color.descriptionFontColor));
        sessionDescription.setTextSize(18);
        layoutMeters = rootView.findViewById(R.id.metersLayout);
        layoutMins = rootView.findViewById(R.id.minsLayout);
        setLocationLayoutState(locationButton.isChecked());
        return rootView;
    }
    private void setupLayoutViews(View rootView){
        Map<String, String> config = getPreferencesValues();
        setupTextViews(rootView);
        setupSeekBars(rootView, config);
        setupCloseSessionButton(rootView);
        setupSwitchButtons(rootView, config);
    }

    private void setupSwitchButtons(View rootView,  Map<String, String> config){
        locationButton = (Switch) rootView.findViewById(R.id.btnLocation);
        locationButton.setChecked(config.get("Location") == "true");
        locationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sharedPreferences.storeValue("location", locationButton.isChecked()? "true" : "false");
                activity.setLocationUpdates(locationButton.isChecked());
                setLocationLayoutState (locationButton.isChecked());
            }
        });
    }

    private void setLocationLayoutState (boolean enabled){
        layoutMeters.setEnabled(enabled);
        layoutMins.setEnabled(enabled);
        metersSeekBar.setEnabled(enabled);
        timeSeekBar.setEnabled(enabled);
    }

    private void setupTextViews (View rootView){
        metersText = (TextView) rootView.findViewById(R.id.metersText);
        timeText = (TextView) rootView.findViewById(R.id.minsText);

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
        metersSeekBar = (SeekBar) rootView.findViewById(R.id.metersSeekBar);
        timeSeekBar= (SeekBar) rootView.findViewById(R.id.minsSeekBar);

        metersSeekBar.setProgress(Integer.parseInt(config.get("meters")));
        metersSeekBar.incrementProgressBy(50);

        timeSeekBar.setProgress(Integer.parseInt(config.get("mins")));
        timeSeekBar.incrementProgressBy(1);

        metersText.setText(Integer.toString(metersSeekBar.getProgress()));
        timeText.setText(Integer.toString(timeSeekBar.getProgress()));
        metersSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                metersText.setText(Integer.toString(progress + 100));
                sharedPreferences.storeValue("meters", metersText.getText().toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeText.setText(Integer.toString(progress + 1));
                sharedPreferences.storeValue("mins", timeText.getText().toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setupCloseSessionButton ( View rootView){
        closeSessionBtn = (Switch) rootView.findViewById(R.id.btnCloseSession);
        closeSessionBtn.setChecked(false);
        closeSessionBtn.setEnabled(StringUtils.isNotBlank(user.getUsername()));
        closeSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCloseSession(activity, getString(R.string.close_session), getString(R.string.close_session_message));
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
