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

	public EditText ContactName;
	public EditText ContactNr;
	public TextView ContactInfo;
	public Button SaveEdit;
	public Button DeleteContact;
	public String name ="";
	public String nr ="";
	public DataHandler handler;
	public Contact contact;

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
			public void onClick(View v) {
				name = ContactName.getText().toString();
				nr = ContactNr.getText().toString();			
				handler = new DataHandler(getBaseContext());
				handler.open();
				if (name.matches("") && nr.matches("")) {
					name = contact.getName();
					nr = contact.getNr();
					Toast.makeText(getBaseContext(),  "No changes made",  Toast.LENGTH_SHORT).show();
				}
				else if (!name.matches("") && nr.matches("")) {
					nr = contact.getNr();
					Toast.makeText(getBaseContext(),  "Contact name changed to " + name,  Toast.LENGTH_SHORT).show();
				}
				else if (name.matches("") && !nr.matches("")) {
					name = contact.getName();
					Toast.makeText(getBaseContext(),  "Contact number changed to " + nr,  Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getBaseContext(),  "Contact name changed to " + name + " and number changed to " + nr,  Toast.LENGTH_SHORT).show();
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
				Toast.makeText(getBaseContext(),  "Contact deleted",  Toast.LENGTH_SHORT).show();
				//Starting a new Intent
				Intent intent = new Intent(getApplicationContext(), ContactList.class);
				//Sending data to another Activity
				startActivity(intent);
			}
		});		
	}
}
