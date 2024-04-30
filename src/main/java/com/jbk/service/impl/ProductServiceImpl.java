package com.jbk.service.impl;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.jbk.dao.ProductDao;
import com.jbk.entity.ProductEntity;
import com.jbk.exception.ResourceAlreadyExists;
import com.jbk.exception.ResourseNotExistsException;
import com.jbk.exception.SomethingWentWrongException;
import com.jbk.model.CategoryModel;
import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;
import com.jbk.model.SupplierModel;
import com.jbk.service.ProductService;
import com.jbk.utility.ObjectValidator;

@Service
public class ProductServiceImpl implements ProductService {
	

	Map<Integer, Map<String, String>> rowError = new HashMap<Integer, Map<String, String>>();
	
	Map<String, Object> finalMap = new LinkedHashMap<String, Object>();
	
	List <Integer>rowNumList=new ArrayList<Integer>();
	
	Map<String, String> validationMap = new HashMap<String, String>();

	int totalRecord;
	
	
	@Autowired
	private RestTemplate restTemplate;
	
		@Autowired
		ProductDao dao;

		@Autowired
		private ModelMapper mapper;
		
		@Autowired
		private ObjectValidator validator;


		@Override
		public boolean addProduct(ProductModel productModel) {
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
			productModel.setProductId(Long.parseLong(timeStamp));

			ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);

			return dao.addProduct(productEntity);
		}

		@Override
		public ProductModel getProductById(long productId) {

			ProductEntity productEntity = dao.getProductById(productId);

			if (productEntity != null) {
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				return productModel;
			} else {
				throw new ResourseNotExistsException("Product Not Exists ID = " + productId);
			}

		}

	


	@Override
	public boolean deleteProductById(long productId) {
		ProductEntity product = dao.getProductById(productId);
		if (product != null) {
			return dao.deleteProductById(productId);
		} else {
			return false; // Product not found
		}
	}

	@Override
	public boolean updateProduct(ProductModel productModel) {
		ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);

		boolean isUpdated = dao.updateProduct(productEntity);
		if (isUpdated == false) {
			throw new ResourseNotExistsException("product not existException with ID:" + productEntity.getProductId());

		}
		return dao.updateProduct(productEntity);
	}

	@Override
	public List<ProductModel> getAllProducts() {
		List<ProductEntity> entityList = dao.getAllProducts();
		List<ProductModel> modelList = new ArrayList<ProductModel>();
		
		if (!entityList.isEmpty()) {
			for (ProductEntity productEntity : entityList) {
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);
			}
			return modelList;
		} else {
			throw new ResourseNotExistsException("product not exist");
		}
	}

	@Override
	public List<ProductModel> sortProduct(String orderType, String property) {
		List<ProductEntity> entityList = dao.sortProduct(orderType, property);
		List<ProductModel> modelList = new ArrayList<ProductModel>();
		if (!entityList.isEmpty()) {

			for (ProductEntity productEntity : entityList) {

				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);

			}

			return modelList;
		} else {
			throw new ResourseNotExistsException("Product Not Exists");
		}
	}

	@Override
	public double getMaxProductPrice() {
		double maxProductPrice = dao.getMaxProductPrice();
		if (maxProductPrice > 0) {
			return maxProductPrice;
		} else {
			throw new ResourseNotExistsException("Product Not Exists");
		}

	}

	@Override
	public List<ProductModel> getMaxPriceProduct() {
		List<ProductEntity> list = dao.getMaxPriceProduct();

		List<ProductModel> modelList = new ArrayList<ProductModel>();
//			
//			for (ProductEntity productEntity : list) {
//				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
//				modelList.add(productModel);
//			}

		modelList = list.stream().map(productEntity -> mapper.map(productEntity, ProductModel.class))
				.collect(Collectors.toList());

		return modelList;
	}

	@Override
	public ProductModel getProductByName(String productName) {
		ProductEntity dbproduct = dao.getProductByName(productName);
		
		if(dbproduct!=null) {
			return mapper.map(dao.getProductByName(productName), ProductModel.class);
		}else
			return null;

		
	}

	@Override
	public List<ProductModel> getAllProducts(double lowPrice, double highPrice) {

		return null;
	}

	@Override
	public List<ProductModel> getProductStartWith(String expression) {
	
				return null;
	}

	@Override
	public double productPriceAverage() {
		
		return 0;
	}

	@Override
	public double countOfTotalProducts() {
		
		return 0;
	}

	@Override
	public List<ProductModel> getAllProducts(long category, long supplier) {
		
		return null;
	}

	@Override
	public List<ProductModel> getAllProducts(String supplier) {
		
		return null;
	}

	private List<ProductModel> readExcel(String filePath) {
		List<ProductModel> list = new ArrayList<>();
		// pointout file
		try {
			FileInputStream fis = new FileInputStream(filePath);

			//new HSSFWorkbookFactory();
			Workbook workbook = WorkbookFactory.create(fis);

			// pointout sheet

			Sheet sheet = workbook.getSheetAt(0);

			totalRecord = sheet.getLastRowNum();

			// rows

			Iterator<Row> rows = sheet.rowIterator();

			// iterate rows and pointput every row

			while (rows.hasNext())
				try {
					{
						Row row = rows.next();
						int rowNum = row.getRowNum();
						if (rowNum == 0) {
							continue;
						}
						// for everyrow we will create one productModel obj

						ProductModel productModel = new ProductModel();
						String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
						productModel.setProductId(Long.parseLong(timeStamp) + rowNum);
						// iterate row and point out every cell

						Iterator<Cell> cells = row.cellIterator();
						while (cells.hasNext()) {
							Cell cell = cells.next();

							//CellType cellType = cell.getCellType();
							// get data

							int columnIndex = cell.getColumnIndex();
							switch (columnIndex) {
							case 0: {
								String productName = cell.getStringCellValue();
								productModel.setProductName(productName);
								break;
							}
							case 1: {
							    long supplierId = (long) cell.getNumericCellValue(); // Cast to long directly
							    productModel.setSupplierId(supplierId);
							    break;
							}
							case 2: {
							    long categoryId = (long) cell.getNumericCellValue(); // Cast to long directly
							    productModel.setCategoryId(categoryId);
							    break;
							}

//							case 1: {
//
//								double supplierId = cell.getNumericCellValue();
//								
//								productModel.setSupplierId((long)cell.getNumericCellValue());
//								break;
//							}
//							case 2: {
//								double categoryId = cell.getNumericCellValue();
//								
//								productModel.setCategoryId((long)cell.getNumericCellValue());
//
//								break;
//							}
							case 3: {
								double productQty = cell.getNumericCellValue();
								productModel.setProductQty((int) productQty);

								break;
							}
							case 4: {
								double productPrice = cell.getNumericCellValue();
								productModel.setProductPrice(productPrice);
								break;
							}
							case 5: {
								double charges = cell.getNumericCellValue();
								productModel.setDeliveryCharges((int) charges);
								break;
							}

							}
						}
//data validation
						 validationMap = validator.validateProduct(productModel);
						if (!validationMap.isEmpty()) {
							// add validation map with rownum
							rowError.put(rowNum + 1, validationMap);
						} else  {
							ProductModel dbProduct = getProductByName(productModel.getProductName());
							if(dbProduct!=null) {
							rowNumList.add(rowNum+1);
						}else {
							
							list.add(productModel);
						}
						
						}
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception

		}

		return list;
	}

	@Override
	public Map<String, Object> uploadSheet(MultipartFile myfile) {
		String msg = "";
		int isAddedCounter = 0;
		int alreadyExistCounter = 0;
		int issueCounter = 0;
		try {
			String path = "uploaded";
			String fileName = myfile.getOriginalFilename();
			FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);
			byte[] data = myfile.getBytes();
			fos.write(data);

			// reading ExcelFile

			List<ProductModel> list = readExcel(path + File.separator + fileName);

			for (ProductModel productModel : list) {
				System.out.println(productModel);
				ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);

				try {
					boolean isAdded = dao.addProduct(productEntity);
					if (isAdded) {
						isAddedCounter = isAddedCounter + 1;
					}
				} catch (ResourceAlreadyExists e) {
					alreadyExistCounter = alreadyExistCounter + 1;

				} catch (SomethingWentWrongException e) {
					issueCounter = issueCounter + 1;
				}
			}

			finalMap.put("totalRecords In sheet", list);
			
			finalMap.put("uploaded record in Db", isAddedCounter);
			
			finalMap.put("total Exists Record in Db",rowNumList.size());
			
			finalMap.put("Rows Num,Exists Record in DB", rowNumList);
			
			finalMap.put("Total Excluded Record count", rowError.size());
			
			finalMap.put("Bad Record Row Number",rowError);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalMap;
	}

	@Override
	public Product_Supplier_Category getProductWithScByPId(long productId) {
		 
		ProductModel productModel=getProductById(productId);
		SupplierModel supplierModel =restTemplate.getForObject("http://localhost:8082/supplier/get-supplier-by-id/"+productModel.getSupplierId(),SupplierModel.class);
		
		
		CategoryModel categoryModel =restTemplate.getForObject("http://localhost:8083/Category/get-Category-by-id/"+productModel.getCategoryId(),CategoryModel.class);
		
		Product_Supplier_Category psc = new Product_Supplier_Category(productModel,supplierModel,categoryModel);		
		return psc;
	}
}
