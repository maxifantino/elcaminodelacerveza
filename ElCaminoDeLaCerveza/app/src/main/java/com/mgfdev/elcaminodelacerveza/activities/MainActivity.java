package com.mgfdev.elcaminodelacerveza.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.services.LocalizationService;
import com.mgfdev.elcaminodelacerveza.services.LoginModule;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPostExecuteInterface {

    private CacheManagerHelper cacheHelper;
    private Context ctx;
    private LocalizationService localizationService;
    private List<HomeActivity> observers = new ArrayList<HomeActivity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ctx = this.getApplicationContext();
        cacheHelper = CacheManagerHelper.getInstance();
        if (cacheHelper.getBrewers() == null) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    populateCache();
                }
            };
            r.run();
        }
        else {
            inflateLayout ();
        }
    }

    public void attachObserver (HomeActivity observer){
        observers.add(observer);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void inflateLayout (){
        setContentView(R.layout.activity_main);
    }

    private void populateCache (){
        BreweriesAsyncService breweriesAsyncService = new BreweriesAsyncService(this);
        breweriesAsyncService.execute((Void) null);
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
        inflateLayout();
        redirectLogin(ctx);
    }
}
