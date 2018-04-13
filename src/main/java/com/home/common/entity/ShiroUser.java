package com.home.common.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
public class ShiroUser implements Serializable { 
	
	private Long id;
	private String loginName;
	private String name;

}
