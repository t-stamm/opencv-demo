package com.catira.opencvdemo.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.ListView;
import android.widget.TextView;

import com.catira.opencvdemo.R;


public class Fragment_Wheelsize extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(final Bundle b) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Reifengröße auswählen...");
        final String[] typen = new String[]{"571", "622", "630"};

        builder.setItems(typen, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                //aus der Liste den String abfangen
                ListView lw = ((AlertDialog) dialog).getListView();
                String checkedItem = lw.getAdapter().getItem(item).toString();

                //Auf das Layout in der zugehörigen Activity zugreifen und die TextView updaten
                TextView textFeld = (TextView) getActivity().findViewById(R.id.set_zoll);
                textFeld.setText(checkedItem);

                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
