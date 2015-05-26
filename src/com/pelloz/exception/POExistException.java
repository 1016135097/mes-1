package com.pelloz.exception;

@SuppressWarnings("serial")
public class POExistException extends Exception {

	public POExistException() {
		super("The persistent object has existed!");
	}

	public POExistException(String msg) {
		super("The persistent object has existed! : " + msg);
	}

}
