package com.example.palpointer;

import java.util.List;

import android.view.View;
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
		if (nameOfThread == "download"){
			downloadPalsCoordinates();
		}

		else if (nameOfThread == "upload"){
			uploadOwnCoordinates();
		}
	}




	private void downloadPalsCoordinates(){
		try{
			executeWhileLoop = true;
			while (executeWhileLoop){
				palsCoordinatesDownloaded = false;
				theActivity.getTable().where().field("phonenumber").eq(ToDoActivity.getRequestedPhoneNumber()).execute(new TableQueryCallback<UserInformation>() {

					@Override
					public void onCompleted(List<UserInformation> entity, int count, Exception exception, ServiceFilterResponse response) {
						if (exception == null) {

							if (!entity.isEmpty()){				
								theActivity.setPalsCoordinates(entity.get(0).getLatitude(), entity.get(0).getLongitude());
								//theActivity.setCompassVisible(true);
								palsCoordinatesDownloaded = true;	
							}

							//Tror inte att denna funkar nu när den är i tråden
							else{
								Toast.makeText(theActivity.getBaseContext(),  "Your pal has not uploaded his/her postion.",  Toast.LENGTH_SHORT).show();
							}				
						}

						//Tror inte att denna funkar nu när den är i tråden
						else {
							theActivity.createAndShowDialog(exception, "UpdatingThreadscheckPhoneNumber");
						}

					}
				});

				while (!palsCoordinatesDownloaded){
					Thread.sleep(1 * 1000);
				}
			}
		}
		catch (InterruptedException e){
			theActivity.createAndShowDialog("Fail to keep uploading coordinates.", "Error");
		}
	}




	public void uploadOwnCoordinates(){

		try {
			executeWhileLoop = true;
			while(executeWhileLoop){
				ownCoordinatesUploaded = false;

				// Create a new item
				item.setLatitude(theActivity.getLatitude());
				item.setLongitude(theActivity.getLongitude());

				// Insert the new item
				theActivity.getTable().update(item, new TableOperationCallback<UserInformation>() {

					public void onCompleted(UserInformation entity, Exception exception, ServiceFilterResponse response) {

						if (exception != null) {
							theActivity.createAndShowDialog(exception, "Error");
						}
						ownCoordinatesUploaded = true;	
					}
				});

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
