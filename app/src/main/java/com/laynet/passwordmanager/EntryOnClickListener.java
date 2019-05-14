package com.laynet.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.laynet.passwordmanager.model.Entry;

/**
 * Created by alain on 20/03/2018.
 */

public class EntryOnClickListener implements View.OnClickListener {

    public static final String ENTRY_ID = "com.laynet.passwordmanager.ENTRYID";

    private final Entry entry;
    private Activity activity;

    public EntryOnClickListener(Activity activity, Entry entry) {
        this.entry = entry;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(activity, AddActivity.class);
        intent.putExtra(ENTRY_ID, entry);
        this.activity.startActivityForResult(intent, EntriesActivity.SAVE_ACTIVITY);
    }
}
