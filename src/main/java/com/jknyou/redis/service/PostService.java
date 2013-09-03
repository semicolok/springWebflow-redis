package com.jknyou.redis.service;

import java.util.List;

import com.jknyou.redis.domain.WebPost;


public interface PostService {
	List<WebPost> getTimeLine();
	List<WebPost> findPostsByName(String name);
	void post(String name, String message);
}
