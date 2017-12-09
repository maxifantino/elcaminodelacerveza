package com.mgfdev.elcaminodelacerveza.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.ConnectionHelper;
import com.mgfdev.elcaminodelacerveza.services.LocalizationService;
import com.mgfdev.elcaminodelacerveza.services.LoginModule;

public class SplashActivity extends AppCompatActivity implements OnPostExecuteInterface {

    private ProgressBar mProgressView;
    private Context ctx;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_splash);
        ctx = this;
        mProgressView = (ProgressBar) findViewById(R.id.splash_progress);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                populateCache();
            }
        };
        r.run();
        showProgress(true);
    }

    private void populateCache(){
        if (ConnectionHelper.isConnected(this)){
            BreweriesAsyncService breweriesAsyncService = new BreweriesAsyncService(this);
            breweriesAsyncService.execute((Void) null);
        }
        else{
            showConnectDialog();
        }
    }

    private void showConnectDialog(){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(getString(R.string.notConnectedMessage));
        dlgAlert.setTitle("");
        dlgAlert.setPositiveButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                populateCache();
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
        LoginModule module = new LoginModule(ctx);
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