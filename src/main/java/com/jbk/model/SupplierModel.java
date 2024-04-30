package com.jbk.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SupplierModel {
	
	@Min(value=1,message="Invalid Supplier Id")
	private long supplierId;
	
	//@NotBlank(message="supplier name should not be blank")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Supplier name must contain only alphabets and spaces")
	private String supplierName;
	
	@NotBlank(message="city name should not be blank")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "city name must contain only alphabets and spaces")
	private String city;
	
	@Min(value=100000,message="Invalid postal code")
	@Max(value=999999,message="Invalid postal code")
	private int postalcode;
	
	@NotBlank(message="country name should not be blank")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "country name must contain only alphabets and spaces")
	private String country;
	
	@Pattern(regexp="^[1-9][0-9]{9}$",message="mobile number should only contain digits not start with 0 and be 10 digits long")
	private String mobile;
	
	public SupplierModel() {
		super();
		
	}

	public SupplierModel(long supplierId, String supplierName, String city,int postalcode, String country, String mobile) {
		super();
		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.city=city;
		this.postalcode = postalcode;
		this.country = country;
		this.mobile = mobile;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCity() {
		return city;
	}
	public void setcity(String city) {
		this.city=city;
	}
	public int getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(int postalcode) {
		this.postalcode = postalcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "SupplierModel [supplierId=" + supplierId + ", supplierName=" + supplierName +"city="+city +" postalcode="
				+ postalcode + ", country=" + country + ", mobile=" + mobile + "]";
	}
	
	

}
