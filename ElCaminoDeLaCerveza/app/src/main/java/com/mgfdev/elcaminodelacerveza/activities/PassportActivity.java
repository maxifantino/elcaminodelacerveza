package com.mgfdev.elcaminodelacerveza.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mgfdev.elcaminodelacerveza.R;
import com.mgfdev.elcaminodelacerveza.dao.ServiceDao;
import com.mgfdev.elcaminodelacerveza.dto.Passport;
import com.mgfdev.elcaminodelacerveza.dto.User;
import com.mgfdev.elcaminodelacerveza.services.BrewerHelperService;

public class PassportActivity extends AppCompatActivity {

    private static final int QR_CODE_SCAN = 1;
    private Passport passport;
    private User user;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passport);
        user = (User) getIntent().getSerializableExtra("USER");
        ctx = this.getApplicationContext();
        passport = loadPassport(user);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            builder = new AlertDialog.Builder(this.getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this.getApplicationContext());
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
}
