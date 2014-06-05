package com.example.palpointer;

import android.os.Parcel;
import android.os.Parcelable;


//Implementing Parcelable to be able to transfer information more easily
public class Contact implements Parcelable {
	private String name;
	private String nr;
		public Contact (String name, String nr){
			this.name = name;
			this.nr = nr;
		}
		

		public Contact(Parcel in) {
			String[] data = new String[2];
			in.readStringArray(data);
			this.name = data[0];
			this.nr = data[1];
		}
		
		/**
		 * Returns the name of the contact
		 */
		public String getName (){
			return name;
		}
		
		/**
		 * Returns the phone number of the contact
		 */
		public String getNr (){
			return nr;
		}
		
		/**
		 * Sets the contact's name
		 */
		public void setName (String name){
			this.name = name;
		}
		
		/**
		 * Sets the contact's phone number
		 */
		public void setNr (String nr){
			this.nr = nr;
		}
		
		//Necessary for a parcelable class
		public int describeContents(){
			return 0;
		}
		
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeStringArray(new String[] {this.name, this.nr});
		}
		
		//Makes the Contact class parcelable
		 public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	           public Contact createFromParcel(Parcel in) {
	               return new Contact(in); 
	           }

	           public Contact[] newArray(int size) {
	               return new Contact[size];
	           }
	       };
}
