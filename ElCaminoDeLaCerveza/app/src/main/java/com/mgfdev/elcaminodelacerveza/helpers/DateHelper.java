package com.mgfdev.elcaminodelacerveza.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Maxi on 29/10/2017.
 */

public class DateHelper {

    private final static SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final static SimpleDateFormat dtwordpressFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static Date getDate (String stringDate){
        Date dt;
        try {
            dt = dtFormat.parse(stringDate);
        }
        catch (Exception e) {
            dt = new Date();
        }
        return dt;
    }

    public static String getDate (Date dt){
        return dtFormat.format(dt);
    }

    public static String getDateWordpress (Date dt){
        return dtwordpressFormat.format(dt);
    }
}
