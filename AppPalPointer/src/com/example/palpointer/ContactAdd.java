package com.example.palpointer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ContactAdd extends Activity {

	public EditText ContactName;
	public EditText ContactNr;
	public Button addContact;
	public String getName ="";
	public String getNr ="";
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
				getName = ContactName.getText().toString();
				getNr = ContactNr.getText().toString();
				handler = new DataHandler(getBaseContext());
				handler.open();
				long id = handler.insertData(getName, getNr);
				Toast.makeText(getBaseContext(),  "Contact saved",  Toast.LENGTH_LONG).show();
				handler.close();
				//Starting a new Intent
				Intent intent = new Intent(getApplicationContext(), ContactList.class);
				//Sending data to another Activity
				startActivity(intent);
			}
		});
	}

}
