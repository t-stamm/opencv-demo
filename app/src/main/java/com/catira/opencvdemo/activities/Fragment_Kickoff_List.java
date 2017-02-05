package com.catira.opencvdemo.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.catira.opencvdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_Kickoff_List extends Fragment {

    public Fragment_Kickoff_List(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            String [] kickofflisteArray = {
                "Optimierung 3",
                "Optimierung 3",
                "Optimierung 3"
        };

        List<String> kickoffListe = new ArrayList<>(Arrays.asList(kickofflisteArray));

        ArrayAdapter <String> kickofflisteAdapter =
                new ArrayAdapter<>(
                        getActivity(), // Die aktuelle Umgebung (diese Activity)
                        R.layout.list_item_kickoff, // ID der XML-Layout Datei
                        R.id.list_item_kickoff_textview, // ID des TextViews
                        kickoffListe); // Beispieldaten in einer ArrayList

        View rootView = inflater.inflate(R.layout.activity_kickoff, container, false);

        ListView kickofflisteListView = (ListView) rootView.findViewById(R.id.listview_kickoff);
        kickofflisteListView.setAdapter(kickofflisteAdapter);


        return rootView;

    }
}
