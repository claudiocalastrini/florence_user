package it.exercises.service;

import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.exercises.interfaces.IUserService;
import it.exercises.model.db.UserDB;
import it.exercises.model.io.UserIn;
import it.exercises.model.io.UserOut;
import it.exercises.repository.UserRepository;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public UserOut addUser(UserIn user) {
		UserDB p=repository.save(newUserToDb(user));
		return fromDbToIo(p);
	}

	@Override
	public List<UserIn> getByCondition(UserIn utenteIo) {
		Specification<UserDB> spec = UserSpecification.build(utenteIo);

		List<UserDB> utenti=repository.findAll(spec);
		return fillList(utenti);
	}
 
	@Override
	public UserOut getUserById(long userId) {
		Optional<UserDB> p=repository.findById(userId);
		if (!p.isPresent()) return null;
		else return fromDbToIo(p.get());
	}

	

	private List<UserIn> fillList(List<UserDB> utenti) {
		return utenti.stream().map(p -> fromDbToIo(p))
		.collect(Collectors.toList());
	}

	

	@Override
	public UserOut updateUser(UserIn user, long userId) {
		if (repository.findById(userId).isPresent()) {
			UserDB p=repository.save(fromIOToDb(user, userId));
			return fromDbToIo(p);
		}
		else return null;
	}

	private UserOut fromDbToIo(UserDB utente) {
		UserOut p=new UserOut();
		p.setUserId(utente.getUserId());
		p.setAddress(utente.getAddress());
	
		p.setName(utente.getName());
		p.setSurname(utente.getSurname());
		p.setMail(utente.getMail());
		return p;
	}
	
	private UserDB fromIOToDb(UserIn utente, long userId) {
		UserDB p=new UserDB();
		p.setUserId(userId);
		p.setAddress(utente.getAddress());
		p.setMail(utente.getMail());
		p.setName(utente.getName());
		p.setSurname(utente.getSurname());
		return p;
	}
	private UserDB newUserToDb(UserIn utente) {
		UserDB p=new UserDB();
		p.setAddress(utente.getAddress());
		p.setMail(utente.getMail());
		p.setName(utente.getName());
		p.setSurname(utente.getSurname());
		return p;
	}
	
	
}
