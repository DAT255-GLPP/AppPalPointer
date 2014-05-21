package com.example.palpointer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContactEdit extends Activity {

	EditText ContactName;
	EditText ContactNr;
	TextView ContactInfo;
	Button SaveEdit;
	Button DeleteContact;
	public String name ="";
	public String nr ="";
	public DataHandler handler;
	public Contact contact;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_edit);
		ContactInfo = (TextView)findViewById(R.id.contactinfo);
		ContactName = (EditText)findViewById(R.id.contactname);
		ContactNr = (EditText)findViewById(R.id.contactnr);
		SaveEdit = (Button)findViewById(R.id.saveedit);
		DeleteContact = (Button)findViewById(R.id.deletecontact);

		Bundle data = getIntent().getExtras();
		contact = (Contact) data.getParcelable("contact");
		ContactInfo.setText("Contact name: " + contact.getName() + "\nContact nr: " + contact.getNr());

		SaveEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				name = ContactName.getText().toString();
				nr = ContactNr.getText().toString();
				handler = new DataHandler(getBaseContext());
				handler.open();
				if (name == null) {
					name = contact.getName();
				}
				if (nr == null) {
					nr = contact.getNr();
				}
				handler.updateData(contact.getName(), name, nr);
				handler.close();
				//Starting a new Intent
				Intent intent = new Intent(getApplicationContext(), ContactList.class);
				//Sending data to another Activity
				startActivity(intent);
			}
		});

		DeleteContact.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				handler = new DataHandler(getBaseContext());
				handler.open();
				handler.deleteData(contact.getName());
				handler.close();
				//Starting a new Intent
				Intent intent = new Intent(getApplicationContext(), ContactList.class);
				//Sending data to another Activity
				startActivity(intent);
			}
		});		
	}
}
