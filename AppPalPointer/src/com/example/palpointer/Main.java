package com.example.palpointer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ImageButton loginButton = (ImageButton) findViewById(R.id.loginButton);

		// Listening to the button's event
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// Starting a new Intent
				Intent intent = new Intent(getApplicationContext(),
						ToDoActivity.class);

				// Sending data to another Activity
				startActivity(intent);
				finish();
			}
		});
	}
	
	// Alert Dialog for exiting the application. 
	@Override
	public void onBackPressed() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				Main.this);
		alertDialogBuilder.setTitle("Exit Application?");
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Main.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}
}
