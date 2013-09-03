package com.jknyou.redis.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jknyou.redis.domain.User;
import com.jknyou.redis.service.UserService;

@Controller
public class UserController {
	private static final String JSON_VIEW = "jsonView";
	
	@Inject private UserService userService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(ModelMap map) {
		return "redis";
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String getAllUser(ModelMap map) {
//		map.put("users", userService.getUsers());
		return JSON_VIEW;
	}

	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public String putUser(User user, ModelMap map) {
		userService.save(user);
		return JSON_VIEW;
	}
	
//	@RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
//	public String deleteUser(@PathVariable String userId, ModelMap map) {
//		userService.delete(userId);
//		return JSON_VIEW;
//	}
}
