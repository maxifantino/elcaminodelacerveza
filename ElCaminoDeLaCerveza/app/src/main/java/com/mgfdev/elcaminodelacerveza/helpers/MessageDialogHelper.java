package com.mgfdev.elcaminodelacerveza.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.mgfdev.elcaminodelacerveza.services.CustomCommand;

/**
 * Created by Maxi on 29/11/2017.
 */

public class MessageDialogHelper {
    public static void showErrorMessage(Context ctx, String title, String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ctx);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }


    public static void showErrorMessage(Activity act, String title, String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(act);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    public static void showYesNoDialog(final Context ctx, String title, String description, String yesText, String noText,
                                final CustomCommand callbackYes, final CustomCommand callbackNo) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ctx);
        dlgAlert.setMessage(description);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton(yesText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callbackYes.execute();
            }
        });

        dlgAlert.setNegativeButton(noText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callbackNo.execute();
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }




}
