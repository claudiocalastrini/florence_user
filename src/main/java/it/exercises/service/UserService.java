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
import it.exercises.model.io.User;
import it.exercises.repository.UserRepository;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public User addUser(User user) {
		UserDB p=repository.save(fromIOToDb(user));
		return fromDbToIo(p);
	}

	@Override
	public List<User> getByCondition(User utenteIo) {
		Specification<UserDB> spec = UserSpecification.build(utenteIo);

		List<UserDB> utenti=repository.findAll(spec);
		return fillList(utenti);
	}
 
	@Override
	public User getUserById(int userId) {
		Optional<UserDB> p=repository.findById(userId);
		if (!p.isPresent()) return null;
		else return fromDbToIo(p.get());
	}

	

	private List<User> fillList(List<UserDB> utenti) {
		return utenti.stream().map(p -> fromDbToIo(p))
		.collect(Collectors.toList());
	}

	

	@Override
	public User updateUser(User user) {
		if (repository.findById(user.getUserId()).isPresent()) return addUser(user);
		else return null;
	}

	private User fromDbToIo(UserDB utente) {
		User p=new User();
		p.setUserId(utente.getUserId());
		p.setAddress(utente.getAddress());
	
		p.setName(utente.getName());
		p.setSurname(utente.getSurname());
		p.setMail(utente.getMail());
		return p;
	}
	
	private UserDB fromIOToDb(User utente) {
		UserDB p=new UserDB();
		p.setUserId(utente.getUserId());
		p.setAddress(utente.getAddress());
		p.setMail(utente.getMail());
		p.setName(utente.getName());
		p.setSurname(utente.getSurname());
		return p;
	}
	
	
	
}
