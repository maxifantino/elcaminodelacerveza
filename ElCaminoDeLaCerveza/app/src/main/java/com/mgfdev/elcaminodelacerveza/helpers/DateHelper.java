package com.mgfdev.elcaminodelacerveza.helpers;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maxi on 29/10/2017.
 */

public class DateHelper {

    private final static SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final static SimpleDateFormat dtwordpressFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

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

    public static Date getDateWordpress (String stringDate){
        Date dt;
        try {
            dt = dtwordpressFormat.parse(stringDate);
        }
        catch (Exception e) {
            dt = new Date();
        }
        return dt;
    }

    /* Debo verificar si dt1 y dt2 ocurrieron entre las 18 y las 06 */
    public static boolean isSameWorkingDay (Date dt1){

        // establezco los limites en base a systemDate
        DateTime startLimit =  new DateTime();
        DateTime endDate =  new DateTime();
        DateTime inputDate = new DateTime(dt1);
        if (startLimit.getHourOfDay() < 6){
            startLimit = startLimit.minusDays(1).withHourOfDay(18);
            endDate =endDate.withHourOfDay(6);
        } else{
            endDate = endDate.plusDays(1).withHourOfDay(6);
            startLimit = startLimit.withHourOfDay(18);
        }
        return inputDate.isAfter(startLimit) && inputDate.isBefore(endDate);
    }
}
