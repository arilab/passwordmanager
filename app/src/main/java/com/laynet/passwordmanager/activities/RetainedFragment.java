package com.laynet.passwordmanager.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.laynet.passwordmanager.model.Entry;

import java.util.ArrayList;
import java.util.List;

public class RetainedFragment extends Fragment {
    private final List<Entry> entries = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setData(List<Entry> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
    }

    public List<Entry> getData() {
        return entries;
    }
}
