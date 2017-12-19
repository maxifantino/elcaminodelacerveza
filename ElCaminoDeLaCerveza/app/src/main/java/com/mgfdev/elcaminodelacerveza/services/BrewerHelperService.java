package com.mgfdev.elcaminodelacerveza.services;

import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;

import java.util.List;

/**
 * Created by Maxi on 12/11/2017.
 */

public class BrewerHelperService {
    public boolean isValidBrewer (String brewer, String username, String password){
        List<BrewerInfo> brewerlies = CacheManagerHelper.getInstance().getBrewers(username, password);
        for (BrewerInfo location: brewerlies) {
            if (location.getBrewery() != null && location.getBrewery().equalsIgnoreCase(brewer)){
                return true;
            }
        }
        return false;
    }
}
