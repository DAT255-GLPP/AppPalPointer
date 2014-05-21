package com.example.palpointer;

import android.view.View;
import android.view.View.OnLongClickListener;

public class DynamicOnLongClickListener implements OnLongClickListener {
	public Contact contact;
	public DynamicOnLongClickListener (Contact contact) {
		this.contact = contact;
	}
	@Override
	public boolean onLongClick(View v) {
		return true;
	}
	
	public Contact getContact (){
		return contact;
	}
}