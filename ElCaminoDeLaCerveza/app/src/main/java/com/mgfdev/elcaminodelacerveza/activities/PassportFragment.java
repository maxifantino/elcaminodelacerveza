package com.mgfdev.elcaminodelacerveza.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListViewCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.mgfdev.elcaminodelacerveza.BuildConfig;
import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.adapter.PassportAdapter;
import com.mgfdev.elcaminodelacerveza.adapter.PassportListAdapter;
import com.mgfdev.elcaminodelacerveza.dao.ServiceDao;
import com.mgfdev.elcaminodelacerveza.dto.Brewer;
import com.mgfdev.elcaminodelacerveza.dto.Passport;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.helpers.FontHelper;
import com.mgfdev.elcaminodelacerveza.helpers.MessageDialogHelper;
import com.mgfdev.elcaminodelacerveza.provider.IntentIntegrator;
import com.mgfdev.elcaminodelacerveza.provider.IntentResult;
import com.mgfdev.elcaminodelacerveza.services.BrewerHelperService;
import com.mgfdev.elcaminodelacerveza.services.PassportService;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PassportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PassportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassportFragment extends CustomFragment{
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    private Button scanBreweButton;
    private ListViewCompat passportList;
    private Passport passport;
    private List<String> passportStringList;
    private ArrayAdapter<String> adapter;
    private static final int QR_CODE_SCAN = 1;
    private static final int PHOTO_REQUEST = 10;
    private HomeActivity activity;
    private User user;
    private BarcodeDetector barcodeDetector;
    private PassportService passportService;
    private Uri imageUri;
    private Context ctx;
    private ListView passportListView;
    PassportListAdapter customAdapter;
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

        passportService = new PassportService();
        ctx = getActivity().getApplicationContext();
        activity = (HomeActivity) getActivity();
        user = activity.getUser();
        activity.attachPassportObserver(this);
        if (user != null){
            passport = passportService.loadPassport(ctx, user);
            populatePassportList(passport);
        }

        initializeQRDetector();
    }

    public void populateListAdapter(View rootView){
        passportListView = (ListView) rootView.findViewById(R.id.passportListView);

        List<Brewer> brewers = PassportAdapter.toBrewerList(passport.getBrewers());
        customAdapter = new PassportListAdapter(ctx, R.layout.passport_list_row, brewers);
        passportListView .setAdapter(customAdapter);
    }

    private void initializeQRDetector(){
        barcodeDetector = new BarcodeDetector.Builder(ctx)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
    }

    private void processQRResult(String brewerResult){
        BrewerHelperService helperService = new BrewerHelperService();
        boolean isValid = helperService.isValidBrewer(brewerResult, activity.getUser().getUsername(), activity.getUser().getPassword());
        if (isValid && notInPassport(brewerResult)){
            addBrewerToPassport(brewerResult);
            passportStringList.add(brewerResult);
            adapter.notifyDataSetChanged();
        } else{
            MessageDialogHelper
                    .showErrorMessage(activity,getString(R.string.brewer_not_found_title), MessageFormat.format(getString(R.string.brewer_not_found_message), brewerResult ));
        }
    }
    private boolean notInPassport(String brewer) {
        checkPassport();
        return !passport.getBrewers().keySet().contains(brewer);
    }

    private void checkPassport(){
        if (passport == null){
            populatePassport();
        }
    }
    private void addBrewerToPassport(String brewer){
        checkPassport();
        passport.addBrewer(brewer);
        ServiceDao dao = new ServiceDao();
        dao.savePassportItem(ctx, user.getId(), brewer);
        customAdapter.add(new Brewer(brewer, new Date()));
        customAdapter.notifyDataSetChanged();
    }

    private void populatePassport (){
        user = activity.getUser();
        passport = passportService.loadPassport(ctx, user);
        if (passportList == null){
            populatePassportList(passport);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_passport, container, false);
        setupScanButton(rootView);
        FontHelper.overrideFonts(ctx, container
                , "montserrat.ttf");
        populateListAdapter(rootView);
        //passportTextView.setText(MessageFormat.format(getString(R.string.passportbelongstitle), user.getUsername()));
        return rootView;
    }
    private void setupScanButton(View rootView){
        scanBreweButton = (Button) rootView.findViewById(R.id.addBrewer);
        scanBreweButton.setTextColor (getResources().getColor(R.color.darkWhite, getActivity().getTheme()));
        scanBreweButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQr();
            }
        });
        scanBreweButton.setOnLongClickListener( new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), getString(R.string.passport_title_message), Toast.LENGTH_SHORT).show();
                return true;
            }

        });
    }

    private void getQr(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }



    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result
            processQRResult(scanResult.getContents());
        }
        // else continue with any other code you need in the metho
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

    private void populatePassportList (Passport passport){
        passportStringList = new ArrayList<String>();
        for (String brewer : passport.getBrewers().keySet()){
            passportStringList.add (brewer);
        }
        adapter=new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1,
                passportStringList);
    }

}
