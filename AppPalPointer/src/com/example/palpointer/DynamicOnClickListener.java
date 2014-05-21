package com.example.palpointer;

import android.view.View;
import android.view.View.OnClickListener;

public class DynamicOnClickListener implements OnClickListener {
	public Contact contact;
	public DynamicOnClickListener (Contact contact) {
		this.contact = contact;
	}
	@Override
	public void onClick(View v) {
	
	}
	
	public Contact getContact (){
		return contact;
	}
}