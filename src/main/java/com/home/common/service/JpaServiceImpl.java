package com.home.common.service;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.Transactional;

import com.home.common.dao.JpaDao;

@EnableAspectJAutoProxy(proxyTargetClass = true)
public abstract class JpaServiceImpl<T, ID extends Serializable> implements JpaService<T, ID> {

	@Autowired
	private JpaDao<T, ID> dao;

	@Override
	public T get(ID id) {
		return dao.findOne(id);
	}

	@Override
	public List<T> list() {
		return dao.findAll();
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(ID id) {
		dao.delete(id);
	}

	@Override
	@Transactional(readOnly = false)
	public T save(T entity) {
		T t = dao.save(entity);
		return t;
	}

	@Override
	@Transactional(readOnly = false)
	public T saveAndFlush(T entity) {
		T t = dao.saveAndFlush(entity);
		return t;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void saveAll(List<T> entities) { 
		dao.save(entities);
	}

}
