package com.mgfdev.elcaminodelacerveza.services;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maxi on 28/10/2017.
 */

public class WordpressApiService {

    private static final String LOGIN_URL = "https://www.elcaminodelacerveza.com/wp-json/custom-plugin/login?username={0}password={1}"
    private RestWebService restService;

    public WordpressApiService(){
        restService = RestWebService.getInstance();
    }

    public boolean doLogin (String user, String password, String urlConnect){
        boolean result = false;
        String formattedUrl =  MessageFormat.format(LOGIN_URL, user, password);
        try {
            restService.doGet(formattedUrl, null);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    public boolean createPost (String user, String password, String comment){
        boolean result = false;
        try {
            restService.doGet(formattedUrl, null);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
    public boolean getBeerLocations (String user, String password){
        
    }
}
