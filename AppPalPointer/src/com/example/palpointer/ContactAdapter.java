package com.example.palpointer;

import java.util.HashMap;
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

	public ContactAdapter(Context context, int resource, List<Contact> list) {
		super(context, resource, list);

		this.contactList = list;
		this.row = resource;
		this.context = context;

	}

	@Override
	public Contact getItem(int pos) {
		return contactList.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Contact contact = contactList.get(position);

		holder.tvName = (TextView) view.findViewById(R.id.tvname);
		holder.tvName.setText(contact.getName());
		holder.tvName.setTypeface(Typeface.SERIF);

		return view;

	}

	public class ViewHolder {
		public TextView tvName;
	}

	@Override
	public int getCount() {
		return contactList.size();
	}
}
