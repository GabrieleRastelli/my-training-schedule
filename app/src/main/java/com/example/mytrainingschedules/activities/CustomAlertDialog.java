package com.example.mytrainingschedules.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Class that implements an alert dialog
 *
 * @author Gabriele Rastelli
 * @author Mattia Gualtieri
 */
public class CustomAlertDialog implements Runnable {

    private Context context;
    private String title, message;
    private DialogInterface.OnClickListener listenerPositive, listenerNegative;

    public CustomAlertDialog(Context context, String title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;
        this.listenerPositive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        };
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setListenerPositive(DialogInterface.OnClickListener listener) {
        this.listenerPositive = listener;
    }

    public void setListenerNegative(DialogInterface.OnClickListener listener) {
        this.listenerNegative = listener;
    }

    @Override
    public void run() {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("YES", listenerPositive)
                .setNegativeButton("NO", listenerNegative)
                .show();
    }
}
