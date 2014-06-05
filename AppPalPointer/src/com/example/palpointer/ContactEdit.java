package com.example.palpointer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContactEdit extends Activity {

	private EditText ContactName;
	private EditText ContactNr;
	private TextView ContactInfo;
	private Button SaveEdit;
	private Button DeleteContact;
	private String name ="";
	private String phonenumber ="";
	private DataHandler handler;
	private Contact contact;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_edit);
		ContactInfo = (TextView)findViewById(R.id.contactinfo);
		ContactName = (EditText)findViewById(R.id.setcontactname);
		ContactNr = (EditText)findViewById(R.id.setcontactnr);
		SaveEdit = (Button)findViewById(R.id.saveedit);
		DeleteContact = (Button)findViewById(R.id.deletecontact);

		Bundle data = getIntent().getExtras();
		contact = (Contact) data.getParcelable("contact");
		ContactInfo.setText("Contact name: " + contact.getName() + "\nContact nr: " + contact.getNr());

		SaveEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				name = ContactName.getText().toString();
				phonenumber = ContactNr.getText().toString();			
				handler = new DataHandler(getBaseContext());
				handler.open();
				if (name.matches("") && phonenumber.matches("")) {
					name = contact.getName();
					phonenumber = contact.getNr();
					Toast.makeText(getBaseContext(),  "No changes made",  Toast.LENGTH_SHORT).show();
				}
				else if (!name.matches("") && phonenumber.matches("")) {
					phonenumber = contact.getNr();
					Toast.makeText(getBaseContext(),  "Contact name changed to " + name,  Toast.LENGTH_SHORT).show();
				}
				else if (name.matches("") && !phonenumber.matches("")) {
					name = contact.getName();
					Toast.makeText(getBaseContext(),  "Contact number changed to " + phonenumber,  Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getBaseContext(),  "Contact name changed to " + name + " and number changed to " + phonenumber,  Toast.LENGTH_SHORT).show();
				}
				handler.updateData(contact.getName(), name, phonenumber);
				handler.close();
				//Starting a new Intent
				Intent intent = new Intent(getApplicationContext(), ContactList.class);
				//Sending data to another Activity
				startActivity(intent);
				finish();
			}
		});

		DeleteContact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler = new DataHandler(getBaseContext());
				handler.open();
				handler.deleteData(contact.getName());
				handler.close();
				Toast.makeText(getBaseContext(),  "Contact deleted",  Toast.LENGTH_SHORT).show();
				//Starting a new Intent
				Intent intent = new Intent(getApplicationContext(), ContactList.class);
				//Sending data to another Activity
				startActivity(intent);
				finish();
			}
		});		
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), ContactList.class);
		startActivity(intent);
		finish();
	}
}
