package com.mgfdev.elcaminodelacerveza.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.mgfdev.elcaminodelacerveza.data.BeerLocation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maxi on 28/10/2017.
 */

public class WordpressApiService {

    private static final String LOGIN_URL = "https://www.elcaminodelacerveza.com/wp-json/custom-plugin/login?username={0}password={1}";
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
    public List<BeerLocation> getBeerLocations (String user, String password){
        List<BeerLocation> locations = new ArrayList<>();
        String beerJson;
        try {
            def params = restService.getAuthenticationMap(user, password);
            beerJson = restService.doGet(formattedUrl, params);
            JsonElement yourJson = mapping.get("servers");
            Type listType = new TypeToken<List<String>>() {}.getType();

            List<String> yourList = new Gson().fromJson(yourJson, listType);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return locations;
    }
}
