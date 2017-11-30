package com.mgfdev.elcaminodelacerveza.services;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.mgfdev.elcaminodelacerveza.dao.ServiceDao;
import com.mgfdev.elcaminodelacerveza.dto.Passport;
import com.mgfdev.elcaminodelacerveza.dto.User;

import java.util.ArrayList;

/**
 * Created by Maxi on 29/11/2017.
 */

public class PassportService {

    public Passport loadPassport(Context ctx, User user){
        ServiceDao dao = new ServiceDao();
        return dao.getPassport(ctx, user.getId());
    }

}
