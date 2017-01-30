package com.catira.opencvdemo.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Html;

import com.catira.opencvdemo.R;

//vordefinierte AlertDialog Klasse angelegt mit max. drei Schaltflächen
public class Fragment_Setting_Help extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle b) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.title_ab_set));
        builder.setMessage (Html.fromHtml(getResources().getString(R.string.text_ab_set)));

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.setCancelable(false); //nicht schließen mit ZURÜCK-Button
        return builder.create();
    }
}
