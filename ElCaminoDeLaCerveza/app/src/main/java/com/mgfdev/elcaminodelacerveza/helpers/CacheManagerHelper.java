package com.mgfdev.elcaminodelacerveza.helpers;

import android.os.AsyncTask;

import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.dto.BrewerListItem;

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
    private static Cache brewersCacheList = null;
    private static CacheManagerHelper instance = null;

    private CacheManagerHelper(){

    }
    public static CacheManagerHelper getInstance(){
        if (instance == null){
            instance = new CacheManagerHelper();
        }
        return instance;
    }

    public  void createBrewerCacheAsync(List <BeerLocation> beerLocations) {
        CacheAsyncTask createCacheAsync = new CacheAsyncTask();
        createCacheAsync.execute();
    }

    public  void createBrewerCache() {
        createCache();
    }

    public  void createBrewerCache(List <BeerLocation> beerLocations) {
        createCache();
        addbrewersToCache(beerLocations);
    }

     public  void createBrewerList() {
        createCacheList();
    }

    public  void createBrewerListCache(List <BeerLocation> beerLocations) {
        createCacheList();
        addbrewersToCache(beerLocations);
    }

    private void createCache(){
        CacheManager cacheManager = CacheManager.getInstance();
        int twoDays = 2 * 24 * 60 * 60;
        brewersCache = new Cache("name", 2000, false, false, twoDays, twoDays);
        brewersCache.initialise();
    }

    private void createCacheList(){
        CacheManager cacheManager = CacheManager.getInstance();
        int twoDays = 2 * 24 * 60 * 60;
        brewersCacheList = new Cache("brewerList", 2000, false, false, twoDays, twoDays);
        brewersCacheList.initialise();
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



    public List<BrewerListItem> getBrewersList(String username, String password){
        return getBrewersList(null,null);
    }

    public List<BrewerListItem> getBrewersList(String username, String password){
        boolean itsValid = brewersCache != null && brewersCacheList.getKeys().size() > 0 ? !brewersCacheList.get(brewersCacheList.getKeys().get(0)).isExpired(): false;
        List <BrewerListItem> beerLocations = null;
        if (itsValid){
            beerLocations = getBrewersListItems();
        }
        return beerLocations;
    }

    private List <BrewerListItem> getBrewersListItems (){
        List <String> keys = brewersCache.getKeys();
        List <BrewerListItem> brewersList = new ArrayList<BrewerListItem>();
        for (String key: keys) {
            brewersList.add ((BrewerListItem)brewersCacheList.get(key).getObjectValue());
        }
        return brewersList;
    }


    public void addbrewersToCache(List <BeerLocation> beerLocations){
        if (brewersCache != null){
            for (BeerLocation location: beerLocations) {
                Element singleElement = new Element(location.getBrewery(), location);
                brewersCache.put(singleElement, false);
            }
        }
        else{
            createBrewerCacheAsync(beerLocations);
        }
    }
    public void addbrewersListToCache(List <BrewerListItem> brewers){
        if (brewersCache != null){
            for (BrewerListItem item: brewers) {
                Element singleElement = new Element(item.getBrewerName(),item );
                brewersCache.put(singleElement, false);
            }
        }
        else{
      //      createBrewerCacheAsync(brewers);
        }
    }
    class CacheAsyncTask extends AsyncTask<Void,Void,Void>{
        private List<BeerLocation> beerLocations;

        public CacheAsyncTask (List<BeerLocation> beerLocations){
            this.beerLocations = beerLocations;
        }

        public CacheAsyncTask () {

        }
            @Override
        protected Void doInBackground(Void... aVoid) {
            createCache();
            createCacheList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (beerLocations != null){
                for (BeerLocation brewer:beerLocations){
                    Element element = new Element(brewer.getBrewery(),brewer);
                    brewersCache.put(element);
                }
            }
        }
    }

}
