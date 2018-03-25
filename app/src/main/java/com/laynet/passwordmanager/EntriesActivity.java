package com.laynet.passwordmanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.laynet.passwordmanager.adapters.EntryArrayAdapter;
import com.laynet.passwordmanager.model.Entry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EntriesActivity extends AppCompatActivity {

    private List<Entry> entries = new ArrayList<>();
    private EntryArrayAdapter listAdapter;

    public static final int SAVE_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new EntryOnClickListener(this, new Entry()));

        addItemsToListView();
    }

    private void addItemsToListView() {
        entries.add(new Entry(1, "Apple", "alainlay@me.com", "toto12"));
        entries.add(new Entry(2, "Google", "alainlay@gmail.com", "toto12"));
        entries.add(new Entry(3, "Facebook", "0211695668", "toto12"));
        entries.add(new Entry(4, "Amazon", "alainlay@gmail.com", "toto12"));
        entries.add(new Entry(5, "Twitter", "alainlay@hotmail.com", "toto12"));
        entries.add(new Entry(6, "Twitch", "milchh", "toto12"));
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
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == SAVE_ACTIVITY) {
            Entry newEntry = intent.getParcelableExtra(EntryOnClickListener.ENTRY_ID);
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

        listAdapter.notifyDataSetChanged();
    }

    private void createFileIfMissing() {
        File f = new File("passwords.txt");
        if (!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
