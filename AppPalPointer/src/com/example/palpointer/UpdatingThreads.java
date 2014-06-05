package com.example.palpointer;

import java.util.List;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

public class UpdatingThreads extends Thread {

	private ToDoActivity theActivity;
	private UserInformation item;
	private String nameOfThread;
	private boolean executeWhileLoop;
	private boolean palsCoordinatesDownloaded = false;
	private boolean ownCoordinatesUploaded = false;


	public UpdatingThreads(ToDoActivity theActivity, UserInformation item, String nameOfThread){
		this.theActivity = theActivity;
		this.item = item;
		this.nameOfThread = nameOfThread;
	}


	public void run(){
		if (nameOfThread == "downloadThread"){
			downloadPalsCoordinates();
		}

		else if (nameOfThread == "uploadThread"){
			uploadOwnCoordinates();
		}
	}

	/**
	 * Method to download a contact's position from the database
	 */
	private void downloadPalsCoordinates(){
		try{
			executeWhileLoop = true;
			while (executeWhileLoop){
				palsCoordinatesDownloaded = false;
				//Check if a person with the contact's number exists in the database
				theActivity.getTable().where().field("phonenumber").eq(ToDoActivity.getRequestedPhoneNumber()).execute(new TableQueryCallback<UserInformation>() {

					@Override
					public void onCompleted(List<UserInformation> entity, int count, Exception exception, ServiceFilterResponse response) {
						if (exception == null) {
							//If a user has the number, get the position
							if (!entity.isEmpty()){				
								theActivity.setPalsCoordinates(entity.get(0).getLatitude(), entity.get(0).getLongitude());
								palsCoordinatesDownloaded = true;	
							}

							else{
								Toast.makeText(theActivity.getBaseContext(),  "Your pal has not uploaded his/her postion.",  Toast.LENGTH_SHORT).show();
							}				
						}
						else {
							theActivity.createAndShowDialog(exception, "UpdatingThreadscheckPhoneNumber");
						}

					}
				});
				//Sleep so downloading doesn't happen all the time
				while (!palsCoordinatesDownloaded){
					Thread.sleep(1 * 1000);
				}
			}
		}
		catch (InterruptedException e){
			theActivity.createAndShowDialog("Fail to keep uploading coordinates.", "Error");
		}
	}
	
	/**
	 * Method to upload own coordinates to the database
	 */
	public void uploadOwnCoordinates(){

		try {
			executeWhileLoop = true;
			while(executeWhileLoop){
				ownCoordinatesUploaded = false;

				//Set the user's position
				item.setLatitude(theActivity.getLatitude());
				item.setLongitude(theActivity.getLongitude());

				//Update the user's position in the database
				theActivity.getTable().update(item, new TableOperationCallback<UserInformation>() {

					public void onCompleted(UserInformation entity, Exception exception, ServiceFilterResponse response) {

						if (exception != null) {
							theActivity.createAndShowDialog(exception, "Error");
						}
						ownCoordinatesUploaded = true;	
					}
				});
				//Sleep so uploading doesn't happen all the time
				while (!ownCoordinatesUploaded){
					Thread.sleep(1 * 1000);
				}
			}
		}

		catch (InterruptedException e){
			theActivity.createAndShowDialog("Fail to keep uploading coordinates.", "Error");
		}
	}

	public void setWhileLoopStatus(boolean executeWhileLoop){
		this.executeWhileLoop = executeWhileLoop;
	}
}
