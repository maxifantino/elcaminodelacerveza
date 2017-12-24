package com.mgfdev.elcaminodelacerveza.helpers;

import android.os.AsyncTask;

import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;

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

    public  void createBrewerCacheAsync(List <BrewerInfo> brewerInfos) {
        CacheAsyncTask createCacheAsync = new CacheAsyncTask();
        createCacheAsync.execute();
    }

    public  void createBrewerCache() {
        createCache();
    }

    public  void createBrewerCache(List <BrewerInfo> brewerInfos) {
        createCache();
        addbrewersToCache(brewerInfos);
    }

    private void createCache(){
        CacheManager cacheManager = CacheManager.getInstance();
        int twoDays = 2 * 24 * 60 * 60;
        brewersCache = new Cache("name", 2000, false, false, twoDays, twoDays);
        brewersCache.initialise();
    }

    public List<BrewerInfo> getBrewers() {
        return getBrewers(null, null);
    }

    public List<BrewerInfo> getBrewers(String username, String password){
        boolean itsValid = brewersCache != null && brewersCache.getKeys().size() > 0 ? !brewersCache.get(brewersCache.getKeys().get(0)).isExpired(): false;
        List <BrewerInfo> brewerInfos = null;
        if (itsValid){
            brewerInfos = getBeerlocations();
        }
        return brewerInfos;
    }

    private List <BrewerInfo> getBeerlocations (){
        List <String> keys = brewersCache.getKeys();
        List <BrewerInfo> brewerInfos = new ArrayList<BrewerInfo>();
        for (String key: keys) {
            brewerInfos.add ((BrewerInfo)brewersCache.get(key).getObjectValue());
        }
        return brewerInfos;
    }

    public void addbrewersToCache(List <BrewerInfo> brewerInfos){
        if (brewersCache != null){
            for (BrewerInfo location: brewerInfos) {
                Element singleElement = new Element(location.getBrewery(), location);
                brewersCache.put(singleElement, false);
            }
        }
        else{
            createBrewerCacheAsync(brewerInfos);
        }
    }

    public BrewerInfo getBrewerById(String id){
        List<BrewerInfo> brewers = getBrewers();
        for (BrewerInfo item: brewers) {
            if (item.getBrewery().equalsIgnoreCase(id)){
                return item;
            }
        }
        return null;
    }

    class CacheAsyncTask extends AsyncTask<Void,Void,Void>{
        private List<BrewerInfo> brewerInfos;

        public CacheAsyncTask (List<BrewerInfo> brewerInfos){
            this.brewerInfos = brewerInfos;
        }

        public CacheAsyncTask () {

        }
            @Override
        protected Void doInBackground(Void... aVoid) {
            createCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (brewerInfos != null){
                for (BrewerInfo brewer: brewerInfos){
                    Element element = new Element(brewer.getBrewery(),brewer);
                    brewersCache.put(element);
                }
            }
        }
    }

}
