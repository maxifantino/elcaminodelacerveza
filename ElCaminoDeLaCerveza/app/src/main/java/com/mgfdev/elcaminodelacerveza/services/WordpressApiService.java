package com.mgfdev.elcaminodelacerveza.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxi on 28/10/2017.
 */

public class WordpressApiService {

    private static final String LOGIN_URL = "https://www.elcaminodelacerveza.com/wp-json/custom-plugin/login?username={0}&password={1}";
    private static final String BREWERS_URL = "https://www.elcaminodelacerveza.com/wp-json/custom-plugin/breweries-list";
    private RestWebService restService;

    public WordpressApiService(){
        restService = RestWebService.getInstance();
    }

    public boolean doLogin (String user, String password){
        boolean result = false;
        String formattedUrl =  MessageFormat.format(LOGIN_URL, user, password);
        try {
            String loginResult = restService.doGet(formattedUrl, null);
            result = "200".equals(loginResult);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
 /*   public boolean createPost (String user, String password, String comment){
        boolean result = false;
        try {
            restService.doGet(formattedUrl, null);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }*/
    public List<BrewerInfo> getBeerLocations (){
        List<BrewerInfo> locations = new ArrayList<>();
        String beerJson;
        try {
            beerJson = restService.doGet(BREWERS_URL, null);
            TypeToken<List<BrewerInfo>> token = new TypeToken<List<BrewerInfo>>(){};
            locations = new Gson().fromJson(beerJson, token.getType());
        }
        catch (Exception
                e){
            e.printStackTrace();
        }
        return locations;
    }


}
