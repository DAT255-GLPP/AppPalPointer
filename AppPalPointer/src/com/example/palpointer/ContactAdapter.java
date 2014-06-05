package com.example.palpointer;

import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {

	private List<Contact> contactList;
	private int row;
	private Context context;

	//Adapter constructor getting contact list to display
	public ContactAdapter(Context context, int resource, List<Contact> list) {
		super(context, resource, list);

		this.contactList = list;
		this.row = resource;
		this.context = context;

	}
	/**
	 * Returns the contact at a given position in the list
	 */
	@Override
	public Contact getItem(int pos) {
		return contactList.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	/**
	 *  Getting view of list item and setting on the list view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} 
		else {
			holder = (ViewHolder) view.getTag();
		}

		Contact contact = contactList.get(position);

		holder.tvName = (TextView) view.findViewById(R.id.tvname);
		holder.tvName.setText(contact.getName());
		holder.tvName.setTypeface(Typeface.SERIF);

		return view;
	}


	//View holder class with fields of list item to hold the current view on list
	public class ViewHolder {
		public TextView tvName;
	}

	/**
	 * Returns the size of the contactlist
	 */
	@Override
	public int getCount() {
		return contactList.size();
	}
}
