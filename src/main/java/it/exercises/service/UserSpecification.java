package it.exercises.service;

import org.springframework.data.jpa.domain.Specification;

import it.exercises.model.db.UserDB;
import it.exercises.model.io.UserIn;

public class UserSpecification {

    public static Specification<UserDB> build(UserIn request) {
        return (root, query, cb) -> {
            Specification<UserDB> spec = Specification.where(null);

            if (request.getMail() != null) {
                spec = spec.and((r, q, c) -> c.equal(r.get("mail"), request.getMail()));
            }
            ricercaLike(request.getName(), "name", spec);
            ricercaLike(request.getSurname(), "surname", spec);
            ricercaLike(request.getAddress(), "address", spec);

            return spec.toPredicate(root, query, cb);
        };
    }

	private static Specification<UserDB> ricercaLike(String value, String fieldName, Specification<UserDB> spec) {
		if (value != null && value.trim().isEmpty()) {
		    String namePattern = "%" + value.trim().toLowerCase() + "%";
		    spec = spec.and((r, q, c) -> c.like(c.lower(r.get(fieldName)), namePattern));
		}
		return spec;
	}
    
    
}

