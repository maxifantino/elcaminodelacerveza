package com.mgfdev.elcaminodelacerveza.services;

import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;

import java.util.List;

/**
 * Created by Maxi on 12/11/2017.
 */

public class BrewerHelperService {
    public boolean isValidBrewer (String brewer, String username, String password){
        boolean result = false;
        List<String> brewers = CacheManagerHelper.getBrewers(username, password);
        return brewers.contains(brewer);
    }
}
