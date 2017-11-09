package com.mgfdev.elcaminodelacerveza.services;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maxi on 28/10/2017.
 */

public class WordpressApiService {

    private RestWebService restService;
    public WordpressApiService(){
        restService = RestWebService.getInstance();
    }
    public boolean doLogin (String user, String password, String urlConnect){
        boolean result = false;
        Map authentication = restService.getAuthenticationMap(user, password);
        return result;
    }

}
