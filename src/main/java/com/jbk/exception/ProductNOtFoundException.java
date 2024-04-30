package com.jbk.exception;

public class ProductNOtFoundException extends Exception {
	public ProductNOtFoundException() 
	{
        super("Product not found");
}
}