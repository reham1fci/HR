package com.max.rm.hr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class normalWindow {
     public static void window(String msg, Activity activity){
         final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
         alertDialog.setMessage(msg);
         alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 alertDialog.dismiss();
             }
         });
       alertDialog.show();
     }
    public static void dialogOkCancel(String message, final dialog_interface click, Activity activity){
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
       // alertDialog.setTitle("انتبه");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        click.onDialogOkClick(alertDialog);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                click.onDialogCancelClick(alertDialog);

            }
        });

        alertDialog.show();
    }
}
