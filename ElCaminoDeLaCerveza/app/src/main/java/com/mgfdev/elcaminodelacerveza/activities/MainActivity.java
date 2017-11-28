package com.mgfdev.elcaminodelacerveza.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.services.LoginModule;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPostExecuteInterface {

    private CacheManagerHelper cacheHelper;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cacheHelper = CacheManagerHelper.getInstance();
        ctx = this.getApplicationContext();
        if (cacheHelper.getBrewers() == null) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    populateCache();
                }
            };
            r.run();
        }
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
        List<BeerLocation> breweries = (List<BeerLocation>)result;
        cacheHelper.addbrewersToCache(breweries);
        redirectLogin(ctx);
    }
}