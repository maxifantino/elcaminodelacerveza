package com.mgfdev.elcaminodelacerveza.adapter;

import android.content.Context;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Maxi on 20/12/2017.
 */

public class CustomSwitchPreference extends SwitchPreferenceCompat {
        public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public CustomSwitchPreference(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomSwitchPreference(Context context) {
            super(context);
        }

        @Override
        public void onBindViewHolder(PreferenceViewHolder holder) {
            super.onBindViewHolder(holder);
            final TextView titleView = (TextView) holder.findViewById(android.R.id.title);
            titleView.setSingleLine(false);
        }


}
