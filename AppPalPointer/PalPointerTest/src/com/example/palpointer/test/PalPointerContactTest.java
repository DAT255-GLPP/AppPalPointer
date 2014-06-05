package com.example.palpointer.test;

import com.example.palpointer.Contact;

import junit.framework.TestCase;

public class PalPointerContactTest extends TestCase {

	Contact contact;
	Contact contactError;
	
	protected void setUp() throws Exception {
		super.setUp();
		contact= new Contact("Pauline", "0737180580");
		contactError= new Contact("5", "43209fes");
	}
	
	public void testIsNumbers(){
		assertTrue(contact.getNr().matches("[0-9]+"));
	}
	public void testLetters(){
		assertTrue(contact.getName().matches("[a-zA-Z]+"));
	}
	public void testNameLength(){
		assertTrue(contact.getName().length()>0);
	}
	public void testTenNumbers(){
		assertTrue(contact.getNr().length()==10);
	}
	public void testErrorNumber() {
		assertFalse(contactError.getNr().matches("[0-9]+") || contactError.getNr().length()==10);
	}
	public void testErrorName() {
		assertFalse(contactError.getName().matches("[a-zA-Z]+") || contactError.getName().length()>1);
	}
}