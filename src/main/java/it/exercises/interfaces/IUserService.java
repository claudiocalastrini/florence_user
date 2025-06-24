package it.exercises.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import it.exercises.model.io.UserIn;
import it.exercises.model.io.UserOut;

@Service
public interface IUserService {
	
		public UserOut addUser(UserIn user);
	
		public List<UserIn> getByCondition(UserIn user);
	
		public UserOut getUserById(long userId);
	
		public UserOut updateUser(UserIn user, long userId);
	
}
