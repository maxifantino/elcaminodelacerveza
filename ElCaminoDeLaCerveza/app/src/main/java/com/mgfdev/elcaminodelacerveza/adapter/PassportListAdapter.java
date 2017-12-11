package com.mgfdev.elcaminodelacerveza.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.dto.Brewer;
import com.mgfdev.elcaminodelacerveza.helpers.DateHelper;

import java.util.List;

/**
 * Created by Maxi on 10/12/2017.
 */

public class PassportListAdapter extends ArrayAdapter<Brewer> {

    private int layoutResourceId;

    public PassportListAdapter(Context context, int layoutResourceId, List<Brewer> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(layoutResourceId, null);
        }

        Brewer p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.brewerNameItem);
            TextView tt2 = (TextView) v.findViewById(R.id.brewerNameItem);

            if (tt1 != null) {
                tt1.setText(p.getBrewerName());
            }

            if (tt2 != null) {
                tt2.setText(DateHelper.getDate(p.getDateCreated()));
            }
        }
        return v;
    }
}
