package com.example.palpointer;

import android.os.Parcel;
import android.os.Parcelable;

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
		
		public String getName (){
			return name;
		}
		
		public String getNr (){
			return nr;
		}
		
		public void setName (String name){
			this.name = name;
		}
		
		public void setNr (String nr){
			this.nr = nr;
		}
		
		public int describeContents(){
			return 0;
		}
		
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeStringArray(new String[] {this.name, this.nr});
		}
		
		 public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	           public Contact createFromParcel(Parcel in) {
	               return new Contact(in); 
	           }

	           public Contact[] newArray(int size) {
	               return new Contact[size];
	           }
	       };
}
