package com.mgfdev.elcaminodelacerveza.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Maxi on 02/12/2017.
 */

public class CustomFragment extends Fragment implements ActionObserver {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setLocationUpdates(Boolean activate, int retries) {

    }
}
