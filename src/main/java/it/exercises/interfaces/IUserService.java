package it.exercises.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import it.exercises.model.io.User;

@Service
public interface IUserService {
	
		public User addUser(User user);
	
		public List<User> getByCondition(User user);
	
		public User getUserById(int userId);
	
		public User updateUser(User user);
	
}
