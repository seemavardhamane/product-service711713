package com.jbk.dao.impl;

import java.util.List;

import javax.persistence.RollbackException;

import org.hibernate.Query;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
//import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbk.dao.ProductDao;
import com.jbk.entity.ProductEntity;
import com.jbk.exception.ResourceAlreadyExists;
import com.jbk.exception.ResourseNotExistsException;
import com.jbk.exception.SomethingWentWrongException;

@SuppressWarnings("deprecation")
@Repository

public class ProductDaoImpl implements ProductDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean addProduct(ProductEntity productEntity) {
		boolean isAdded = false;
		try (Session session = sessionFactory.openSession()) {
			Transaction transaction;

			// check its exists or not
			ProductEntity dbEntity = getProductByName(productEntity.getProductName());

			if (dbEntity == null) {
				session.save(productEntity); // insert into
				transaction = session.beginTransaction();
				transaction.commit();
				isAdded = true;

				// session=null;
			} else {
				throw new ResourceAlreadyExists(
						"Product Already Exists with Id : " + productEntity.getProductId());
			}

		}

		catch (RollbackException e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong in during add product, check unique fields");
		}

		return isAdded;
	}


	@Override
	public ProductEntity getProductById(long productId) {
		ProductEntity productEntity = null;
		try {
			Session session = sessionFactory.openSession();

			productEntity = session.get(ProductEntity.class, productId);

		} catch (HibernateException e) {
			throw new SomethingWentWrongException("Connection Failure");
		}
		return productEntity;
	}

	@Override
	public boolean deleteProductById(long productId) {

		boolean isDeleted = false;
		try {
			Session session = sessionFactory.openSession();

			ProductEntity dbEntity = session.get(ProductEntity.class, productId);
			if (dbEntity != null) {
				session.delete(dbEntity);
				session.beginTransaction().commit();
				isDeleted = true;
			} else {
				throw new ResourseNotExistsException("Resourse ot found");
			}
		} catch (Exception e) {
			throw new SomethingWentWrongException("Something went wrong during delete product");
		}
		return isDeleted;
	}

	@Override
	public boolean updateProduct(ProductEntity productEntity) {
		boolean isUpdated = false;
		try {
			Session session = sessionFactory.openSession();

			ProductEntity dbProduct = getProductById(productEntity.getProductId());

			if (dbProduct != null) {
				session.update(productEntity);
				session.beginTransaction().commit();
				isUpdated = true;

			} else {
				isUpdated = false;
			}

		} catch (Exception e) {
			throw new SomethingWentWrongException("something went wrong during update");

		}
		return isUpdated;
	}

	@Override
	public List<ProductEntity> getAllProducts() {
		List<ProductEntity> list = null;
		try {
			Session session = sessionFactory.openSession();

			Criteria criteria = session.createCriteria(ProductEntity.class);

			list = criteria.list();

		} catch (Exception e) {
			throw new SomethingWentWrongException("something went wrong while retriving all products  ");
		}
		return list;
	}

	@Override
	public List<ProductEntity> sortProduct(String orderType, String property) {
		List<ProductEntity> list = null;
		try {
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ProductEntity.class);

			if (orderType.equalsIgnoreCase("desc")) {
				criteria.addOrder(Order.desc(property));
			} else {
				criteria.addOrder(Order.asc(property));
			}

			list = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public double getMaxProductPrice() {
		double maxPrice = 0;
		try {
			Session session = sessionFactory.openSession();

			Criteria criteria = session.createCriteria(ProductEntity.class);

			Projection productPriceProjection = Projections.max("productPrice");

			criteria.setProjection(productPriceProjection); /// select max(productPrice) from ProductEntity

			List list = criteria.list();
			if (!list.isEmpty()) {
				maxPrice = (double) list.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return maxPrice;
	}

	@Override
	public List<ProductEntity> getMaxPriceProduct() {
		double maxProductPrice = getMaxProductPrice();
		List<ProductEntity> list = null;
		if (maxProductPrice > 0) {

			// find max price product
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ProductEntity.class);

			// select * from product where product_price=?
			criteria.add(Restrictions.eq("productPrice", maxProductPrice)); // filter query

			list = criteria.list();

		} else {
			throw new ResourseNotExistsException("Product Not Exists");
		}

		return list;

	}

	@Override
	public ProductEntity getProductByName(String productName) {

		List<ProductEntity> list = null;
		ProductEntity productEntity = null;
		try {
			Session session = sessionFactory.openSession();

			Query<ProductEntity> query = session.createQuery("FROM ProductEntity WHERE productName= :name");

			query.setParameter("name", productName);

			list = query.list();

			if (!list.isEmpty()) {
				productEntity = list.get(0);
			} else {
				throw new ResourseNotExistsException("Product Not Exists");
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return productEntity;
	}

	@Override
	public List<ProductEntity> getAllProducts(double low, double high) {
		List<ProductEntity> list = null;
		try {
			Session session = sessionFactory.openSession();
			// criteria
			Criteria criteria = session.createCriteria(ProductEntity.class);

			list = criteria.list(); // select * from product

		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something Went Wrong During retrive all Product");
		}
		return list;
	}

	@Override
	public List<ProductEntity> getProductStartWith(String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double productPriceAverage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double countOfTotalProducts() {
		// TODO Auto-generated method stub
		return 0;
	}

}