package com.mgfdev.elcaminodelacerveza.services;

import java.util.Map;

/**
 * Created by Maxi on 28/10/2017.
 */

public class RestHelper {
    private static RestHelper instance;
    private RestHelper(){}

    public static RestHelper getInstance  (){
        if (instance == null){
            instance = new RestHelper();
        }
        return instance;
    }

    public String buildQueryString (Map<String, String> params){
        StringBuilder queryString = new StringBuilder();
        Integer index=0;
        int paramsQuantity = params.keySet().size();
        for (String key : params.keySet()){
            queryString.append(key +"="+params.get(key));
            queryString.append(++index <paramsQuantity ? "?": "");

        }
        return queryString.toString();
    }

}
