package com.mgfdev.elcaminodelacerveza.helpers;

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

    private static void createBrewerCache(){
        CacheManager cacheManager = CacheManager.getInstance();
        int oneDay = 24 * 60 * 60;
        brewersCache = new Cache("name", 2000, false, false, oneDay, oneDay);
        cacheManager.addCache(brewersCache);
    }

    public static List<String> getBrewers(String username, String password){
        WordpressApiService service = new WordpressApiService();
        boolean itsInvalid = brewersCache != null ? !brewersCache.get(brewersCache.getKeys().get(0)).isExpired(): false;
        List<String> brewers = null;
        if (itsInvalid){
            brewers = brewersCache.getKeys();
        }
        else{
            List <BeerLocation> beerLocations = service.getBeerLocations(username, password);
            addbrewersToCache(beerLocations);
        }
        return brewers;
    }

    private static void addbrewersToCache(List <BeerLocation> beerLocations){
        List<String> brewers = new ArrayList<String>();
        createBrewerCache();
        for (BeerLocation brewer:beerLocations){
            Element element = new Element(brewer.getCraftName(),brewer.getCraftName());
            brewersCache.put(element);
        }
    }
}
