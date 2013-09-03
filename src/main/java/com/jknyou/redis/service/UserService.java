package com.jknyou.redis.service;

import java.util.List;

import com.jknyou.redis.domain.User;

public interface UserService {
	void save(User user);
	List<String> getNewUsers();
	boolean isUserValid(String uName);
	String findName(String uid);
	String findUidByName(String name);
	List<String> getFollowing(String name);
	List<String> getFollowers(String name);
}
