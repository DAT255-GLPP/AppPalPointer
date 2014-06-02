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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactList extends Activity {

	private DataHandler handler;
	private List<Contact> contactlist = new ArrayList<Contact>();
	private ListView listViewContact;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);

		listViewContact = (ListView) findViewById(R.id.lvcontact);

		handler = new DataHandler(getBaseContext());
		handler.open();
		Cursor C = handler.returnData();
		if (C.moveToFirst()) {
			do {
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

		Button AddContact = (Button) findViewById(R.id.addcontact);
		// Listening to first button's event
		AddContact.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// Starting a new Intent
				Intent intent = new Intent(getApplicationContext(),
						ContactAdd.class);
				// Sending data to another Activity
				startActivity(intent);
			}
		});
	}

	public void displayContactlist() {

		if (contactlist != null && contactlist.size() > 0) {

			ContactAdapter adapter = new ContactAdapter(this,
					R.layout.rowcontact, contactlist);
			listViewContact.setAdapter(adapter);

			listViewContact.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int postion, long arg3) {
					String contactNumber = contactlist.get(postion).getNr();

					// Starting a new Intent
					Intent intent = new Intent(getApplicationContext(),
							ToDoActivity.class); // Sending data to another
													// Activity
					intent.putExtra("com.example.palpointer.contactNr",
							contactNumber);
					startActivity(intent);

				}
			});

			listViewContact
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int postion, long id) {
							Contact contact = contactlist.get(postion);
							// Starting a new Intent
							Intent intent = new Intent(getApplicationContext(),
									ContactEdit.class); // Sending data to
														// another
														// Activity
							intent.putExtra("contact", contact);
							startActivity(intent);

							return true;
						}
					});

		}

	/*	LinearLayout ll = (LinearLayout) findViewById(R.id.contactlist);
		for (Contact c : contactlist) {
			TextView nameView = new TextView(this);
			nameView.setText(c.getName());
			nameView.setTextSize(20);
			nameView.setHeight(25);
			nameView.setTypeface(Typeface.SERIF);
			// nameView.setPadding(6, 3, 0,3);
			nameView.setTextColor(Color.parseColor("#0e2367"));

			nameView.setOnClickListener(new DynamicOnClickListener(c) {
				public void onClick(View v) {
					String contactNumber = getContact().getNr();
					// Starting a new Intent
					Intent intent = new Intent(getApplicationContext(),
							ToDoActivity.class); // Sending data to another
													// Activity
					intent.putExtra("com.example.palpointer.contactNr",
							contactNumber);
					startActivity(intent);
				}
			});
			nameView.setOnLongClickListener(new DynamicOnLongClickListener(c) {
				public boolean onLongClick(View v) {
					Contact contact = getContact();
					// Starting a new Intent
					Intent intent = new Intent(getApplicationContext(),
							ContactEdit.class); // Sending data to another
												// Activity
					intent.putExtra("contact", contact);
					startActivity(intent);
					return true;
				}
			});
			ll.addView(nameView);
		}*/

	}

	public void informUser(View view) {
		Toast.makeText(
				getBaseContext(),
				"Click on the pal you want to find.\nLongclick to edit or erase pal.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), ToDoActivity.class);
		startActivity(intent);
	}

}
