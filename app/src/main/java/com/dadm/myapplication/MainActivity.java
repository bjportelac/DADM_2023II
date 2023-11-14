package com.dadm.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dadm.myapplication.DB.Operations;
import com.dadm.myapplication.Model.Contact;

public class MainActivity extends AppCompatActivity {

    private Button addContactButton;
    private Button editContactButton;
    private Button deleteContactButton;
    private Button viewAllContactButton;
    private Operations contactOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        addContactButton = (Button) findViewById(R.id.add_contacts);
        editContactButton = (Button) findViewById(R.id.edit_contacts);
        deleteContactButton = (Button) findViewById(R.id.delete_contacts);
        viewAllContactButton = (Button) findViewById(R.id.view_contacts);

        contactOps = new Operations(MainActivity.this);

        addContactButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddOrUpdateContact.class);
            i.putExtra("action", "Add");
            startActivity(i);
        });

        editContactButton.setOnClickListener(v ->{
            getContactId();
        });

        deleteContactButton.setOnClickListener(v ->{
            remContactId();
        });

        viewAllContactButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ViewAllContacts.class);
            startActivity(i);
        });


    }

    public void getContactId(){
        LayoutInflater li = LayoutInflater.from(this);
        View getEmpIdView = li.inflate(R.layout.dialog_get_emp_id, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getEmpIdView);

        final EditText editText = (EditText) getEmpIdView.findViewById(R.id.contact_id_edit_field);

        builder.setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    Intent i = new Intent(MainActivity.this, AddOrUpdateContact.class);
                    i.putExtra("action", "Update");
                    i.putExtra("id", Long.parseLong(editText.getText().toString()));
                    startActivity(i);


                })
                .create()
                .show();
    }

    public void remContactId(){
        LayoutInflater li = LayoutInflater.from(this);
        View getEmpIdView = li.inflate(R.layout.dialog_get_emp_id, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getEmpIdView);

        final EditText editText = (EditText) getEmpIdView.findViewById(R.id.contact_id_edit_field);

        builder.setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    showConfirmationDialog(contactOps.getContact(Long.parseLong(editText.getText().toString())));
                })
                .create()
                .show();
    }

    private void showConfirmationDialog(final Contact contact){
        new AlertDialog.Builder(this)
                .setMessage("¿Seguro de eliminar " + contact.getName() + "?")
                .setPositiveButton("Si", (dialog, which) -> {
                    contactOps.removeContact(contact);
                    Toast.makeText(MainActivity.this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(contactOps != null){
            contactOps.openConnection();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(contactOps != null){
            contactOps.closeConnection();
        }
    }
}