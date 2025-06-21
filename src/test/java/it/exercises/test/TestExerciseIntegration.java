package it.exercises.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale.Category;

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
import com.fasterxml.jackson.databind.SerializationFeature;

import it.exercises.UserApplication;
import it.exercises.interfaces.IUserService;
import it.exercises.model.io.User;
import it.exercises.repository.UserRepository;
import jakarta.persistence.Column;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = UserApplication.class)
class TestExerciseIntegration {
    @LocalServerPort
    private int port;	

	@Autowired
	private IUserService userService;
	
	@Autowired
	private UserRepository userRepository;
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
		userService.addUser(MockObjects.fillUser());
	}
	
	@Column(name = "id_user")
	//nome, cognome, mail e indirizzo
	private int userId;
	@Column(name = "name")
	private String name;
	@Column(name = "surname")
	
	private String surname;
	@Column(name = "mail")
	private String mail;
	@Column(name = "address")
	private String address;

	@Column(name = "date_update")
	private String createURLWithPort(String path) {
        return "http://localhost:" + port + path;
    }
	@Test
	@Sql(statements = "INSERT INTO users(id_user, name, surname, mail, address) VALUES (1, 'name1',  'surname1', 'mail@mail1.it', 'indirizzo1')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "INSERT INTO users(id_user, name, surname, mail, address) VALUES (2, 'name2',  'surname2', 'mail@mail2.it', 'indirizzo2')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM users WHERE id_user in (1,2) ", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testUsersList() {
	    HttpEntity<User> entity = new HttpEntity<>(new User(), headers);
	    ResponseEntity<List<User>> response = restTemplate.exchange(
	            createURLWithPort("/users/findByCondition"), HttpMethod.POST, entity, new ParameterizedTypeReference<List<User>>(){});
	    List<User> orderList = response.getBody();
	    assert orderList != null;
	    assertTrue(response.getStatusCode().is2xxSuccessful());
	    assertEquals(orderList.size(), userService.getByCondition(new User()).size());
	    assertEquals(orderList.size(), userRepository.findAll().size());
	}
	//TODO ci sarebbero da fare i test anche per le varie condizioni di ricerca. 
	
	@Test
	void getSaveUser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    int id = 10;
	    String mail2 = "mail10@mail.it";
	    String surname2 = "cognome10";
		String name2 = "nome10";
		String address2 = "indirizzo10";
		
		HttpEntity<User> entity = new HttpEntity<>(MockObjects.fillUserIo(id,mail2,surname2,name2 , address2), headers);
	    ResponseEntity<User> response = restTemplate.exchange(
	            createURLWithPort("/users/addUser"), HttpMethod.POST, entity, new ParameterizedTypeReference<User>(){});
	 
	    User user = response.getBody();
	    assert user != null;
	    assertTrue(response.getStatusCode().is2xxSuccessful());
	    User p=userService.getUserById(id);
	    assertEquals(p.getAddress(), address2);
	    assertEquals(p.getName(), name2);
	    assertEquals(p.getSurname(), surname2);
	    assertEquals(p.getMail(), mail2);
	    assertEquals(p.getUserId(), id);
		
		
	}
	
	
	
	
		
	
	
	
}