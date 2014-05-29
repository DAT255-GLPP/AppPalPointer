package com.example.palpointer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class Main extends Activity {	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);    

		ImageButton loginButton = (ImageButton) findViewById(R.id.loginButton);

		//Listening to the button's event
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				//Starting a new Intent
				Intent intent = new Intent(getApplicationContext(), ToDoActivity.class);     		

				//Sending data to another Activity
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
	}
}
