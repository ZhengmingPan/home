package com.home.core.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
 
public interface UserMybatisDao {

	@Select("SELECT * FROM core_user") 
	@Results({
		@Result(column="login_name", jdbcType = JdbcType.VARCHAR, javaType = String.class)
	})
	List<String> list();
	
}
