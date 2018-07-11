package com.example.sonika.contactsimport

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showContacts();
    }
    /**
     * Show the contacts in the ListView.
     */
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    private fun showContacts() {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            var contacts : List<String> = getContactNames();
            var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
            list.setAdapter(adapter);
        }

    }

    private fun getContactNames() : List<String> {
        var contacts = ArrayList<String>();
        // Get the ContentResolver
        val cr = getContentResolver();
        // Get the Cursor of all the contacts
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        // Move the cursor to first. Also check whether the cursor is empty or not.
        if (cursor.moveToFirst()) {
            // Iterate through the cursor
            do {
                // Get the contacts name
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                val number = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                contacts.add(number)
            } while (cursor.moveToNext());
        }
        // Close the curosor
        cursor.close();
        return contacts;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts()
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
