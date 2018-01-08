package com.mgfdev.elcaminodelacerveza.services;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgfdev.elcaminodelacerveza.dao.ServiceDao;
import com.mgfdev.elcaminodelacerveza.dto.User;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maxi on 29/10/2017.
 */

public class LoginModule {
    private WordpressApiService service;
    private Context context;
    private static User user;

    private static LoginModule instance ;

    public static LoginModule getInstance(Context ctx){
        if (instance == null){
            instance = new LoginModule(ctx);
        }
        return instance;
    }

    private LoginModule(Context context){
        service = new WordpressApiService();
        this.context = context;
    }
    public boolean execute(String username, String password) throws Exception{
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) ){
            throw (new RuntimeException("Username/Password could not be empty"));
        }
        String result = service.doLogin(username, password);

        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> tokens = new Gson().fromJson(result, mapType);
        boolean loginResult = "200".equals(tokens.get("status_code"));
        if (loginResult){
            Map<String, String> userTokens = (Map<String, String>) tokens.get("message");
            user = new User(userTokens.get("nickname"), userTokens.get("email"), password, null);
        }
        return loginResult;
    }


    public void markUserAsLogged(Integer id){
        ServiceDao serviceDao = new ServiceDao();
        user.setId(id);
        try{
            serviceDao.markUserAsLogged(context, id);
        }
        catch (Exception e){
            Log.e("LoginModule", "Error while marking user as logged" + e.getMessage());
        }
    }


    public User isInDB (String username){
        ServiceDao serviceDao = new ServiceDao();
        return serviceDao.getUser(context, username);
    }

    public User getLoggedUser (){
       return user;
    }

    public User getLoggedUser (Context ctx){
        ServiceDao serviceDao = new ServiceDao();
        return serviceDao.getLoggedUser(context);
    }

    public void saveInDb (){
        ServiceDao serviceDao = new ServiceDao();
        serviceDao.saveUser(context, user);
    }

    public boolean doLogout(Context ctx, User user){
        ServiceDao dao = new ServiceDao();
        return dao.doLogout(ctx, user);
    }
}
