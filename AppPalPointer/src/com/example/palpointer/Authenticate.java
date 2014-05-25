package com.example.palpointer;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

public class Authenticate{
	
	/**
	 * Mobile Service Client reference
	 */
	private static MobileServiceClient mClient;
	
	private static UserInformation mUser;
	
	private static UpdatingThreads upload;
	
	public static void setClient(MobileServiceClient theClient){
		mClient = theClient;
	}
	
	public static void setUser(UserInformation theUser){
		mUser = theUser;
	}
	
	public static MobileServiceClient getClient(){
		return mClient;
	}
	
	public static UserInformation getUser(){
		return mUser;
	}
	
	public static void setUploadThread(UpdatingThreads uploadingThread){
		upload = uploadingThread;
	}
	
	public static UpdatingThreads getUploadThread(){
		return upload;
	}
	
}

