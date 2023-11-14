package com.dadm.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.dadm.myapplication.DB.Operations;
import com.dadm.myapplication.Model.Contact;

import java.util.List;

public class ViewAllContacts extends AppCompatActivity {

    private Operations contactOps;
    List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_contacts);

        ListView listView = findViewById(R.id.list);
        contactOps = new Operations(this);
        contactOps.openConnection();
        contacts = contactOps.getAllContacts();
        contactOps.closeConnection();

        final ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contacts);
        listView.setAdapter(adapter);

        Button filterButton = findViewById(R.id.filter_contacts_btn);

        final CheckBox consultingCheckBox = findViewById(R.id.check_type_consult);
        final CheckBox developerCheckBox = findViewById(R.id.check_type_dev);
        final CheckBox softwareFactoryCheckBox = findViewById(R.id.check_type_factory);

        filterButton.setOnClickListener(v -> {
            contactOps.openConnection();
            contacts = contactOps.getFilteredContacts(consultingCheckBox.isChecked(), developerCheckBox.isChecked(), softwareFactoryCheckBox.isChecked());
            contactOps.closeConnection();
            adapter.clear();
            adapter.addAll(contacts);
        });
    }
}