package com.catira.opencvdemo.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.ListView;
import android.widget.TextView;

import com.catira.opencvdemo.R;
import com.catira.opencvdemo.model.CyclingPosition;
import com.catira.opencvdemo.model.MeasurementContext;


public class Fragment_Setting extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(final Bundle b) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.cycling_position_title));

        int[] resourceIds = CyclingPosition.getRessouces();
        final String[] typen = new String[resourceIds.length];

        for(int i = 0; i < resourceIds.length; i++) {
            typen[i] = getString(resourceIds[i]);
        }

        builder.setItems(typen, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                //aus der Liste den String abfangen
                ListView lw = ((AlertDialog) dialog).getListView();
                String checkedItem = lw.getAdapter().getItem(item).toString();

                //Auf das Layout in der zugehörigen Activity zugreifen und die TextView updaten
                TextView textFeld = (TextView) getActivity().findViewById(R.id.set_pos);
                textFeld.setText(checkedItem);
                MeasurementContext.currentCyclingPosition = CyclingPosition.fromString(checkedItem);
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}