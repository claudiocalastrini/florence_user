package it.exercises.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.exercises.ProductCategoryApplication;
import it.exercises.interfaces.IProductService;
import it.exercises.model.io.Category;
import it.exercises.model.io.Product;
import it.exercises.repository.ProductRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProductCategoryApplication.class)
class TestExerciseIntegration {
    @LocalServerPort
    private int port;	

	@Autowired
	private IProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
    private TestRestTemplate restTemplate;
	private static HttpHeaders headers;

   

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }
	@BeforeTestClass
	void createScenario() {
		productService.addProduct(fillProduct());
	}
	private String createURLWithPort(String path) {
        return "http://localhost:" + port + path;
    }
	@Test
	@Sql(statements = "INSERT INTO product(id_product, description, quantity, price) VALUES (1, 'test',  1, 1.1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "INSERT INTO product(id_product, description, quantity, price) VALUES (2, 'test2',  1, 1.1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM product WHERE id_product in (1,2) ", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testOrdersList() {
	    HttpEntity<String> entity = new HttpEntity<>(null, headers);
	    ResponseEntity<List<Product>> response = restTemplate.exchange(
	            createURLWithPort("/products/getAll"), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Product>>(){});
	    List<Product> orderList = response.getBody();
	    assert orderList != null;
	    assertTrue(response.getStatusCode().is2xxSuccessful());
	    assertEquals(orderList.size(), productService.getAll().size());
	    assertEquals(orderList.size(), productRepository.findAll().size());
	}
	
	@Test
	void getSaveProduct() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	   
	    HttpEntity<Product> entity = new HttpEntity<>(fillProductIo(10,Category.CAT1,"prodotto 10", 10.0, 1), headers);
	    ResponseEntity<Product> response = restTemplate.exchange(
	            createURLWithPort("/products/addProduct"), HttpMethod.POST, entity, new ParameterizedTypeReference<Product>(){});
	 
	    Product product = response.getBody();
	    assert product != null;
	    assertTrue(response.getStatusCode().is2xxSuccessful());
	    Product p=productService.getProductById(10);
	    assertEquals(p.getPrice(), 10.0);
	    assertEquals(p.getQuantity(), 1);
	    assertEquals(p.getProductId(), 10);
		
		
	}
	
	
	private Product fillProduct() {
		Product p= new Product();
		p.setCategory(Category.CAT1);
		p.setDescription("desc");
		p.setPrice(10.0);
		p.setQuantity(1);
		p.setProductId(1);
		return p;
	}
	
		
	
	private Product fillProductIo(int id, Category cat, String description, double price, int quantity) {
		Product p=fillProduct();
		p.setProductId(id);
		p.setCategory(cat);
		p.setDescription(description);
		p.setPrice(price);
		p.setQuantity(quantity);
		return p;
	}
	
}