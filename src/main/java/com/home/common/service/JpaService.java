package com.home.common.service;

import java.io.Serializable;
import java.util.List;

public interface JpaService<T, ID extends Serializable> {

	T get(ID id);

	List<T> list();

	void delete(ID id);

	T save(T entity);

	T saveAndFlush(T entity);

	void saveAll(List<T> entities);

}
