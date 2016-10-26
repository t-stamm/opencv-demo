package com.catira.opencvdemo.services;

import java.util.List;

import com.catira.opencvdemo.model.BikeCalculationHistoryEntry;

/**
 * Created by Timo on 26.10.2016.
 */

public class BikeCalculationHistoryStorage {

    private List<BikeCalculationHistoryEntry> entries;

    public void addEntry(BikeCalculationHistoryEntry entry) {
        entries.add(entry);
    }

    public List<BikeCalculationHistoryEntry> getEntries() {
        return entries;
    }

    public void save() {
        // TODO save entries to android persistent storage
    }
}
