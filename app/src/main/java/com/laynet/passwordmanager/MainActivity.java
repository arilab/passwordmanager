package com.laynet.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.laynet.passwordmanager.exceptions.CryptoException;
import com.laynet.passwordmanager.persist.EntryPersistence;
import com.laynet.passwordmanager.security.MasterPassword;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int INIT_RESULT = 1;

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
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == INIT_RESULT) {
            CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, R.string.initialize_success, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
