package it.exercises.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.exercises.ProductCategoryApplication;
import it.exercises.model.io.Category;
import it.exercises.model.io.Product;
import it.exercises.repository.ProductRepository;
import it.exercises.service.ProductService;

@SpringBootTest(classes = ProductCategoryApplication.class)
@AutoConfigureMockMvc
class TestExercise3 {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;
	
	@MockBean
	private ProductRepository productRepository;
	
	@BeforeTestClass
	void createScenario() {
	
	}
	@Test
	void getAllProducts() throws Exception {
		
		this.mockMvc.perform(get("/products/getAll")).andExpect(status().isOk());
	}
	
	@Test
	void getAllProductsGroupByCategory() throws Exception {
		
		this.mockMvc.perform(get("/products/getAllGrouped")).andExpect(status().isOk());
	}
	
	@Test
	void getProductsByCategory() throws Exception {
		
		this.mockMvc.perform(get("/products/getProductByCategory/CAT1")).andExpect(status().isOk());
	}
	@Test
	void getAllProductById200() throws Exception {
		Product p=fillProduct();
		when(productService.getProductById(1)).thenReturn(p);
		this.mockMvc.perform(get("/products/getProductById/1")).andExpect(status().isOk()).andReturn().equals(p);
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
	
	@Test
	void getAllProductById404() throws Exception {
	//	when(productService.getProductById(1)).thenReturn(new Product());
		this.mockMvc.perform(get("/products/getProductById/1")).andExpect(status().isNotFound());
	}
	
	@Test
	void getSaveProduct() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(fillProductIo(10,Category.CAT1,"prodotto 10", 10.0, 1) );
		this.mockMvc.perform(post("/products/addProduct").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isOk());
		
		
	}
	
	@Test
	void getSaveProduct412Qta() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(fillProductIo(10,Category.CAT1,"prodotto 10", 10.0, -1) );
		this.mockMvc.perform(post("/products/addProduct").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isPreconditionFailed());
		
		
	}
	@Test
	void getSaveProduct412Price() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(fillProductIo(10,Category.CAT1,"prodotto 10", -10.0, 1) );
		this.mockMvc.perform(post("/products/addProduct").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isPreconditionFailed());
		
		
	}
	@Test
	void updateSaveProduct404() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(fillProductIo(10,Category.CAT1,"prodotto 10", 10.0, 1) );
		this.mockMvc.perform(put("/products/updateProduct").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isNotFound());
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