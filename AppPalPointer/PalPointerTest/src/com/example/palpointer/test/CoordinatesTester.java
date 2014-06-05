package com.example.palpointer.test;

import junit.framework.TestCase;
import com.example.palpointer.ToDoActivity;

public class CoordinatesTester extends TestCase {
	ToDoActivity t;

	protected void setUp() throws Exception {
		super.setUp();
		t=new ToDoActivity();
	}
	
	public void testCoordinates() {
		assertFalse(t.coordinatesAvailable(t.getLatitude(),t.getLongitude()));
	}

	public void testCoordinates2() {
		assertTrue(t.coordinatesAvailable(23.2312,1.231321));
	}
	
	public void testCoordinates3() {
		assertFalse(t.coordinatesAvailable(-1000,-1000));
	}
}
