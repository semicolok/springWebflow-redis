package com.jknyou.redis.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.jknyou.redis.domain.WebPost;
import com.jknyou.redis.repository.PostRepository;
import com.jknyou.redis.service.PostService;
import com.jknyou.redis.service.UserService;

@Service
public class PostServiceImpl implements PostService {
	@Inject private PostRepository postRepository;
	@Inject private UserService userService;

	@Override
	public List<WebPost> getTimeLine() {
		return postRepository.timeLine();
	}

	@Override
	public List<WebPost> findPostsByName(String name) {
		String uid = userService.findUidByName(name);
		return postRepository.getPosts(uid);
	}

	@Override
	public void post(String name, String message) {
		postRepository.post(name, message);
	}

}
