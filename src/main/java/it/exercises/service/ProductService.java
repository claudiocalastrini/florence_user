package it.exercises.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.exercises.interfaces.IProductService;
import it.exercises.model.db.ProductDB;
import it.exercises.model.io.Category;
import it.exercises.model.io.Product;
import it.exercises.repository.ProductRepository;
@Service
public class ProductService implements IProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Override
	public Product addProduct(Product product) {
		ProductDB p=repository.save(fromIOToDb(product));
		return fromDbToIo(p);
	}

	@Override
	public List<Product> getAll() {
		List<ProductDB> prodotti=repository.findAll();
		return fillList(prodotti);
	}

	@Override
	public Product getProductById(int productId) {
		Optional<ProductDB> p=repository.findById(productId);
		if (p.isEmpty()) return null;
		else return fromDbToIo(p.get());
	}

	@Override
	public List<Product> getAllProductByCategory(Category cat) {
		List<ProductDB> prodotti=repository.findByCategory(cat);
		return fillList(prodotti);
	}

	private List<Product> fillList(List<ProductDB> prodotti) {
		return prodotti.stream().map(p -> fromDbToIo(p))
		.collect(Collectors.toList());
	}

	@Override
	public List<Product> getAllProductsGroupedByCategory() {
		List<ProductDB> prodotti=repository.findAllByOrderByCategoryDesc();
		return fillList(prodotti);
	}

	@Override
	public Product updateProduct(Product product) {
		if (repository.findById(product.getProductId()).isPresent()) return addProduct(product);
		else return null;
	}

	private Product fromDbToIo(ProductDB prodotto) {
		Product p=new Product();
		p.setProductId(prodotto.getProductId());
		p.setCategory(prodotto.getCategory());
		p.setDate(prodotto.getDate());
		p.setDescription(prodotto.getDescription());
		p.setPrice(prodotto.getPrice());
		p.setQuantity(prodotto.getQuantity());
		return p;
	}
	
	private ProductDB fromIOToDb(Product prodotto) {
		ProductDB p=new ProductDB();
		p.setProductId(prodotto.getProductId());
		p.setCategory(prodotto.getCategory());
		p.setDescription(prodotto.getDescription());
		p.setPrice(prodotto.getPrice());
		p.setQuantity(prodotto.getQuantity());
		return p;
	}
	
}
