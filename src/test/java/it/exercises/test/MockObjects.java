package it.exercises.test;

import java.util.Locale.Category;

import it.exercises.model.db.UserDB;
import it.exercises.model.io.User;

public class MockObjects {
	  public static UserDB fillUser(int id) {
			UserDB  p= new UserDB();
			p.setUserId(id);
			
			p.setMail("mail@mail.it");
			p.setSurname("surname");
			p.setName("name");
			p.setAddress("indirizzo");
			return p;
		}
	  public static User fillUserIo(int id, String mail, String surname, String name, String address) {
			User p=MockObjects.fillUser();
			p.setUserId(id);
			p.setMail(mail);
			p.setSurname(surname);
			p.setName(name);
			p.setAddress(address);
			return p;
		}
	  public static User fillUser() {
			User p= new User();
			p.setUserId(1);
			p.setMail("mail2@mail.it");
			p.setSurname("surname");
			p.setName("name");
			p.setAddress("indirizzo");
			return p;
		} 
}
