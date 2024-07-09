package it.exercises.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.exercises.ProductCategoryApplication;
import it.exercises.model.db.ProductDB;
import it.exercises.model.io.Category;
import it.exercises.repository.ProductRepository;

//@DataJpaTest
//@ExtendWith(SpringExtension.class)
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(classes = ProductCategoryApplication.class)
public class JpaTest {


	    @Autowired
	    ProductRepository orderRepository;

	    @BeforeEach
	    public void setUp() {
	        orderRepository.save(fillProduct(1));
	        orderRepository.save(fillProduct(2));
	    }

	    
	    @Test
		public void testProductList() {
		    assertEquals(2, orderRepository.findAll().size());
		}  
	    
	    @AfterEach
	    public void destroy() {
	        orderRepository.deleteAll();
	    }
	    private ProductDB fillProduct(int id) {
			ProductDB  p= new ProductDB();
			p.setCategory(Category.CAT1);
			p.setDescription("desc");
			p.setPrice(10.0);
			p.setQuantity(1);
			p.setProductId(id);
			return p;
		}
}
