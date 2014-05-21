package com.example.palpointer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {
	
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);    

        Button fndFriend = (Button) findViewById(R.id.friendsPos);
    
        //Listening to first button's event
        fndFriend.setOnClickListener(new View.OnClickListener() {
    	
        	public void onClick(View arg0) {
        		
        		//Starting a new Intent
        		Intent nextScreen = new Intent(getApplicationContext(), ToDoActivity.class);
        		

        		//Sending data to another Activity
        		startActivity(nextScreen);
        	}
        });
   
        Button sendPos = (Button) findViewById(R.id.myPos);
    
        //Listening to second button's event
        sendPos.setOnClickListener(new View.OnClickListener() {
    	
        	public void onClick(View arg1) {
        		TextView positionSent = (TextView)findViewById(R.id.posSent);
        		positionSent.setText("Position sent to database.");
    
        	}
        });
   }
}





