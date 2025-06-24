package it.exercises.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.exercises.model.io.ResponseUser;
import it.exercises.model.io.UserIn;
@Component
public class UserValidator {
	public static final String OK = "OK";
	@Autowired MailValidator mailValidator;
	//TODO aggiungere oggetto errore, con codice e descrizione
	
	public ResponseUser validateUser(UserIn user) {
		ResponseUser response=new ResponseUser();
		response.setEsito(OK);
		if (!mailValidator.isValidEmail(user.getMail())) {
			response.setDescrizioneErrore("Mail non valida");
			response.setEsito("ERR_MAIL");
		}
		if (user.getName()!=null && user.getName().length()>100){
			response.setDescrizioneErrore("Nome non valido");
			response.setEsito("ERR_NAME"); 
		} 
		if (user.getSurname()!=null && user.getSurname().length()>100){
			response.setDescrizioneErrore("Cognome non valido");
			response.setEsito("ERR_SURNAME");
		} 	
		if (user.getAddress()!=null && user.getAddress().length()>500){
			response.setDescrizioneErrore("Indirizzo non valido");
			response.setEsito("ERR_ADDRESS"); 
		} 
	
		return response;
	}
}
