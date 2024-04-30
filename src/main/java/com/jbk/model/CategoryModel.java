package com.jbk.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class CategoryModel {
	
	@Min(value=1,message="Invalid CategoryId")	
	private long categoryId;
	
	
	private String categoryName;
	
	@NotBlank(message = "Description Name Should Not be Blank")
	private String description;
	
	@Min(value = 1, message = "Invalid category Id")
	private int discount;
	
	private int gst;
	public CategoryModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CategoryModel(long categoryId, String categoryName, String description, int discount, int gst) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.description = description;
		this.discount = discount;
		this.gst = gst;
	}
	public long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public int getGst() {
		return gst;
	}
	public void setGst(int gst) {
		this.gst = gst;
	}
	@Override
	public String toString() {
		return "CategoryModel [categoryId=" + categoryId + ", categoryName=" + categoryName + ", description="
				+ description + ", discount=" + discount + ", gst=" + gst + "]";
	}

}
