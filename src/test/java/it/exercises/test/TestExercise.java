package it.exercises.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale.Category;

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

import it.exercises.UserApplication;
import it.exercises.model.io.UserIn;
import it.exercises.model.io.UserOut;
import it.exercises.repository.UserRepository;
import it.exercises.service.UserService;

@SpringBootTest(classes = UserApplication.class)
@AutoConfigureMockMvc
class TestExercise {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	@BeforeTestClass
	void createScenario() {
	
	}
	@Test
	void getAllUsers() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson=ow.writeValueAsString(MockObjects.fillUserIn("mail@mail.it","cognome", "nome","indirizzo") );
		this.mockMvc.perform(post("/users/findByCondition").contentType(MediaType.APPLICATION_JSON).content(requestJson)).andExpect(status().isOk());
		
	}
	
	
	@Test
	void getUserById200() throws Exception {
		UserOut p=MockObjects.fillUserOut();
		when(userService.getUserById(1l)).thenReturn(p);
		this.mockMvc.perform(get("/users/getUserById/1")).andExpect(status().isOk()).andReturn().equals(p);
	}
	
	
	@Test
	void getUserById404() throws Exception {
	//	when(userService.getUserById(1)).thenReturn(new User());
		this.mockMvc.perform(get("/users/getUserById/1")).andExpect(status().isNotFound());
	}
	
	@Test
	void getSaveUser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(MockObjects.fillUserIn("mail@mail.it","cognome", "nome","indirizzo") );
		this.mockMvc.perform(post("/users/addUser").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isOk());
		
		
	}
	
	@Test
	void getSaveUser412Mail() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(MockObjects.fillUserIn("mailmail.it","cognome", "nome","indirizzo") );
		this.mockMvc.perform(post("/users/addUser").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isPreconditionFailed());
		
		
	}
	@Test
	void getSaveUser412Surname() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(MockObjects.fillUserOut(10,"mail@mail.it","A".repeat(101),"nome","indirizzo") );
		this.mockMvc.perform(post("/users/addUser").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isPreconditionFailed());
		
		
	}
	@Test
	void getSaveUser412Name() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(MockObjects.fillUserIn("mail@mail.it","cognome","A".repeat(101),"indirizzo") );
		this.mockMvc.perform(post("/users/addUser").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isPreconditionFailed());
		
		
	}
	@Test
	void getSaveUser412Address() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(MockObjects.fillUserIn("mail@mail.it","cognome","nome","A".repeat(501)) );
		this.mockMvc.perform(post("/users/addUser").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isPreconditionFailed());
		
		
	}
	@Test
	void updateSaveUser404() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(MockObjects.fillUserIn("mail@mail.it","cognome", "nome","indirizzo") );
		this.mockMvc.perform(put("/users/updateUser/999").contentType(MediaType.APPLICATION_JSON).content(requestJson))
        .andExpect(status().isNotFound());
	}
		
	
	
	
}