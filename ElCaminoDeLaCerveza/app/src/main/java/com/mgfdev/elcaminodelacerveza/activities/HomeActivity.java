package com.mgfdev.elcaminodelacerveza.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.FontHelper;
import com.mgfdev.elcaminodelacerveza.helpers.MessageDialogHelper;
import com.mgfdev.elcaminodelacerveza.services.CustomCommand;
import com.mgfdev.elcaminodelacerveza.services.LocalizationService;
import com.mgfdev.elcaminodelacerveza.services.SharedPreferenceManager;

import static java.lang.Thread.sleep;

public class HomeActivity extends FragmentActivity implements ActionObserver {

    private User user;
    private SharedPreferenceManager sharedPreferences;
    private LocalizationService localizationService;
    private Context ctx;
    private Activity activity;
    private static final int CALL_SETTINGS = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            View layout;
            boolean result = false;
            setAllLayoutsInvisible();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    layout = findViewById(R.id.mapsLayout);
                    layout.setVisibility(View.VISIBLE);
                    result = true;
                    break;
                case R.id.navigation_passport:
                    layout = findViewById(R.id.passportLayout);
                    layout.setVisibility(View.VISIBLE);
                    result = true;
                    break;
                case R.id.navigation_settings:
                    layout = findViewById(R.id.settingsLayout);
                    layout.setVisibility(View.VISIBLE);
                    result = true;
                    break;
            }
            return result;
        }
    };

    public User getUser() {
        if (user == null) {
            user = (User) getIntent().getSerializableExtra("USER");
        }
        return user;
    }

    private void setAllLayoutsInvisible() {
        View layoutSeetings = findViewById(R.id.settingsLayout);
        layoutSeetings.setVisibility(View.INVISIBLE);
        View mapsLayout = findViewById(R.id.mapsLayout);
        mapsLayout.setVisibility(View.INVISIBLE);
        View passportLayout = findViewById(R.id.passportLayout);
        passportLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return ;
        }
        FontHelper.overrideFonts(this, findViewById(android.R.id.content)
                , "montserrat.ttf");

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ctx = this.getApplicationContext();
        user = (User) getIntent().getSerializableExtra("USER");
        sharedPreferences = SharedPreferenceManager.getInstance(this);
        setLocationUpdates(getLocationSwitch());
        setAllLayoutsInvisible();
        View layout = findViewById(R.id.mapsLayout);
        layout.setVisibility(View.VISIBLE);
        activity = this;
    }


    private boolean getLocationSwitch() {
        return Boolean.parseBoolean(sharedPreferences.getStringValue("location"));
    }

    @Override
    public void setLocationUpdates(Boolean activate) {
        localizationService = LocalizationService.getInstance(this);
        localizationService.init();
        if (activate) {
            boolean result = localizationService.isLocationActive();
            if (!result) {
                showYesNoDialog(this, getString(R.string.activateLocationTitle), getString(R.string.activateLocationDescription),
                        getString(R.string.activate), getString(R.string.cancel));
            }
        } else {
            localizationService.stop();
        }
    }

    private void showYesNoDialog(final Context ctx, String title, String description, String yesText, String noText) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ctx);
        dlgAlert.setMessage(description);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton(yesText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), CALL_SETTINGS);

            }
        });

        dlgAlert.setNegativeButton(noText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setLocationUpdates(false);
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return super.registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        setLocationUpdates(true);
    }

    @Override
    public void onBackPressed() {

        class CallbackNo implements CustomCommand{
            @Override
            public void execute() {
            }
        }
        class CallbackYes implements CustomCommand{
            @Override
            public void execute() {
                ActivityCompat.finishAffinity(activity);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        }
        MessageDialogHelper.showYesNoDialog(activity,getString(R.string.app_name), getString(R.string.exitMessage),getString(R.string.yes),
                getString(R.string.no),new CallbackYes(), new CallbackNo());


    }
}