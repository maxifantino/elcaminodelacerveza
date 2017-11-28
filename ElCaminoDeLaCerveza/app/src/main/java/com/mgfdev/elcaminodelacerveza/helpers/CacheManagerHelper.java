package com.mgfdev.elcaminodelacerveza.helpers;

import android.os.AsyncTask;

import com.mgfdev.elcaminodelacerveza.activities.OnPostExecuteInterface;
import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.services.WordpressApiService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxi on 12/11/2017.
 */

public class CacheManagerHelper {

    private static Cache brewersCache = null;
    private static CacheManagerHelper instance = null;

    private CacheManagerHelper(){

    }
    public static CacheManagerHelper getInstance(){
        if (instance == null){
            instance = new CacheManagerHelper();
        }
        return instance;
    }

    private  void createBrewerCache(List <BeerLocation> beerLocations) {
        CacheAsyncTask createCacheAsync = new CacheAsyncTask(beerLocations);

        createCacheAsync.execute();
    }

    private void createCacheAsync(){git status
            
        CacheManager cacheManager = CacheManager.getInstance();
        int oneDay = 24 * 60 * 60;
        brewersCache = new Cache("name", 2000, false, false, oneDay, oneDay);
        cacheManager.addCache(brewersCache);
    }

    public List<BeerLocation> getBrewers() {
        return getBrewers(null, null);
    }

    public List<BeerLocation> getBrewers(String username, String password){
        boolean itsValid = brewersCache != null && brewersCache.getKeys().size() > 0 ? !brewersCache.get(brewersCache.getKeys().get(0)).isExpired(): false;
        List <BeerLocation> beerLocations = null;
        if (itsValid){
            beerLocations = getBeerlocations();
        }
        return beerLocations;
    }

    private List <BeerLocation> getBeerlocations (){
        List <String> keys = brewersCache.getKeys();
        List <BeerLocation> beerLocations = new ArrayList<BeerLocation>();
        for (String key: keys) {
            beerLocations.add ((BeerLocation)brewersCache.get(key).getObjectValue());
        }
        return beerLocations;
    }

    public void addbrewersToCache(List <BeerLocation> beerLocations){
        for (BeerLocation location: beerLocations) {
            Element singleElement = new Element(location.getBrewery(), location);
//// VER ACA QUE mIERDA PASO. no lo creo el hijo de re mil putas.

            brewersCache.put(singleElement, false);
        }
    }

    class CacheAsyncTask extends AsyncTask<Void,Void,Void>{
        private List<BeerLocation> beerLocations;

        public CacheAsyncTask (List<BeerLocation> beerLocations){
            this.beerLocations = beerLocations;
        }

        @Override
        protected Void doInBackground(Void... aVoid) {
            createCacheAsync();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for (BeerLocation brewer:beerLocations){
                Element element = new Element(brewer.getBrewery(),brewer);
                brewersCache.put(element);
            }
        }
    }

}
