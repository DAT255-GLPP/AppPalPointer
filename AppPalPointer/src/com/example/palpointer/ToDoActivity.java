package com.example.palpointer;

import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

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
import android.widget.Toast;
import android.widget.ToggleButton;

public class ToDoActivity extends Activity implements SensorEventListener{

	TextView textDistance;
	ToggleButton toggleButton;

	final static double NO_COORDINATE = -1000;
	final static float HIGH_ACCURACY = 30;
	final static float LOW_ACCURACY = 100;

	double oldLat = NO_COORDINATE;
	double oldLong = NO_COORDINATE;
	double myLat = NO_COORDINATE;
	double myLong = NO_COORDINATE;
	double palLat = NO_COORDINATE;
	double palLong = NO_COORDINATE;
	double palBearing = 0;
	int distance = 0;
	double precisionOfGps;

	boolean keepUploadingCoordinates = false;
	boolean keepDownloadingPalsCoordinates = false;

	Thread threadForUploadingOwnCoordinates;
	Thread threadForUploadingPalsCoordinates;
	String phoneNumber;
	public static String contactNumber;

	UpdatingThreads download;
	UpdatingThreads upload;

	//boolean isAutethenticated = false;

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
	// define the display assembly compass picture
	private ImageView arrowImage;

	// record the compass picture angle turned
	private float currentDegree = 0f;

	// device sensor manager
	private SensorManager mSensorManager;

	public MobileServiceUser user;
	UserInformation item;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do);
		setCompassLayout();

		textDistance = (TextView)findViewById(R.id.textDistance);
		toggleButton = (ToggleButton)findViewById(R.id.toggleButton);

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

		Button palsButton = (Button) findViewById(R.id.palsButton);
		//Listening to first button's event
		palsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				//Starting a new Intent
				Intent contactScreen = new Intent(getApplicationContext(), ContactList.class);
				//Sending data to another Activity
				startActivity(contactScreen);
			}
		});

		Button changeNumber = (Button) findViewById(R.id.changeNumber);

		//Listening to second button's event
		changeNumber.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg1) {
				updatePhoneNumber();
			}
		});

		if (Authenticate.getUploadThread() != null) {
			toggleButton.setChecked(true);
		}
	}

	public void checkIfPhoneNumberExists(){
		mToDoTable.where().field("userid").eq(user.getUserId()).execute(new TableQueryCallback<UserInformation>() {

			@Override
			public void onCompleted(List<UserInformation> entity, int count, Exception exception, ServiceFilterResponse response) {
				if (exception == null) {

					if (entity.isEmpty()){
						item = new UserInformation();
						Authenticate.setUser(item);
						setIdAndPhoneNumber();	
					}
					else{						
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

	public void updatePhoneNumber(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Update information");
		alert.setMessage("Enter your new phonenumber");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();

				if (value.equals("")) {
					Toast.makeText(getBaseContext(),  "You must enter a phonenumber",  Toast.LENGTH_SHORT).show();
				}

				else if (!value.matches("[0-9]+")) {
					Toast.makeText(getBaseContext(),  "Only digits are allowed",  Toast.LENGTH_SHORT).show();
				}

				else if (value.length() != 10) {
					Toast.makeText(getBaseContext(),  "The number has to be 10 digits long",  Toast.LENGTH_SHORT).show();
				}

				else {
					item.setPhoneNumber(value);

					mToDoTable.update(item, new TableOperationCallback<UserInformation>() {

						public void onCompleted(UserInformation entity, Exception exception, ServiceFilterResponse response) {

							if (exception != null) {
								createAndShowDialog(exception, "Error");
							}
						}
					});
					Toast.makeText(getBaseContext(),  "Your number has been updated to " + value,  Toast.LENGTH_SHORT).show();
				}
			}
		});
		alert.show();
	}

	public void startDownloadingPalsPosition(){
		download = new UpdatingThreads(this, item, "download");
		download.start();
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
			arrowImage.setVisibility(View.VISIBLE);
		}
	}

	public void stopUploadingCoordinates(View view){
		upload = Authenticate.getUploadThread();
		if (upload != null){
			upload.setWhileLoopStatus(false);
			upload = null;
			Authenticate.setUploadThread(upload);
			Toast.makeText(getBaseContext(),  "You are no longer uploading your coordinates!",  Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(getBaseContext(),  "You have not started to upload your coordinates yet",  Toast.LENGTH_SHORT).show();
		}
	}

	public void setCompassLayout(){
		// our compass image
		arrowImage = (ImageView) findViewById(R.id.arrow);

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

		if (contactNumber != null && gpsIsAccurate && coordinatesAvailable(palLat, palLong)){
			// get the angle around the z-axis rotated
			degree = Math.round((event.values[0]+palBearing) % 360);
		}

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
		arrowImage.startAnimation(ra);
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
					Toast.makeText(getBaseContext(),  "You are now logged in!",  Toast.LENGTH_SHORT).show();
					// createTable();
					checkIfPhoneNumberExists();
					Authenticate.setClient(mClient);

				} else {
					Toast.makeText(getBaseContext(),  "Error during login",  Toast.LENGTH_SHORT).show();
					//Starting a new Intent
					Intent intent = new Intent(getApplicationContext(), Main.class);
					//Sending data to another Activity
					startActivity(intent);
				}
			}
		});
	}


	private void createTable() {

		// Get the Mobile Service Table instance to use
		mToDoTable = mClient.getTable(UserInformation.class);
	}

	public void startUploadOwnCoordinates(View view){
		upload = Authenticate.getUploadThread();
		if (coordinatesAvailable(myLat, myLong) && upload == null) {
			upload = new UpdatingThreads(this, item, "upload");
			upload.start();
			Authenticate.setUploadThread(upload);
			Toast.makeText(getBaseContext(),  "You are now uploading your coordinates!",  Toast.LENGTH_SHORT).show();
		}
		else if (!coordinatesAvailable(myLat, myLong)){
			Toast.makeText(getBaseContext(),  "Coordinates not available, not uploading!",  Toast.LENGTH_SHORT).show();
			((ToggleButton) view).setChecked(false);
		}


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

	public void onToggleClicked (View view) {
		boolean on = ((ToggleButton) view).isChecked();

		if (on) {
			startUploadOwnCoordinates(view);
		}
		else {
			stopUploadingCoordinates(view);
		}
	}

	public class myLocationListener implements LocationListener {
		float[] resultArray = new float[3];

		public void updateCoordinates(Location location){
			oldLong = myLong;
			oldLat = myLat;
			myLong = location.getLongitude();
			myLat = location.getLatitude();	
		}

		public void updateDistance(Location location){
			if (coordinatesAvailable(myLat, myLong) && coordinatesAvailable(palLat, palLong)) {

				Location.distanceBetween(myLat, myLong, palLat, palLong, resultArray);
				distance = Math.round(resultArray[0]);
				textDistance.setText("Distance to pal: " + Integer.toString(distance) + " meters.");

				palBearing = Math.round(resultArray[1]);
			}
		}

		@Override
		public void onLocationChanged(Location location) {

			if (contactNumber != null && location.getAccuracy() <= LOW_ACCURACY) {
				gpsIsAccurate = true;
			}

			if (!gpsIsAccurate && contactNumber != null){
				if (location.getAccuracy() <= LOW_ACCURACY){
					gpsIsAccurate = true;
					precisionOfGps = location.getAccuracy();
					if (precisionOfGps <= HIGH_ACCURACY){
						arrowImage.setImageResource(R.drawable.arrow_green);
					}
					else {
						arrowImage.setImageResource(R.drawable.arrow_red);
					}
				}
			}

			if (location != null) {
				updateCoordinates(location);
				updateDistance(location);
			}

			if (contactNumber != null && coordinatesAvailable(palLat, palLong)){

				if (precisionOfGps > HIGH_ACCURACY && location.getAccuracy() <= HIGH_ACCURACY){
					arrowImage.setImageResource(R.drawable.arrow_green);
					greenArrow = true;
					redArrow = false;
					precisionOfGps = location.getAccuracy();
				}

				else if (precisionOfGps <= HIGH_ACCURACY && location.getAccuracy() > HIGH_ACCURACY) {
					arrowImage.setImageResource(R.drawable.arrow_red);
					redArrow = true;
					greenArrow = false;
					precisionOfGps = location.getAccuracy();
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


