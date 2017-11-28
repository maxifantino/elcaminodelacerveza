package com.mgfdev.elcaminodelacerveza.services;

import com.mgfdev.elcaminodelacerveza.data.BeerLocation;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;

import java.util.List;

/**
 * Created by Maxi on 12/11/2017.
 */

public class BrewerHelperService {
    public boolean isValidBrewer (String brewer, String username, String password){
        List<BeerLocation> brewerlies = CacheManagerHelper.getInstance().getBrewers(username, password);
        for (BeerLocation location: brewerlies) {
            if (location.getBrewery() != null && location.getBrewery().equals(brewer)){
                return true;
            }
        }
        return false;
    }
}
