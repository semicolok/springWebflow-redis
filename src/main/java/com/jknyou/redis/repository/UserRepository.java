package com.jknyou.redis.repository;

import java.util.List;

import com.jknyou.redis.domain.User;


public interface UserRepository{

	boolean isUserValid(String name);
	void save(User user);
	String addAuth(String name);
	String findUidByName(String name);
	List<String> getNewUsers();
	String findName(String uid);
	String findUid(String name);
	List<String> getFollowers(String uid);
	List<String> getFollowing(String uid);
}