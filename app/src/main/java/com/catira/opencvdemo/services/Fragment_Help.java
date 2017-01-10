package com.catira.opencvdemo.services;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.catira.opencvdemo.R;

//vordefinierte AlertDialog Klasse angelegt mit max. drei Schaltflächen
public class Fragment_Help extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle b) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.title_ab_help));
        builder.setMessage(getString(R.string.text_ab_help));

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });

        builder.setCancelable(false); //nicht schließen mit ZURÜCK-Button
        return builder.create();
    }
}