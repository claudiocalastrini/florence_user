package it.exercises.validator;

import org.springframework.stereotype.Component;

import it.exercises.model.io.Product;
@Component
public class ProductValidator {
	public boolean validateProduct(Product product) {
		if (product.getPrice()<0) return false;  
		if (product.getQuantity()<0) return false;
		//aggiungere controllo sulla categoria?
		else return true;
	}
}
