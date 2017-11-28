package com.mgfdev.elcaminodelacerveza.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.dao.ServiceDao;
import com.mgfdev.elcaminodelacerveza.dto.Passport;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.services.BrewerHelperService;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PassportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PassportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassportFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    private Button scanBreweButton;
    private ListViewCompat passportList;
    private Passport passport;
    private List<String> passportStringList;
    private ArrayAdapter<String> adapter;
    private static final int QR_CODE_SCAN = 1;
    private Context ctx;
    private HomeActivity activity;
    private User user;
    public PassportFragment() {
        // Required empty public constructor
    }

    public static PassportFragment newInstance(String param1, String param2) {
        PassportFragment fragment = new PassportFragment();
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
    }


    private void populatePassportList (Passport passport){
        passportStringList = new ArrayList<String>();
        for (String brewer : passport.getBrewers().keySet()){
            passportStringList.add (brewer);
        }
        adapter=new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1,
                passportStringList);
    }

    private Passport loadPassport(User user){
        ServiceDao dao = new ServiceDao();
        return dao.getPassport(ctx, user.getId());
    }

    private void getQr() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, QR_CODE_SCAN);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case QR_CODE_SCAN:
                processQRResult(data);
                break;
            default:
                break;
        }
    }


    private void processQRResult(Intent data){
        String brewerResult = data.getStringExtra("SCAN_RESULT");
        BrewerHelperService helperService = new BrewerHelperService();
        if (helperService.isValidBrewer(brewerResult, user.getUsername(), user.getPassword())){
            addBrewerToPassport(brewerResult);
            passportStringList.add(brewerResult);
            adapter.notifyDataSetChanged();

        } else{
            showErrorMessage(brewerResult);
        }
    }

    private void addBrewerToPassport(String brewer){
        passport.addBrewer(brewer);
        ServiceDao dao = new ServiceDao();
        dao.savePassportItem(ctx, user.getId(), brewer);


    }

    private void showErrorMessage(String brewerName){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ctx, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ctx);
        }
        builder.setTitle(getString(R.string.brewer_not_found_title))
                .setMessage(java.text.MessageFormat.format(getString(R.string.brewer_not_found_title), brewerName))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_passport, container, false);

        scanBreweButton = (Button) rootView.findViewById(R.id.addBrewer);
        scanBreweButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQr();
            }
        });

        return rootView;
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
