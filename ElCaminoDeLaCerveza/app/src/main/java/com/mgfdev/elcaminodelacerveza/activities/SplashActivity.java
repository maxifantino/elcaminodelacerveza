package com.mgfdev.elcaminodelacerveza.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.AndroidCheckHelper;
import com.mgfdev.elcaminodelacerveza.helpers.ConnectionHelper;
import com.mgfdev.elcaminodelacerveza.services.LocalizationService;
import com.mgfdev.elcaminodelacerveza.services.LoginModule;

public class SplashActivity extends AppCompatActivity implements OnPostExecuteInterface {

    private ProgressBar mProgressView;
    private Context ctx;
    private Activity activity;
    private Integer totalImagesDownloaded = 0;
    private LoginModule module;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        module =  LoginModule.getInstance(this);
        setContentView(R.layout.activity_splash);
        ctx = this;
        mProgressView = (ProgressBar) findViewById(R.id.splash_progress);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                askLocationRights();
            }
        };
        r.run();
        showProgress(true);
        if (!AndroidCheckHelper.checkLocationPermission(ctx)){
            AndroidCheckHelper.requestLocationPermission(this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        setupapk(false);
    }


    private void askLocationRights(){
        boolean hasAccess = (ActivityCompat.checkSelfPermission(this.ctx, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED);

// hermoso bug de android. Si no esta definido, no lo ejecuta y no evalua el if de abajo.
        if (hasAccess){
            setupapk(false);
        }

    }

    private void setupapk(boolean retry){
        if (ConnectionHelper.isConnected(this)){
            BreweriesAsyncService breweriesAsyncService = new BreweriesAsyncService(this);
            breweriesAsyncService.execute((Void) null);
        }
        else if (retry){
            activity.finishAffinity();
        }
        else {
            showConnectDialog();
        }
    }

    private void showConnectDialog(){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(getString(R.string.notConnectedMessage));
        dlgAlert.setTitle("");
        dlgAlert.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setupapk(true);
            }
        });

        dlgAlert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

    }

    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void redirectLogin(Context ctx){
        // check if the user was logged before

        User user = module.getLoggedUser(ctx);
        if (user != null){ // dirijo a HomeModule
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra ("USER", user);
            startActivity(intent);
        } else{ // dirijo a loginModule
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onTaskCompleted(Object result) {
        redirectLogin(ctx);
    }

}