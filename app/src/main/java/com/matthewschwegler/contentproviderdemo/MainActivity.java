package com.matthewschwegler.contentproviderdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView contactNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contactNames = (ListView) findViewById(R.id.contact_names);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);
                List<String> contacts = new ArrayList<String>();
                if(cursor.moveToFirst()) {
                    do {
                        //Get the name using the cursor to get the column index of the name.
                        contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    } while(cursor.moveToNext());
                }
                //Takes the contact_detail layout and fills the R.id.name with the contacts field.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.contact_detail, R.id.name, contacts);
                contactNames.setAdapter(adapter);
            }
        });

        // Android M level permissions on the fly request
        final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        int hasReadContactsPermission =
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);
        if(hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
            //This line asks andriod if this permission requires user permission and then requests if so
            if(!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_CONTACTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {Manifest.permission.READ_CONTACTS},
                REQUEST_CODE_ASK_PERMISSIONS);
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
}
