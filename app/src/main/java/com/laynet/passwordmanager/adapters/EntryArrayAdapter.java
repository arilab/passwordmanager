package com.laynet.passwordmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.laynet.passwordmanager.model.Entry;

import java.util.List;

/**
 * Created by alain on 20/03/2018.
 */

public class EntryArrayAdapter extends ArrayAdapter<Entry> {

    private int layoutResource;

    public EntryArrayAdapter(Context context, int layoutResource, List<Entry> entries) {
        super(context, layoutResource, entries);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        Entry entry = getItem(position);

        if (entry != null) {
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(entry.name);
        }

        return view;
    }
}

