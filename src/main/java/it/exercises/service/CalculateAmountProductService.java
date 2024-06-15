package it.exercises.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import it.exercises.model.io.Category;
import it.exercises.model.io.Product;

@Service
public class CalculateAmountProductService {
	/**
	*
	@param
	products the list of products *
	@param
	category the category on which to filter
	*
	*
	@return
	The sum of total amount for all products belonging to the input category
	*/
	public double amount (List<Product> products, Category category)
	{
		return products.stream().filter(product -> product.getCategory().compareTo(category)==0).map(p->  new BigDecimal(p.getPrice()*p.getQuantity())).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
	}
}