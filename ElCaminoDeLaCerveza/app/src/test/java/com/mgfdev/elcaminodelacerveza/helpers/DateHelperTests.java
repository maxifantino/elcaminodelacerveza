package com.mgfdev.elcaminodelacerveza.helpers;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by Maxi on 09/01/2018.
 */

public class DateHelperTests {

    @Test
    public void testIsNotInworkingHourMidnight(){
        DateTime dt = new DateTime().withHourOfDay(2);
        boolean result = DateHelper.isSameWorkingDay(dt.toDate());
        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsInworkingHourAfternoon(){
        DateTime dt = new DateTime().withHourOfDay(19);
        boolean result = DateHelper.isSameWorkingDay(dt.toDate());
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsNotInworkingHourAfternoon(){
        DateTime dt = new DateTime().withHourOfDay(19).minus(1);
        boolean result = DateHelper.isSameWorkingDay(dt.toDate());
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsInworkingHourMidnight(){
        DateTime dt = new DateTime().withHourOfDay(2).plusDays(1);
        boolean result = DateHelper.isSameWorkingDay(dt.toDate());
        Assert.assertEquals(true, result);
    }

}
