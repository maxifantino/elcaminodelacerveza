package com.mgfdev.elcaminodelacerveza.adapter;

import com.mgfdev.elcaminodelacerveza.dto.Passport;
import com.mgfdev.elcaminodelacerveza.dto.PassportItemDto;
import com.mgfdev.elcaminodelacerveza.helpers.DateHelper;

import java.util.List;

/**
 * Created by Maxi on 02/01/2018.
 */

public class PassportItemDTOAdapter {
    public static  Passport toPassport(List<PassportItemDto> items){
        Passport passport = new Passport();
        if (items != null){
            for (PassportItemDto item: items) {
                passport.addBrewer(item.getBrewerName(), DateHelper.getDateWordpress(item.getDateVisit()));
            }
        }

        return passport;
    }
}
