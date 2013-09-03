package com.jknyou.redis.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable{
	private static final long serialVersionUID = 5042839870408631298L;
	
	private Long id;
	private String name;
	private String pass;
	private String authKey;
	
	public User() {
	}
	
	public User(String name, String pass) {
		this.name = name;
		this.pass = pass;
	}
}