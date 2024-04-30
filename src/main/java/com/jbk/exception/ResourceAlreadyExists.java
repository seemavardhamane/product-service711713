package com.jbk.exception;

public class ResourceAlreadyExists extends RuntimeException {
	public ResourceAlreadyExists(String msg) {
		super(msg);
	}
}
