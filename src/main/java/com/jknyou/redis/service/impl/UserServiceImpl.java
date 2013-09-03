package com.jknyou.redis.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.jknyou.redis.domain.User;
import com.jknyou.redis.repository.UserRepository;
import com.jknyou.redis.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Inject private UserRepository userRepository;

	@Override
	@CacheEvict(value = "userCache", allEntries=true)
	public void save(User user) {
		if (user != null && !userRepository.isUserValid(user.getName())) {
			userRepository.save(user);
		}
	}

	@Override
	public List<String> getNewUsers() {
		return userRepository.getNewUsers();
	}

	@Override
	public boolean isUserValid(String uName) {
		return userRepository.isUserValid(uName);
	}

	@Override
	public String findName(String uid) {
		return userRepository.findName(uid);
	}

	@Override
	public String findUidByName(String name) {
		return userRepository.findUid(name);
	}

	@Override
	public List<String> getFollowing(String name) {
		return userRepository.getFollowing(this.findUidByName(name));
	}

	@Override
	public List<String> getFollowers(String name) {
		return userRepository.getFollowers(this.findUidByName(name));
	}

//	@Override
//	@Cacheable(value="userCache")
//	public List<User> getUsers() {
//		return userRepository.getUsers();
//	}
//
//	@Override
//	@CacheEvict(value = "userCache", allEntries=true)
//	public void put(User user) {
//		userRepository.put(user);
//	}

}
