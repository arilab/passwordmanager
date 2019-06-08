package com.laynet.passwordmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.laynet.passwordmanager.R;
import com.laynet.passwordmanager.exceptions.CryptoException;
import com.laynet.passwordmanager.persist.EntryPersistence;
import com.laynet.passwordmanager.persist.FileSystem;
import com.laynet.passwordmanager.security.MasterPassword;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int INIT_RESULT = 1;
    public static final int BACKUP_RESULT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button enterButton = findViewById(R.id.enter);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText masterPasswordEdit = findViewById(R.id.masterPassword);
                String password = masterPasswordEdit.getText().toString();
                masterPasswordEdit.setText("");
                if (validatePassword(password)) {
                    removeWrongPasswordError();
                    Intent intent = new Intent(MainActivity.this, EntriesActivity.class);
                    startActivity(intent);
                } else {
                    displayWrongPasswordError();
                }
            }
        });

        Button initButton = findViewById(R.id.init);
        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InitMasterPasswordActivity.class);
                startActivityForResult(intent, INIT_RESULT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        int result = 0;
        if (requestCode == INIT_RESULT) {
            result = R.string.initialize_failed;
            if (resultCode == Activity.RESULT_OK) {
                result = R.string.initialize_success;
            } else if (resultCode == Activity.RESULT_CANCELED) {
                result = R.string.initialize_cancelled;
            }
        } else if (requestCode == BACKUP_RESULT) {
            result = R.string.backup_failed;
            if (resultCode == Activity.RESULT_OK) {
                result = R.string.backup_successful;
            }
        }
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, result, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_backup:
                shareFilestore();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareFilestore() {
        File filestore = new FileSystem().getFilestore(getApplicationContext());
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.laynet.passwordmanager.fileprovider", filestore);
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("application/octet-stream");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);

        startActivityForResult(Intent.createChooser(intentShareFile, "Share File"), BACKUP_RESULT);
    }

    private boolean validatePassword(String password) {
        if (password == null || password.trim().equals(""))
            return false;
//        return password.equals("toto12");
        MasterPassword.getInstance().setPassword(password);
        try {
            new EntryPersistence().read(getApplicationContext());
        } catch (CryptoException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void displayWrongPasswordError() {
        TextView textView = findViewById(R.id.wrongpassword);
        textView.setVisibility(View.VISIBLE);
    }

    private void removeWrongPasswordError() {
        TextView textView = findViewById(R.id.wrongpassword);
        textView.setVisibility(View.INVISIBLE);
    }
}
