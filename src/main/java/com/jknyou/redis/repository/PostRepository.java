package com.jknyou.redis.repository;

import java.util.List;

import com.jknyou.redis.domain.WebPost;

public interface PostRepository{
	List<WebPost> timeLine();
	List<WebPost> getPosts(String uid);
	void post(String name, String message);
}