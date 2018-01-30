package com.mgfdev.elcaminodelacerveza.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.adapter.BrewerListAdapter;

import com.mgfdev.elcaminodelacerveza.data.BrewerInfo;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.CacheManagerHelper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrewerFragment extends CustomFragment {
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    private User user;
    private Uri imageUri;
    private Context ctx;
    private HomeActivity activity;
    private List<BrewerInfo> brewers;
    private String List;
    private ListView brewersListView;
    private BrewerListAdapter customAdapter;
    public BrewerFragment() {
        // Required empty public constructor
    }

    public static CustomFragment newInstance(String param1, String param2) {
        BrewerFragment fragment = new BrewerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx = getActivity().getApplicationContext();
        activity = (HomeActivity) getActivity();
        user = activity.getUser();
        brewers = CacheManagerHelper.getInstance().getBrewers();
        CollectionUtils.filter(brewers, new Predicate<BrewerInfo>() {
            @Override
            public boolean evaluate(BrewerInfo o) {
                return !o.isFiltered();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_brewer, container, false);
        populateListAdapter(rootView);

        //downloadImages();
        return rootView;
    }
/*
    private void downloadImages(){

        for (int index = 0; index < brewersListView.getCount(); index ++) {
            View v =  brewersListView.getAdapter().getView(index, null,brewersListView);
            ImageView icon1 = (ImageView) ((LinearLayout)v.findViewById(R.id.thumbnail)).getChildAt(0);
            downloadImage (icon1);
        }
    }

    private void downloadImage (ImageView img){
        String url =  img.getTag() != null?   img.getTag().toString(): StringUtils.EMPTY;
        DownloadImageTask task = new DownloadImageTask(img,R.dimen.brewer_thumb_height, R.dimen.brewer_thumb_width, url, ctx);
        task.execute();
    }
*/
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }

    public void populateListAdapter(View rootView){
        brewersListView = (ListView) rootView.findViewById(R.id.brewerListView);

        Collections.sort(brewers, new BrewerInfoComparator());

        customAdapter = new BrewerListAdapter(ctx, R.layout.brewer_list_row, brewers);
        brewersListView .setAdapter(customAdapter);

    }



    private class BrewerInfoComparator implements Comparator<BrewerInfo> {

        @Override
        public int compare(BrewerInfo o1, BrewerInfo o2) {
            return o1.getBrewery().compareTo(o2.getBrewery());
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
