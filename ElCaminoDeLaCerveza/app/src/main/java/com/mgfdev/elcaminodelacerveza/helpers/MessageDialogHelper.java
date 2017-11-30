package com.mgfdev.elcaminodelacerveza.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

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
}
