package com.mgfdev.elcaminodelacerveza.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Maxi on 05/12/2017.
 */

public class FontHelper {

    public void setFont(final Context ctx, TextView v, String fontName){
        Typeface myTypeface = Typeface.createFromAsset(v.getContext().getAssets(),fontName);
        if (v != null) {
            v.setTypeface(myTypeface);
        }
    }
    public static void overrideFonts(final Context context, final View v, String fontName) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child,fontName);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(),fontName));
            }
        } catch (Exception e) {
        }
    }
}
