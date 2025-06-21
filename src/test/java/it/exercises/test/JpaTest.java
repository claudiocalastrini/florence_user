package it.exercises.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale.Category;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.exercises.UserApplication;
import it.exercises.model.db.UserDB;
import it.exercises.repository.UserRepository;

//@DataJpaTest
//@ExtendWith(SpringExtension.class)
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@SpringBootTest(classes = UserApplication.class)
public class JpaTest {


	    @Autowired
	    UserRepository userRepository;

	    @BeforeEach
	    public void setUp() {
	        userRepository.save(MockObjects.fillUser(1));
	        userRepository.save(MockObjects.fillUser(2));
	    }

	    
	    @Test
		public void testUserList() {
		    assertEquals(2, userRepository.findAll().size());
		}  
	    
	    @AfterEach
	    public void destroy() {
	        userRepository.deleteAll();
	    }
	  
}
