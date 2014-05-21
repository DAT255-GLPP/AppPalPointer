package com.example.palpointer;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactAdd extends Activity {

	public EditText ContactName;
	public EditText ContactNr;
	public Button addContact;
	public String name ="";
	public String nr ="";
	public DataHandler handler;
	public List<Contact> contactlist = new ArrayList<Contact>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_add);
		ContactName = (EditText)findViewById(R.id.contactname);
		ContactNr = (EditText)findViewById(R.id.contactnr);
		addContact = (Button)findViewById(R.id.addcontact);
		addContact.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				name = ContactName.getText().toString();
				nr = ContactNr.getText().toString();	
				if (!name.matches("") && !nr.matches("")) {
					Toast.makeText(getBaseContext(),  "Contact saved",  Toast.LENGTH_LONG).show();
					handler = new DataHandler(getBaseContext());
					handler.open();
					long id = handler.insertData(name, nr);
					handler.close();
					//Starting a new Intent
					Intent intent = new Intent(getApplicationContext(), ContactList.class);
					//Sending data to another Activity
					startActivity(intent);
				}
				else if (!name.matches("") && nr.matches("")) {
					Toast.makeText(getBaseContext(),  "Contact number required",  Toast.LENGTH_LONG).show();
				}
				else if (name.matches("") && !nr.matches("")) {
					Toast.makeText(getBaseContext(),  "Contact name required",  Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(getBaseContext(),  "Contact name and number required",  Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}