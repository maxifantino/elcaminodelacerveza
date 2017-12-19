package com.mgfdev.elcaminodelacerveza.activities;

import android.os.AsyncTask;

import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;
import com.mgfdev.elcaminodelacerveza.services.WordpressApiService;

import java.util.List;

/**
 * Created by Maxi on 25/11/2017.
 */

public class BreweriesAsyncService extends AsyncTask<Object, Object, Object> {

    public OnPostExecuteInterface delegate = null;
    private List<BrewerInfo> locations;
    public BreweriesAsyncService(OnPostExecuteInterface asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected Object doInBackground(Object... params) {
        WordpressApiService service = new WordpressApiService();
        locations = service.getBeerLocations();
       // brewerList = service.getBrewerList();
        CacheManagerHelper instance = CacheManagerHelper.getInstance();
        instance.createBrewerCache(locations);
     //   instance.createBrewerList (brewerList);
        return locations;
    }

    @Override
    protected void onPostExecute(Object result) {

        delegate.onTaskCompleted(locations);
    }

}
