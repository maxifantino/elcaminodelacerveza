package com.mgfdev.elcaminodelacerveza.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.dto.PassportItem;
import com.mgfdev.elcaminodelacerveza.helpers.DateHelper;

import java.util.List;

/**
 * Created by Maxi on 10/12/2017.
 */

public class PassportListAdapter extends ArrayAdapter<PassportItem> {

    private int layoutResourceId;
    public PassportListAdapter(Context context, int layoutResourceId, List<PassportItem> items) {
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

        PassportItem p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.brewerNameText);
            TextView tt2 = (TextView) v.findViewById(R.id.createdDateText);

            if (tt1 != null) {
                tt1.setText(p.getBrewerName());
            }

            if (tt2 != null) {
                SpannableStringBuilder str = new SpannableStringBuilder("Visitas: " + p.getVisitsCount() +" . Última: " + DateHelper.getDate(p.getLastVisit()));
                str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 7,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tt2.setText(str );
            }
        }
        return v;
    }
}
