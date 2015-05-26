package com.pelloz.exception;

@SuppressWarnings("serial")
public class NoSuchPOException extends Exception {

	public NoSuchPOException(String usermsg) {
		super("Can't find the persistent object! : " + usermsg);
	}
	
	public NoSuchPOException() {
		super("Can't find the persistent object");
	}

}
