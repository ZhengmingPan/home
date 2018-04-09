package com.home.common.service;

import java.io.Serializable;
import java.util.List;

public interface JpaService<T, ID extends Serializable> {

	public T get(ID id);

	public List<T> list();

	public void delete(ID id);

	public T save(T entity);

	public T saveAndFlush(T entity);

	void saveAll(List<T> entities);

}
