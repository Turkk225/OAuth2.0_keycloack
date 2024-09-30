package com.kse.core.authkeycloak.logics;

import java.util.List;

public interface Crud<E> {
	
	E save(E entity);
	E update(E entity);
	E getById(int id);
	List<E> getAll();
	Boolean delete(Integer id);
}
