package com.example.palpointer;

/**
 * Represents an item in a ToDo list
 */
public class UserInformation {


	
	/**
	 * Item latitude
	 */
	@com.google.gson.annotations.SerializedName("latitude")
	private double latitude;
	
	/**
	 * Item longitude
	 */
	@com.google.gson.annotations.SerializedName("longitude")
	private double longitude;

	/**
	 * Item Id
	 */
	@com.google.gson.annotations.SerializedName("id")
	private String mId;

	/**
	 * userId
	 */
	@com.google.gson.annotations.SerializedName("userid")
	private String userId;
	
	/**
	 * phoneNumber
	 */
	@com.google.gson.annotations.SerializedName("phonenumber")
	private String phoneNumber;

	
	
	
	
	/**
	 * ToDoItem constructor
	 */
	public UserInformation() {

	}

	
	/**
	 * Sets the latitude
	 * 
	 * @param latitude
	 *            latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLatitude (){
		return this.latitude;
	}
	
	/**
	 * Sets the longitude
	 * 
	 * @param longitude
	 *            longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLongitude (){
		return this.longitude;
	}

	/**
	 * Returns the item id
	 */
	public String getId() {
		return mId;
	}
	
	/**
	 * Sets the item id
	 * 
	 * @param id
	 *            id to set
	 */
	public final void setId(String id) {
		mId = id;
	}

	/**
	 * Sets the userId
	 * 
	 * @param userId
	 *            userId to set
	 */
	public final void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * Returns the userId
	 */
	public String getUserId() {
		return this.userId;
	}
	
	
	/**
	 * Sets the phoneNumber
	 * 
	 * @param number
	 *            number to set
	 */
	public final void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * Returns the userId
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}




//	@Override
//	public boolean equals(Object o) {
//		return o instanceof ToDoItem && ((ToDoItem) o).mId == mId;
//	}
}
