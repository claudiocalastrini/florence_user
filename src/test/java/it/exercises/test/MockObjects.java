package it.exercises.test;

import java.util.Locale.Category;

import it.exercises.model.db.UserDB;
import it.exercises.model.io.UserIn;
import it.exercises.model.io.UserOut;

public class MockObjects {
	  public static UserDB fillUser(long id) {
			UserDB  p= new UserDB();
			p.setUserId(id);
			
			p.setMail("mail@mail.it");
			p.setSurname("surname");
			p.setName("name");
			p.setAddress("indirizzo");
			return p;
		}
	  public static UserOut fillUserOut(long id, String mail, String surname, String name, String address) {
		  	UserOut p= new UserOut();
			p.setUserId(id);
			p.setMail(mail);
			p.setSurname(surname);
			p.setName(name);
			p.setAddress(address);
			return p;
		}
	  public static UserIn fillUserIn(String mail, String surname, String name, String address) {
		  	UserIn p= new UserIn();

			p.setMail(mail);
			p.setSurname(surname);
			p.setName(name);
			p.setAddress(address);
			return p;
		}
	  
	  public static UserOut fillUserOut() {
			UserOut p= new UserOut();
			p.setUserId(1);
			p.setMail("mail2@mail.it");
			p.setSurname("surname");
			p.setName("name");
			p.setAddress("indirizzo");
			return p;
		} 
	  
	  public static UserIn fillUserIn() {
			UserIn p= new UserIn();
			
			p.setMail("mail2@mail.it");
			p.setSurname("surname");
			p.setName("name");
			p.setAddress("indirizzo");
			return p;
		} 
}
