package com.jbk.exception;

public class ProductAlreadyExistsException extends Exception{
	public ProductAlreadyExistsException() {
        super("Product already exists");
    }
}
