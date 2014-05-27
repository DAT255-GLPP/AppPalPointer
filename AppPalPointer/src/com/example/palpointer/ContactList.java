package com.example.palpointer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactList extends Activity {

	public DataHandler handler;
	public List<Contact> contactlist = new ArrayList<Contact>();
	public ToDoActivity toDoActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		handler = new DataHandler(getBaseContext());
		handler.open();
		Cursor C = handler.returnData();
		if(C.moveToFirst()) {
			do  {
				Contact contact = new Contact(C.getString(0), C.getString(1));
				contactlist.add(contact);
			} while (C.moveToNext());
		}
		handler.close();
		
		Collections.sort(contactlist, new Comparator<Contact>() {
	        @Override
	        public int compare(Contact c1, Contact c2) {
	            return c1.getName().compareToIgnoreCase(c2.getName());
	        }
	    });
		
		displayContactlist();
		
		Button addcontact = (Button) findViewById(R.id.addcontact);
		//Listening to first button's event
		addcontact.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				//Starting a new Intent
				Intent intent = new Intent(getApplicationContext(), ContactAdd.class);
				//Sending data to another Activity
				startActivity(intent);
			}
		});
	}

	public void displayContactlist (){
		LinearLayout ll = (LinearLayout) findViewById(R.id.contactlist);
		for(Contact c : contactlist) {
			TextView nameView = new TextView(this);
			nameView.setText(c.getName());
			nameView.setTextSize(20);
			nameView.setHeight(25);
			nameView.setTypeface(Typeface.SERIF);
			//nameView.setPadding(6, 3, 0, 3);
			nameView.setTextColor(Color.parseColor("#0e2367"));

			
			nameView.setOnClickListener(new DynamicOnClickListener(c) {
				public void onClick(View v) {
					String contactNumber = getContact().getNr();
					//Starting a new Intent
					Intent intent = new Intent(getApplicationContext(), ToDoActivity.class);
					//Sending data to another Activity
					intent.putExtra("com.example.palpointer.contactNr", contactNumber);
					startActivity(intent);
				}
			});
			nameView.setOnLongClickListener(new DynamicOnLongClickListener(c) {
				public boolean onLongClick(View v) {
					Contact contact = getContact();
					//Starting a new Intent
					Intent intent = new Intent(getApplicationContext(), ContactEdit.class);
					//Sending data to another Activity
					intent.putExtra("contact", contact);
					startActivity(intent);
					return true;
				}
			});
			ll.addView(nameView);
		}
	}
	
	public void informUser(View view){
		Toast.makeText(getBaseContext(),  "Click on the pal you want to find.\nLongclick to edit or erase pal.",  Toast.LENGTH_SHORT).show();
	}
	
}
