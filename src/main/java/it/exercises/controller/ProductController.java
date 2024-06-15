package it.exercises.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


import io.swagger.v3.oas.annotations.Parameter;
import it.exercises.interfaces.IProductService;
import it.exercises.model.io.Category;
import it.exercises.model.io.Product;
import it.exercises.validator.ProductValidator;


@RestController
@RequestMapping("/products")
public class ProductController  {


	public static final String GET_PRODUCT="getProductById/{idProdotto}";
	public static final String GET_ALL_PRODUCTS="getAll";
	public static final String GET_ALL_GROUPED_PRODUCTS="getAllGrouped";
	public static final String GET_BY_CATEGORY="getProductByCategory/{category}";
	public static final String ADD_PRODUCT="addProduct";
	public static final String UPDATE_PRODUCT="updateProduct";
	
	@Autowired
	private IProductService productService;
	@Autowired
	private ProductValidator validator;
	
	Logger logger = LogManager.getLogger(ProductController.class);

	@PostMapping(value = ADD_PRODUCT)
	public ResponseEntity<Product> addProduct(
			@Parameter(description = "Prodotto", required = true) @RequestBody Product product)
			throws JsonMappingException, JsonProcessingException {

		Product prodinsert=null;
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		if (validator.validateProduct(product)) {
			try {
				prodinsert=productService.addProduct(product);
			
				status = HttpStatus.OK;
			} catch (Exception e) {
				logger.log(Level.ERROR, e);
			}
		}else status=HttpStatus.PRECONDITION_FAILED;
		

		return new ResponseEntity<>(prodinsert, status);
	}
	@PutMapping(value = UPDATE_PRODUCT)
	public ResponseEntity<Product> updateProduct(
			@Parameter(description = "Prodotto", required = true) @RequestBody Product product)
			throws JsonMappingException, JsonProcessingException {

		Product produpdate=null;
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		if (validator.validateProduct(product)) {
			try {
				produpdate=productService.updateProduct(product);
				if (produpdate==null) {
					status=HttpStatus.NOT_FOUND;
				}
				else {
					status = HttpStatus.OK;
				}
			} catch (Exception e) {
				logger.log(Level.ERROR, e);
			}
		}else status=HttpStatus.PRECONDITION_FAILED;
		

		return new ResponseEntity<>(produpdate, status);
	}
	@GetMapping(value = GET_ALL_GROUPED_PRODUCTS)
	public ResponseEntity<List<Product>> getAllGroupedProducts()
			throws JsonMappingException, JsonProcessingException {

		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		List<Product> retvalue=new ArrayList<Product>();
		try {
			retvalue=productService.getAllProductsGroupedByCategory();
			status = HttpStatus.OK;
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}

		return new ResponseEntity<>(retvalue, status);
	}
	@GetMapping(value = GET_ALL_PRODUCTS)
	public ResponseEntity<List<Product>> getAllProducts()
			throws JsonMappingException, JsonProcessingException {

		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		List<Product> retvalue=new ArrayList<Product>();
		try {
			retvalue=productService.getAll();
			status = HttpStatus.OK;
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}

		return new ResponseEntity<>(retvalue, status);
	}
	
	@GetMapping(value = GET_BY_CATEGORY)
	public ResponseEntity<List<Product>> getByCategoAllProducts(
			@PathVariable(name = "category", required = true) String category
			)
			throws JsonMappingException, JsonProcessingException {

		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		List<Product> retvalue=new ArrayList<Product>();
		try {
			retvalue=productService.getAllProductByCategory(Category.valueOf(category));
			status = HttpStatus.OK;
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}

		return new ResponseEntity<>(retvalue, status);
	}
	
	@GetMapping(value = GET_PRODUCT)
	public ResponseEntity<Product> getProduct(
			@PathVariable(name = "idProdotto", required = true) Integer idProdotto)
			throws JsonMappingException, JsonProcessingException {

		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Product prodotto=null;
		try {
			prodotto=productService.getProductById(idProdotto);
			if (prodotto==null) {
				status=HttpStatus.NOT_FOUND;
			}
			else {
				status = HttpStatus.OK;
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}

		return new ResponseEntity<>(prodotto, status);
	}
}
