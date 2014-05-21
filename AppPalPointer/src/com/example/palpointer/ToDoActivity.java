package com.example.palpointer;


import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.*;

import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;

import java.net.MalformedURLException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;


import android.content.DialogInterface;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

public class ToDoActivity extends Activity implements SensorEventListener{

	TextView textLat;
	TextView textLong;
	TextView textPalLat;
	TextView textPalLong;
	TextView textDist;
	TextView textCurrentBearing;
	TextView textPalBearing;
	ImageView imageViewCompass;
//	ImageView imageViewCompassGreen;
//	ImageView imageViewCompassRed;
	

	double NO_COORDINATE = -1000;
	double oldLat = NO_COORDINATE;
	double oldLong = NO_COORDINATE;
	double myLat = NO_COORDINATE;
	double myLong = NO_COORDINATE;
	double distance = 0;
	double currentBearing = 0;
	double palBearing = 0;
	double palLat = NO_COORDINATE;
	double palLong = NO_COORDINATE;

	boolean keepUploadingCoordinates = false;
	boolean keepDownloadingPalsCoordinates = false;

	Thread threadForUploadingOwnCoordinates;
	Thread threadForUploadingPalsCoordinates;
	String phoneNumber;
	public static String contactNr;
	public static String contactNumber;

	UpdatingThreads download;
	UpdatingThreads upload;
	
	boolean isAutethenticated = false;
	
	boolean compassIsVisible = false;
	
	boolean gpsIsAccurate = false;
	
	boolean greenArrow = false;
	boolean redArrow = false;


	/**
	 * Mobile Service Client reference
	 */
	private MobileServiceClient mClient;

	/**
	 * Mobile Service Table used to access data
	 */
	private MobileServiceTable<UserInformation> mToDoTable;	

	/**
	 * Initializes the activity
	 */
	//CompassStart
	// define the display assembly compass picture
	private ImageView image;
//	private ImageView imageGreen;
//	private ImageView imageRed;

	// record the compass picture angle turned
	private float currentDegree = 0f;

	// device sensor manager
	private SensorManager mSensorManager;

	TextView tvHeading;
	//CompassEnd

	public MobileServiceUser user;
	UserInformation item;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do);
		setCompassLayout();
		
		textLat = (TextView)findViewById(R.id.textLat);
		textLong = (TextView)findViewById(R.id.textLong);
		textPalLat = (TextView)findViewById(R.id.textPalLat);
		textPalLong = (TextView)findViewById(R.id.textPalLong);
		textDist = (TextView)findViewById(R.id.textDist);
		textCurrentBearing = (TextView)findViewById(R.id.textCurrentBearing);
		textPalBearing = (TextView)findViewById(R.id.textPalBearing);
		imageViewCompass = (ImageView)findViewById(R.id.imageViewCompass);
//		imageViewCompassGreen = (ImageView)findViewById(R.id.imageViewGreen);
//		imageViewCompassGreen = (ImageView)findViewById(R.id.imageViewRed);

		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener ll = new myLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

		try {
			// Create the Mobile Service Client instance, using the provided
			// Mobile Service URL and key
			
			mClient = Authenticate.getClient();
			item = Authenticate.getUser();
			
			if ((mClient == null) && (user == null)){
				mClient = new MobileServiceClient(
						"https://palpointer.azure-mobile.net/",
						"fFsYNDwyydTpUlQsPrqfQKezqvXonv99",
						this
						);
				authenticate();
				createTable();
			}
			
			else if (getIntent().hasExtra("com.example.palpointer.contactNr")) {
				createTable();
				contactNumber = getIntent().getStringExtra("com.example.palpointer.contactNr");
				startDownloadingPalsPosition();
			}
		}

			//autenticate the user
			

		catch (MalformedURLException e) {
			createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}
		
		

		Button gp = (Button) findViewById(R.id.gp);
		//Listening to first button's event
		gp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				//Starting a new Intent
				Intent contactScreen = new Intent(getApplicationContext(), ContactList.class);
				//Sending data to another Activity
				startActivity(contactScreen);
			}
		});

		//		Button back = (Button) findViewById(R.id.goBack);
		//
		//	    
		//        //Listening to first button's event
		//        back.setOnClickListener(new View.OnClickListener() {
		//    	
		//        	public void onClick(View arg0) {
		//        		
		//        		//Starting a new Intent
		//        		Intent firstScreen = new Intent(getApplicationContext(), Main.class);
		//        		
		//
		//        		//Sending data to another Activity
		//        		startActivity(firstScreen);
		//        	}
		//        });     

		Button displayPos = (Button) findViewById(R.id.display);

		//Listening to second button's event
		displayPos.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg1) {
				displayPalsPosition();

			}
		});
		
		
		
		
//		if (getIntent().hasExtra("com.example.palpointer.contactNr")) {
//			while (!isAutethenticated){
//				try {
//					Thread.sleep(1 * 1000);
//				}
//				catch (InterruptedException e){
//					
//				}
//			}
//			contactNumber = getIntent().getStringExtra("com.example.palpointer.contactNr");
//			startDownloadingPalsPosition();
//		}
	}
	
//	public void onStart(){
//while (!isAutethenticated){
//			
//		}
//		
//	}




	public void checkIfPhoneNumberExists(){
		mToDoTable.where().field("userid").eq(user.getUserId()).execute(new TableQueryCallback<UserInformation>() {

			@Override
			public void onCompleted(List<UserInformation> entity, int count, Exception exception, ServiceFilterResponse response) {
				if (exception == null) {

					if (entity.isEmpty()){
						//doesPhoneNumberExist = false;
						item = new UserInformation();
						Authenticate.setUser(item);
						setIdAndPhoneNumber();	
					}
					else{					
						//doesPhoneNumberExist = true;					
						//fetchOldTableRow();	
						item = entity.get(0);
						Authenticate.setUser(item);
					}

				}

				else {
					createAndShowDialog(exception, "checkPhoneNumber");
				}		
			}
		});
	}

	public void setIdAndPhoneNumber() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Update information");
		alert.setMessage("Please register your phone number");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				// Do something with value!

				item.setUserId(user.getUserId());
				item.setPhoneNumber(value);

				mToDoTable.insert(item, new TableOperationCallback<UserInformation>() {

					public void onCompleted(UserInformation entity, Exception exception, ServiceFilterResponse response) {

						if (exception == null) {

						} else {
							createAndShowDialog(exception, "Error");
						}

					}
				});	  
			}
		});
		alert.show();
	}	

	public static void setNr (String nr){
		contactNr = nr;
	}


	public void startDownloadingPalsPosition(){
//		if (download.isAlive()){
//			download.setWhileLoopStatus(false);
//		}
//		while (download.isAlive()){
//			try {
//				Thread.sleep(5 * 100);
//			}
//			catch (InterruptedException e){
//				
//			}
//		}
		
		download = new UpdatingThreads(this, item, "download");
		download.start();
	}


	public void displayPalsPosition(){
		download = new UpdatingThreads(this, item, "download");

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Search Friend");
		alert.setMessage("Please state friends phone number");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				phoneNumber = input.getText().toString();
				download.start();
			}
		});
		alert.show();

	}

	public MobileServiceTable<UserInformation> getTable(){
		return mToDoTable;
	}

	public static String getRequestedPhoneNumber (){
		return contactNumber;
	}

	public void setPalsCoordinates(double palLat, double palLong){
		this.palLat = palLat;
		this.palLong = palLong;
	}

	public void setCompassVisible(boolean value){
		if (value == true){
			imageViewCompass.setVisibility(View.VISIBLE);
		}
	}

	public void stopDownloadingCoordinates(View view){
		if (!(download == null))
			download.setWhileLoopStatus(false); 
	}

	public void setCompassLayout(){
		// our compass image
		image = (ImageView) findViewById(R.id.imageViewCompass);
//		imageGreen = (ImageView) findViewById(R.id.imageViewGreen);
//		imageRed = (ImageView) findViewById(R.id.imageViewRed);
		// TextView that will tell the user what degree is he heading
		tvHeading = (TextView) findViewById(R.id.tvHeading);

		// initialize your android device sensor capabilities
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// for the system's orientation sensor registered listeners
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// to stop the listener and save battery
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (!compassIsVisible){
			setCompassVisible(true);
			compassIsVisible = true;
		}
		
		float degree = 0;
		
		if (!(contactNumber == null) && gpsIsAccurate){
			// get the angle around the z-axis rotated
			degree = Math.round((event.values[0]+palBearing) % 360);
		}

		tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

		// create a rotation animation (reverse turn degree degrees)
		RotateAnimation ra = new RotateAnimation(
				currentDegree, 
				-degree,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF,
				0.5f);

		// how long the animation will take place
		ra.setDuration(210);

		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		
		if (greenArrow){
			image.setImageResource(R.drawable...);
			image.startAnimation(ra);
			currentDegree = -degree;
		}
		else if (redArrow){
			redImage.startAnimation(ra);
			currentDegree = -degree;
		}
		
		else if (!greenArrow && !redArrow)
			image.startAnimation(ra);
			currentDegree = -degree;
	}

	private void authenticate() {

		// Login using the Facebook provider.
		mClient.login(MobileServiceAuthenticationProvider.Facebook,
				new UserAuthenticationCallback() {

			@Override
			public void onCompleted(MobileServiceUser tempuser,
					Exception exception, ServiceFilterResponse response) {
				user = tempuser;
				if (exception == null) {
					createAndShowDialog(String.format(
							"You are now logged in - %1$2s",
							user.getUserId()), "Success");
					// createTable();
					checkIfPhoneNumberExists();
					Authenticate.setClient(mClient);
					
				} else {
					createAndShowDialog("You must log in. Login Required", "Error");
				}
			}
		});
	}


	private void createTable() {

		// Get the Mobile Service Table instance to use
		mToDoTable = mClient.getTable(UserInformation.class);

		// Load the items from the Mobile Service
		// refreshItemsFromTable();
	}

	public void startUploadOwnCoordinates(View view){
		if (coordinatesAvailable(myLat, myLong)) {
			upload = new UpdatingThreads(this, item, "upload");
			upload.start();
		}
		else
			createAndShowDialog("No coordinates available", "Try again");
	}

	public double getLatitude(){
		return myLat;
	}

	public double getLongitude(){
		return myLong;
	}

	/**
	 * Creates a dialog and shows it
	 * 
	 * @param exception
	 *            The exception to show in the dialog
	 * @param title
	 *            The dialog title
	 */
	public void createAndShowDialog(Exception exception, String title) {
		Throwable ex = exception;
		if(exception.getCause() != null){
			ex = exception.getCause();
		}
		createAndShowDialog(ex.getMessage(), title);
	}

	/**
	 * Creates a dialog and shows it
	 * 
	 * @param message
	 *            The dialog message
	 * @param title
	 *            The dialog title
	 */
	public void createAndShowDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}

	public boolean coordinatesAvailable (double latitude, double longitude) {
		return latitude != NO_COORDINATE && longitude != NO_COORDINATE;
	}

	public class myLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			
			if (location.getAccuracy() <= 100){
				gpsIsAccurate = true;
			}
			
			if (location.getAccuracy() <= 100){
				greenArrow = true;
				redArrow = false;
			}
			
			else {
				redArrow = true;
				greenArrow = false;
			}
			
			if(location != null)
			{
				oldLong = myLong;
				oldLat = myLat;
				myLong = location.getLongitude();
				myLat = location.getLatitude();

				if (coordinatesAvailable(myLat, myLong)) {
					textLat.setText("My latitude: " + Double.toString(myLat));
					textLong.setText("My longitude: " + Double.toString(myLong));
				}

				if (coordinatesAvailable(palLat, palLong)) {
					textPalLat.setText("Pal latitude: " + Double.toString(palLat));
					textPalLong.setText("Pal longitude: " + Double.toString(palLong));
				}

				if (coordinatesAvailable(myLat, myLong) && coordinatesAvailable(palLat, palLong)) {
					distance = Calculations.calculateDistance(myLat, myLong, palLat, palLong);
					textDist.setText("Distance: " + Double.toString(distance));

					palBearing = Calculations.calculateBearing(palLat, palLong, myLat, myLong);
					textPalBearing.setText("Bearing to pal: " + Double.toString(palBearing));
				}

				if (coordinatesAvailable(myLat, myLong) && coordinatesAvailable(oldLat, oldLong)) {
					currentBearing = Calculations.calculateBearing(myLat, myLong, oldLat, oldLong);
					textCurrentBearing.setText("Current bearing: " + Double.toString(currentBearing));
				}

			}		
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
		}
	}
}


