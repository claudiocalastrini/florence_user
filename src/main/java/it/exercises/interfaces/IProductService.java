package it.exercises.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import it.exercises.model.io.Category;
import it.exercises.model.io.Product;

@Service
public interface IProductService {
	
		public Product addProduct(Product product);
	
		public List<Product> getAll();
	
		public Product getProductById(int productId);
		public List<Product> getAllProductByCategory(Category cat);
	
		public List<Product> getAllProductsGroupedByCategory();
	
		public Product updateProduct(Product product);
	
}
