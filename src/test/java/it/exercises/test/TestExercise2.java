package it.exercises.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.exercises.ProductCategoryApplication;
import it.exercises.model.io.Category;
import it.exercises.model.io.Product;
import it.exercises.model.io.TreeNode;
import it.exercises.service.CalculateAmountProductService;
import it.exercises.service.TreeNodeService;

@SpringBootTest(classes = ProductCategoryApplication.class)
public class TestExercise2 {
	
	   @Autowired CalculateAmountProductService service;
	
	@Test
	public void testNullProducts() throws Exception {
		Assertions.assertThrows(NullPointerException.class, () -> {
			service.amount(null, Category.CAT1);
		});
	    
	}
	
	@Test
	public void testEmptyProducts() throws Exception {
		List<Product> products=new ArrayList<Product>();
		assertEquals(0.0, service.amount(products, Category.CAT1));
	    
	}

	@Test
	public void testSum2ProductCategoryOk1() throws Exception {
		List<Product> products=new ArrayList<Product>();
		products.add(fillProduct(0.1,3, Category.CAT1));
		products.add(fillProduct(0.2,2, Category.CAT2));
		assertEquals(0.3, service.amount(products, Category.CAT1));
	    
	}
	
	@Test
	public void testSum2ProductCategoryOk2() throws Exception {
		List<Product> products=new ArrayList<Product>();
		products.add(fillProduct(0.1,1, Category.CAT1));
		products.add(fillProduct(0.2,2, Category.CAT1));
		assertEquals(0.5, service.amount(products, Category.CAT1));
	    
	}
	@Test
	public void testSum2ProductCategoryOk0() throws Exception {
		List<Product> products=new ArrayList<Product>();
		products.add(fillProduct(0.1,1, Category.CAT1));
		products.add(fillProduct(0.2,2, Category.CAT1));
		assertEquals(0.0, service.amount(products, Category.CAT3));
	    
	}
	private Product fillProduct(double price, int quantity, Category cat) {
		Product p=new Product();
		p.setCategory(cat);
		p.setPrice(price);
		p.setQuantity(quantity);
		return p;
	}
	
}
