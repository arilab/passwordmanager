package com.laynet.passwordmanager;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.laynet.passwordmanager.Exceptions.CryptoException;
import com.laynet.passwordmanager.adapters.EntryArrayAdapter;
import com.laynet.passwordmanager.model.Entry;
import com.laynet.passwordmanager.persist.EntryPersistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class EntriesActivity extends AppCompatActivity {

    private List<Entry> entries = new ArrayList<>();
    private EntryArrayAdapter listAdapter;

    public static final int SAVE_ACTIVITY = 1;

    public static final int SAVE_RESULT = 1;
    public static final int DELETE_RESULT = 2;
    public static final int CANCEL_RESULT = 3;

    private final static String TAG_RETAINED_FRAGMENT = "retainedFragment";

    private RetainedFragment retainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new EntryOnClickListener(this, new Entry()));

        FragmentManager fm = getFragmentManager();
        retainedFragment = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        if (retainedFragment == null) {
            try {
                readEntriesFromFile();
            } catch (IOException e) {
                CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorEntries);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, R.string.fileloadfailed, Snackbar.LENGTH_LONG);
                snackbar.show();
            } catch (CryptoException e) {
                CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorEntries);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, R.string.fileloadfailed, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            retainedFragment = new RetainedFragment();
            fm.beginTransaction().add(retainedFragment, TAG_RETAINED_FRAGMENT).commit();
            retainedFragment.setData(entries);
        } else {
            entries.addAll(retainedFragment.getData());
        }
        mapEntriesToListView();
    }

    private void readEntriesFromFile() throws IOException, CryptoException {
        entries.addAll(new EntryPersistence().read(getApplicationContext()));
    }

    private void mapEntriesToListView() {
        listAdapter = new EntryArrayAdapter(this, android.R.layout.simple_list_item_1, entries);
        ListView listView = findViewById(R.id.entries);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entry selectedEntry = entries.get(position);
                Intent intent = new Intent(EntriesActivity.this, AddActivity.class);
                intent.putExtra(EntryOnClickListener.ENTRY_ID, selectedEntry);
                startActivityForResult(intent, SAVE_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == CANCEL_RESULT)
            return;

        if (resultCode == SAVE_RESULT) {
            Entry newEntry = intent.getParcelableExtra(EntryOnClickListener.ENTRY_ID);
            saveEntry(newEntry);
        }

        if (resultCode == DELETE_RESULT) {
            Entry entryToDelete = intent.getParcelableExtra(EntryOnClickListener.ENTRY_ID);
            deleteEntry(entryToDelete);
        }

        listAdapter.notifyDataSetChanged();
        retainedFragment.setData(entries);
        try {
            new EntryPersistence().write(getApplicationContext(), entries);
        } catch (IOException e) {
            CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorEntries);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, R.string.fileloadfailed, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void saveEntry(Entry newEntry) {
        if (newEntry.id == 0) {
            newEntry.id = entries.size() + 1;
            entries.add(newEntry);
        } else {
            for (Entry entry : entries) {
                if (entry.id == newEntry.id) {
                    entry.name = newEntry.name;
                    entry.login = newEntry.login;
                    entry.password = newEntry.password;
                    break;
                }
            }
        }
    }

    private void deleteEntry(Entry entryToDelete) {
        for (Iterator<Entry> it = entries.iterator(); it.hasNext();) {
            if (it.next().id == entryToDelete.id) {
                it.remove();
                break;
            }
        }
    }
}
