package it.exercises.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.exercises.model.db.ProductDB;
import it.exercises.model.io.Category;

@Repository
public interface ProductRepository extends JpaRepository<ProductDB, Integer> {
  public List<ProductDB>findByCategory(Category category);
  public List<ProductDB>findAllByOrderByCategoryDesc();
}
