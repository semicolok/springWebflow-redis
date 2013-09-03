package com.jknyou.redis.repository.impl;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.jknyou.redis.domain.User;
import com.jknyou.redis.repository.UserRepository;
import com.jknyou.redis.support.utils.KeyUtils;

@Repository
public class UserRepositoryImpl implements UserRepository {
	
	@Inject private StringRedisTemplate redisTemplate;
	// global users
	private RedisList<String> users;
	
	@Override
	public boolean isUserValid(String name) {
		return redisTemplate.hasKey(KeyUtils.user(name));
	}

	private ValueOperations<String, String> getOpsForValue() {
		return redisTemplate.opsForValue();
	}
	
	@Override
	public void save(User user) {
		String uid = String.valueOf(new RedisAtomicLong(KeyUtils.globalUid(), redisTemplate.getConnectionFactory()).incrementAndGet());
		BoundHashOperations<String, String, String> userOps = redisTemplate.boundHashOps(KeyUtils.uid(uid));
		userOps.put("name", user.getName());
		userOps.put("pass", user.getPass());
		
		getOpsForValue().set(KeyUtils.user(user.getName()), uid);
		users.addFirst(user.getName());
		addAuth(user.getName());
	}

	@Override
	public String addAuth(String name) {
		String uid = this.findUidByName(name);
		ValueOperations<String, String> valueOps = getOpsForValue();
		String auth = UUID.randomUUID().toString();
		valueOps.set(KeyUtils.auth(uid), auth);
		valueOps.set(KeyUtils.authKey(auth), uid);
		return auth;
	}

	@Override
	public String findUidByName(String name) {
		return getOpsForValue().get(KeyUtils.user(name));
	}

	@Override
	public List<String> getNewUsers() {
		users = new DefaultRedisList<String>(KeyUtils.users(), redisTemplate);
		return users.range(0, -1);
	}

	@Override
	public String findName(String uid) {
		if (!StringUtils.hasText(uid)) {
			return "";
		}
		BoundHashOperations<String, String, String> userOps = redisTemplate.boundHashOps(KeyUtils.uid(uid));
		return userOps.get("name");
	}

	@Override
	public String findUid(String name) {
		return getOpsForValue().get(KeyUtils.user(name));
	}

	@Override
	public List<String> getFollowers(String uid) {
		return covertUidsToNames(KeyUtils.followers(uid));
	}

	@Override
	public List<String> getFollowing(String uid) {
		return covertUidsToNames(KeyUtils.following(uid));
	}
	
	private List<String> covertUidsToNames(String key) {
		return redisTemplate.sort(SortQueryBuilder.sort(key).noSort().get("uid:*->name").build());
	}
}