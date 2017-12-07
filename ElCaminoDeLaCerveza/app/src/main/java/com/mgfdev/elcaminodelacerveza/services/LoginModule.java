package com.mgfdev.elcaminodelacerveza.services;

import android.content.Context;

import com.mgfdev.elcaminodelacerveza.dao.ServiceDao;
import com.mgfdev.elcaminodelacerveza.dto.User;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maxi on 29/10/2017.
 */

public class LoginModule {
    private WordpressApiService service;
    private Map<String, String> credentials = new HashMap<String, String>();
    private Context context;

    public LoginModule(Context context){
        service = new WordpressApiService();
        this.context = context;
    }

    public boolean execute(String username, String password) throws Exception{
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) ){
            throw (new RuntimeException("Username/Password could not be empty"));
        }
        boolean result = service.doLogin(username, password);
        if (result){
            credentials.clear();
            credentials.put(username, password);
        }
        return result;
    }

    public void saveCredentials (String username, String password){
        ServiceDao serviceDao = new ServiceDao();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        serviceDao.saveUser(context, user);
    }

    public User getLoggedUser (Context ctx){
        ServiceDao serviceDao = new ServiceDao();
        User user = serviceDao.getLoggedUser(ctx);

        return user;
    }

    public boolean doLogout(Context ctx, User user){
        ServiceDao dao = new ServiceDao();
        return dao.doLogout(ctx, user);
    }
}
