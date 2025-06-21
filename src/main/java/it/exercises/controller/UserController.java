package it.exercises.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.exercises.interfaces.IUserService;
import it.exercises.model.io.ResponseUser;
import it.exercises.model.io.User;
import it.exercises.validator.UserValidator;



@RestController
@RequestMapping("/users")
public class UserController  {


	public static final String GET_USER="getUserById/{idUtente}";
	public static final String GET_USERS="findByCondition";


	public static final String ADD_USER="addUser";
	public static final String UPDATE_USER="updateUser";
	
	public static final String RESPONSE_ERROR = "{\"esito\": \"codiceErrore\",\"descrizioneErrore\":\"string\"}}";

	
	@Autowired
	private IUserService userService;
	@Autowired
	private UserValidator validator;
	
	Logger logger = LogManager.getLogger(UserController.class);

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operazione completata con successo", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "412", description = "Errore di validazione", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = RESPONSE_ERROR) }, schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "500", description = "Errore generico", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = RESPONSE_ERROR) }, schema = @Schema(implementation = User.class))) })
	@PostMapping(value = ADD_USER)
	public ResponseEntity<ResponseUser> addUser(
			@Parameter(description = "Utente", required = true) @RequestBody User user)
			throws JsonMappingException, JsonProcessingException {

		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseUser response= validator.validateUser(user);
		if (response.getEsito().equals(UserValidator.OK)) {
			try {
				response.setUser(userService.addUser(user));
			
				status = HttpStatus.OK;
			} catch (Exception e) {
				logger.log(Level.ERROR, e);
			}
		}else status=HttpStatus.PRECONDITION_FAILED;
		

		return new ResponseEntity<>(response, status);
	}
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operazione completata con successo", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "404", description = "Utente non trovato", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = RESPONSE_ERROR) }, schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "412", description = "Errore di validazione", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = RESPONSE_ERROR) }, schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "500", description = "Errore generico", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = RESPONSE_ERROR) }, schema = @Schema(implementation = User.class))) })

	@PutMapping(value = UPDATE_USER)
	public ResponseEntity<ResponseUser> updateUser(
			@Parameter(description = "Utente", required = true) @RequestBody User user)
			throws JsonMappingException, JsonProcessingException {

		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseUser response= validator.validateUser(user);
		if (response.getEsito().equals(UserValidator.OK)) {
			try {
				response.setUser(userService.updateUser(user));
				if (response.getUser()==null) {
					status=HttpStatus.NOT_FOUND;
				}
				else {
					status = HttpStatus.OK;
				}
			} catch (Exception e) {
				logger.log(Level.ERROR, e);
			}
		}else status=HttpStatus.PRECONDITION_FAILED;
		

		return new ResponseEntity<>(response, status);
	}
	
	@PostMapping(value = GET_USERS)
	public ResponseEntity<List<User>> getUsers(@Parameter(description = "Utente", required = true) @RequestBody User user)
			throws JsonMappingException, JsonProcessingException {

		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		List<User> retvalue=new ArrayList<User>();
		try {
			retvalue=userService.getByCondition(user);
			status = HttpStatus.OK;
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage(), e);
		}

		return new ResponseEntity<>(retvalue, status);
	}
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Operazione completata con successo", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
			@ApiResponse(responseCode = "404", description = "Utente non trovato", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = RESPONSE_ERROR) }, schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "500", description = "Errore generico", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = RESPONSE_ERROR) }, schema = @Schema(implementation = User.class))) })

	@GetMapping(value = GET_USER)
	public ResponseEntity<User> getUser(
			@PathVariable(name = "idUtente", required = true) Integer idUtente)
			throws JsonMappingException, JsonProcessingException {

		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		User utente=null;
		try {
			utente=userService.getUserById(idUtente);
			if (utente==null) {
				status=HttpStatus.NOT_FOUND;
			}
			else {
				status = HttpStatus.OK;
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, e);
		}

		return new ResponseEntity<>(utente, status);
	}
}
