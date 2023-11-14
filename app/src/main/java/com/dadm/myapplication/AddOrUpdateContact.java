package com.dadm.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.dadm.myapplication.DB.Operations;
import com.dadm.myapplication.Model.Contact;

public class AddOrUpdateContact extends AppCompatActivity {

    private EditText nameEditText;
    private EditText urlEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText descriptionEditText;
    private CheckBox isConsultingCheckBox;
    private CheckBox isDeveloperCheckBox;
    private CheckBox isSoftwareFactoryCheckBox;
    private Contact newContact;
    private Contact oldContact;
    private String mode;
    private Operations contactOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_contact);

        newContact = new Contact();
        oldContact = new Contact();

        nameEditText = findViewById(R.id.name_field);
        urlEditText = findViewById(R.id.url_field);
        emailEditText = findViewById(R.id.mail_field);
        phoneEditText = findViewById(R.id.phone_field);
        descriptionEditText = findViewById(R.id.desc_field);

        isConsultingCheckBox = findViewById(R.id.check_type_consult);
        isDeveloperCheckBox = findViewById(R.id.check_type_dev);
        isSoftwareFactoryCheckBox = findViewById(R.id.check_type_factory);

        Button addUpdateButton = findViewById(R.id.add_contact_btn);


        contactOps = new Operations(this);
        mode = getIntent().getStringExtra("action");

        if(mode.equalsIgnoreCase("Update")){
            addUpdateButton.setText("Actualizar contacto");
            initContact(getIntent().getLongExtra("id",0));
        }

        isConsultingCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            newContact.setIsConsulting(isChecked ? 1 : 0);
            if(mode.equalsIgnoreCase("Update")){
                oldContact.setIsConsulting(isChecked ? 1 : 0);
            }
        });

        isDeveloperCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            newContact.setIsDevelopment(isChecked ? 1 : 0);
            if(mode.equalsIgnoreCase("Update")){
                oldContact.setIsDevelopment(isChecked ? 1 : 0);
            }
        });

        isSoftwareFactoryCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            newContact.setIsSoftwareFactory(isChecked ? 1 : 0);
            if(mode.equalsIgnoreCase("Update")){
                oldContact.setIsSoftwareFactory(isChecked ? 1 : 0);
            }
        });

        addUpdateButton.setOnClickListener(v -> {
            contactOps.openConnection();
            if(mode.equalsIgnoreCase("Add")){
                newContact.setName(nameEditText.getText().toString());
                newContact.setUrl(nameEditText.getText().toString());
                newContact.setEmail(emailEditText.getText().toString());
                newContact.setPhone(phoneEditText.getText().toString());
                newContact.setDescription(descriptionEditText.getText().toString());

                newContact.setIsConsulting(isConsultingCheckBox.isChecked() ? 1 : 0);
                newContact.setIsDevelopment(isDeveloperCheckBox.isChecked() ? 1 : 0);
                newContact.setIsSoftwareFactory(isSoftwareFactoryCheckBox.isChecked() ? 1 : 0);

                contactOps.addContact(newContact);
                contactOps.closeConnection();

                Toast.makeText(AddOrUpdateContact.this, "Contacto creado exitosamente",
                        Toast.LENGTH_SHORT)
                        .show();

                finish();
            }else{
                oldContact.setName(nameEditText.getText().toString());
                oldContact.setUrl(urlEditText.getText().toString());
                oldContact.setEmail(emailEditText.getText().toString());
                oldContact.setPhone(phoneEditText.getText().toString());
                oldContact.setDescription(descriptionEditText.getText().toString());

                oldContact.setIsConsulting(isConsultingCheckBox.isChecked() ? 1 : 0);
                oldContact.setIsDevelopment(isDeveloperCheckBox.isChecked() ? 1 : 0);
                oldContact.setIsSoftwareFactory(isSoftwareFactoryCheckBox.isChecked() ? 1 : 0);

                contactOps.updateContact(oldContact);
                contactOps.closeConnection();

                Toast.makeText(AddOrUpdateContact.this, "Contacto actualizado exitosamente",
                                Toast.LENGTH_SHORT)
                        .show();

                finish();
            }
        });
    }

    private void initContact(long empId){
        contactOps.openConnection();
        oldContact = contactOps.getContact(empId);
        contactOps.closeConnection();

        nameEditText.setText(oldContact.getName());
        urlEditText.setText(oldContact.getUrl());
        emailEditText.setText(oldContact.getEmail());
        phoneEditText.setText(oldContact.getPhone());
        descriptionEditText.setText(oldContact.getDescription());

        isConsultingCheckBox.setChecked(oldContact.getIsConsulting() == 1? Boolean.TRUE : Boolean.FALSE);
        isDeveloperCheckBox.setChecked(oldContact.getIsDevelopment() == 1? Boolean.TRUE : Boolean.FALSE);
        isSoftwareFactoryCheckBox.setChecked(oldContact.getIsSoftwareFactory() == 1? Boolean.TRUE : Boolean.FALSE);


    }
}