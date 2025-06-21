package it.exercises.repository;

import java.util.List;
import java.util.Locale.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import it.exercises.model.db.UserDB;

@Repository
public interface UserRepository extends JpaRepository<UserDB, Integer>,JpaSpecificationExecutor<UserDB> {
  
}
