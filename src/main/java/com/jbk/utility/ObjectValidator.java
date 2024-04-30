
package com.jbk.utility;

import java.util.HashMap;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.jbk.exception.ResourseNotExistsException;

import com.jbk.exception.SomethingWentWrongException;

import com.jbk.model.ProductModel;


@Component
public class ObjectValidator {
	
	
	public Map<String,String> validateProduct(ProductModel productModel) {

		Map<String, String> validationMap = new HashMap<String, String>();

		String productName = productModel.getProductName();

		double productPrice = productModel.getProductPrice();

		int productQty = productModel.getProductQty();

		int deliveryCharges = productModel.getDeliveryCharges();

		long supplierId = productModel.getSupplierId();

		long categoryId = productModel.getCategoryId();

		if (productName == null || productName.trim().equals(" ")) {

			validationMap.put("productName", "Invalid productName");
		}
		if (productPrice <= 0) {
			validationMap.put("productPrice", "product price should be greaterbthan zero");
		}
		if (productQty <= 0) {
			validationMap.put("product Qty", "product Qty must be greater than 0");
		}
		if (deliveryCharges <= 0) {
			validationMap.put("charges", "Delivery charges  must be greater than 0");
		}
		if (supplierId > 0) {
			try {
				//supplierService.getSupplierById(supplierId);

			} catch (ResourseNotExistsException e) {
				validationMap.put("Supplier", e.getMessage());

			} catch (SomethingWentWrongException e) {
				validationMap.put("supplier", e.getMessage());

			}
		} else {
			validationMap.put("supplier", "Invalid supplier Id");
		}

		if (categoryId > 0) {
			try {
				//categoryService.getCategoryById(categoryId);

			} catch (ResourseNotExistsException e) {
				validationMap.put("supplier", e.getMessage());

			} catch (SomethingWentWrongException e) {
				validationMap.put("supplier", e.getMessage());

			}
		} else {
			validationMap.put("supplier", "Invalid supplier Id");
		}

		return validationMap;

	}

}
