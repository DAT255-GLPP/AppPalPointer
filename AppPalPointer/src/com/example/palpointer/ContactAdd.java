package com.example.palpointer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactAdd extends Activity {

	private EditText ContactName;
	private EditText ContactNr;
	private Button addContact;
	private String name ="";
	private String phonenumber ="";
	private DataHandler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_add);
		ContactName = (EditText)findViewById(R.id.contactname);
		ContactNr = (EditText)findViewById(R.id.contactnr);
		addContact = (Button)findViewById(R.id.addcontact);
		
		
		//Adding contacts on click
		
		addContact.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				name = ContactName.getText().toString();
				phonenumber = ContactNr.getText().toString();	
				//Insert contact info into SQLite if contact name and phonenumber has been filled in
				if (!name.matches("") && !phonenumber.matches("") && phonenumber.length() == 10) {
					Toast.makeText(getBaseContext(),  "Contact saved",  Toast.LENGTH_SHORT).show();
					handler = new DataHandler(getBaseContext());
					handler.open();
					long id = handler.insertData(name, phonenumber);
					handler.close();
					
					//Starting a new Intent
					Intent intent = new Intent(getApplicationContext(), ContactList.class);
					//Sending data to another Activity
					startActivity(intent);
					finish();
				}
				//Inform the user that a phonenumber is necessary if no number has been given by the user
				else if (!name.matches("") && phonenumber.matches("")) {
					Toast.makeText(getBaseContext(),  "Contact number required",  Toast.LENGTH_SHORT).show();
				}
				//Inform the user that a contact name is necessary if no name has been given by the user
				else if (name.matches("") && !phonenumber.matches("")) {
					Toast.makeText(getBaseContext(),  "Contact name required",  Toast.LENGTH_SHORT).show();
				}
				//Inform the user that a phonenumber must be 10 digits long
				else if (phonenumber.length() != 10) {
					Toast.makeText(getBaseContext(),  "The number has to be 10 digits long",  Toast.LENGTH_SHORT).show();
				}
				//Inform the user that both a contact name and number is necessary if no name or number has been given by the user
				else {
					Toast.makeText(getBaseContext(),  "Contact name and number required",  Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	/**
	 * Go back to contact list when back button is pressed and finish current activity
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), ContactList.class);
		startActivity(intent);
		finish();
	}
	
}