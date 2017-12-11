package com.mgfdev.elcaminodelacerveza.adapter;

import com.mgfdev.elcaminodelacerveza.dto.Brewer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Maxi on 10/12/2017.
 */

public class PassportAdapter {

    public static List<Brewer> toBrewerList (Map<String, Date> brewers){
        List<Brewer> brewerList = new ArrayList<Brewer>();
        if (brewerList == null){
            return brewerList;
        }
        Set<String> keyset = brewers.keySet();
        Iterator<String> brewerIterator= keyset.iterator();
        for (;brewerIterator.hasNext();){
            String brewername = brewerIterator.next();
            Date createdDate = brewers.get(brewername);
            Brewer item = new Brewer(brewername,createdDate);
            brewerList.add(item);
        }
        return brewerList;
    }
}
