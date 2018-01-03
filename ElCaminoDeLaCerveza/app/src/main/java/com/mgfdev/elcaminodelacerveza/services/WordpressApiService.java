package com.mgfdev.elcaminodelacerveza.services;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Splitter;
import com.google.common.util.concurrent.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgfdev.elcaminodelacerveza.adapter.PassportItemDTOAdapter;
import com.mgfdev.elcaminodelacerveza.dao.ServiceDao;
import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.dto.Passport;
import com.mgfdev.elcaminodelacerveza.dto.PassportItemDto;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.DateHelper;

import java.io.IOException;
import java.net.SocketException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maxi on 28/10/2017.
 */

public class WordpressApiService {

    private static final String LOGIN_URL = "https://www.elcaminodelacerveza.com/wp-json/custom-plugin/login-new?username={0}&password={1}";
    private static final String BREWERS_URL = "https://www.elcaminodelacerveza.com/wp-json/custom-plugin/breweries-list";
    private static final String PASSPORT_POST = "https://www.elcaminodelacerveza.com/wp-json/custom-plugin/update-passport";
    private static final String PASSPORT_GET = "https://www.elcaminodelacerveza.com/wp-json/custom-plugin/get-passport?username={0}&password={1}";

    private RestWebService restService;

    public WordpressApiService(){
        restService = RestWebService.getInstance();
    }

    public String doLogin (String user, String password){
        String loginResult ="";
        String formattedUrl =  MessageFormat.format(LOGIN_URL, user, password);
        try {
             loginResult = restService.doGet(formattedUrl, null);
        } catch (IOException e) {
            e.printStackTrace();
            loginResult = "{\"status_code\": \"500\"}";
        }
        return loginResult;
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

    public boolean postPassportNews (String nickname, String brewerName, Date createdDate){
        boolean result = false;
        Integer resultCount = 0;
        boolean success = false;
        while (resultCount < 3 && !success){
            try {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", nickname);
                        params.put("brewer", brewerName);
                params.put("date", DateHelper.getDateWordpress(createdDate));
                String saveResult = restService.doPost(PASSPORT_POST, params);
                result = "200".equals(saveResult);
                success = true;
            }
            catch (SocketException e){
                resultCount++;
                Log.e("postPassportNews", "SocketException . Error while saving passport " + e.getMessage());
            }
            catch (Exception e){
                success = true;
                Log.e("postPassportNews", "SocketException . Error while saving passport " + e.getMessage());
            }
        }
        return result;
    }

    private Map<String, String> buildPostParams(String op1, String op2, Date op3){
        Map <String, String> params = new HashMap<String, String>();
        params.put ("username", op1);
        params.put ("brewer", op2);
        params.put ("date",  DateHelper.getDateWordpress(op3));
        return params;
    }

    public boolean postPassportNewsManual (Context context, String username){
        ServiceDao dao = new ServiceDao();
        List<PassportItemDto> desincronizedIds = dao.getDesyncronizedItems(context, username);
        boolean finalResult = true;

        if (desincronizedIds == null || desincronizedIds!= null && desincronizedIds.size() == 0){
            return true;
        }

        for (PassportItemDto item: desincronizedIds) {
            boolean result = this.postPassportNews(username,item.getBrewerName(), item.getVisitDate());
            finalResult = finalResult && true;
            if (result){
                dao.sincronyzePassportItem(context, item.getPassportItem());
            }
        }
        return finalResult;
    }


    public Passport getPassportFromServer (String nickname, String password){
        // email + password
        Passport passport;
        String passportJSON;
        List<PassportItemDto> itemsDto = null;
        try {
            String formattedUrl =  MessageFormat.format(PASSPORT_GET, nickname, password);

            passportJSON = restService.doGet(formattedUrl, null);
            TypeToken<List<PassportItemDto>> token = new TypeToken<List<PassportItemDto>>(){};
            itemsDto = new Gson().fromJson(passportJSON, token.getType());
        }
        catch (Exception
                e){
            e.printStackTrace();
        }

        return PassportItemDTOAdapter.toPassport(itemsDto);
    }

}
