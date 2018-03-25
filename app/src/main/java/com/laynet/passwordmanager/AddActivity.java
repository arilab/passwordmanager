package com.laynet.passwordmanager;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.laynet.passwordmanager.model.Entry;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {setResult(RESULT_CANCELED); finish();}
        });

        Button addButton = findViewById(R.id.addButton);
        addButton.setTag(0);
        final Entry entry = getIntent().getParcelableExtra(EntryOnClickListener.ENTRY_ID);
        if (entry.id != 0) {
            addButton.setText(R.string.edit);
            addButton.setTag(entry.id);
            displayEntryDetails(entry);
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEntry(createEntryFromDetails());
            }
        });
    }

    private void displayEntryDetails(Entry entry) {
        EditText name = findViewById(R.id.name);
        name.setText(entry.name);
        EditText login = findViewById(R.id.login);
        login.setText(entry.login);
        EditText password = findViewById(R.id.password);
        password.setText(entry.password);
    }

    private Entry createEntryFromDetails() {
        EditText name = findViewById(R.id.name);
        EditText login = findViewById(R.id.login);
        EditText password = findViewById(R.id.password);
        Button addButton = findViewById(R.id.addButton);
        return new Entry((int)addButton.getTag(), name.getText().toString(), login.getText().toString(), password.getText().toString());
    }

    private void saveEntry(Entry entry) {
        Intent output = new Intent();
        output.putExtra(EntryOnClickListener.ENTRY_ID, entry);
        setResult(RESULT_OK, output);
        finish();
    }
}
