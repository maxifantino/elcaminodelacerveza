package com.mgfdev.elcaminodelacerveza.helpers;

import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.services.WordpressApiService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maxi on 12/11/2017.
 */

public class CacheManagerHelper {

    private static Cache brewersCache = null;
    private static CacheManagerHelper instance;

    private CacheManagerHelper(){}

    public static CacheManagerHelper getInstance(){
        if (instance == null){
            instance = new CacheManagerHelper();
        }
        return instance;
    }

    private  void createBrewerCache(){
        CacheManager cacheManager = CacheManager.getInstance();
        int oneDay = 24 * 60 * 60;
        brewersCache = new Cache("name", 2000, false, false, oneDay, oneDay);
        cacheManager.addCache(brewersCache);
    }

    public List<BeerLocation> getBrewers(String username, String password){
        WordpressApiService service = new WordpressApiService();
        boolean itsInvalid = brewersCache != null ? !brewersCache.get(brewersCache.getKeys().get(0)).isExpired(): false;
        List<BeerLocation> beerLocations = null;
        if (itsInvalid){
            beerLocations = service.getBeerLocations(username, password);
            addbrewersToCache(beerLocations);
        }
        else{
            getBeerlocations();
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

    private void addbrewersToCache(List <BeerLocation> beerLocations){
        List<BeerLocation> brewers = new ArrayList<BeerLocation>();
        createBrewerCache();
        for (BeerLocation brewer:beerLocations){
            Element element = new Element(brewer.getCraftName(),brewer);
            brewersCache.put(element);
        }
    }
}
