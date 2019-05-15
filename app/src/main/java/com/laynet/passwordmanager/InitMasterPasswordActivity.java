package com.laynet.passwordmanager;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.laynet.passwordmanager.model.Entry;
import com.laynet.passwordmanager.persist.EntryPersistence;
import com.laynet.passwordmanager.persist.FileSystem;
import com.laynet.passwordmanager.security.MasterPassword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitMasterPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_master_password);

        Button initializeButton = findViewById(R.id.initialize);
        initializeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newPasswordEdit = findViewById(R.id.newPassword);
                String newPassword = newPasswordEdit.getText().toString();
                EditText retypePasswordEdit = findViewById(R.id.retypePassword);
                String retypePassword = retypePasswordEdit.getText().toString();

                if (validatePassword(newPassword, retypePassword)) {
                    removeWrongPasswordError();

                    MasterPassword.getInstance().setPassword(newPassword);

                    new FileSystem().deleteFilestore(getApplicationContext());

                    List<Entry> entries = new ArrayList<>();
                    Entry entry = new Entry(1, "Password Manager", "Me", "password");
                    entries.add(entry);
                    try {
                        new EntryPersistence().write(getApplicationContext(), entries);
                    } catch (IOException e) {
                        Snackbar snackbar = Snackbar
                                .make(view, R.string.filewritefailed, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                    Intent output = new Intent();
                    setResult(MainActivity.INIT_RESULT, output);
                    finish();
                } else {
                    displayWrongPasswordError();
                }
            }
        });
    }

    private boolean validatePassword(String newPassword, String retypePassword) {
        if (newPassword == null || newPassword.trim().equals(""))
            return false;
        if (retypePassword == null || retypePassword.trim().equals(""))
            return false;
        return newPassword.equals(retypePassword);
    }

    private void displayWrongPasswordError() {
        TextView textView = findViewById(R.id.wrongmasterpassword);
        textView.setVisibility(View.VISIBLE);
    }

    private void removeWrongPasswordError() {
        TextView textView = findViewById(R.id.wrongmasterpassword);
        textView.setVisibility(View.INVISIBLE);
    }
}
