package com.jknyou.redis.webflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.jknyou.redis.domain.WebPost;
import com.jknyou.redis.service.PostService;
import com.jknyou.redis.service.UserService;

@Component
public class WitterView implements Serializable{
	private static final long serialVersionUID = 8620520183408203684L;
	
	@Inject private PostService postService;
	@Inject private UserService userService;
	
	private List<WebPost> posts;
	private List<String> follows;
	private List<String> followings;
	private WebPost selectedPost;
	private boolean searchCheck = false;
	private boolean searchFollowCheck = false;
	private boolean searchFollowingCheck = false;
	private String message;
	
	public WitterView() {
		this.posts = new ArrayList<WebPost>();
		this.follows = new ArrayList<String>();
		this.followings = new ArrayList<String>();
	}
	
	public List<String> getFollows() {
		if (!searchFollowCheck) {
			follows = userService.getFollowers("jknyou");
			searchFollowCheck = true;
		}
		return follows;
	}
	public List<String> getFollowings() {
		if (!searchFollowingCheck) {
			followings = userService.getFollowing("jknyou");
			searchFollowingCheck = true;
		}
		return followings;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public List<WebPost> getPosts() {
		if (!searchCheck) {
			posts = postService.findPostsByName("jknyou");
			searchCheck = true;
		}
        return posts;  
    }

	public WebPost getSelectedPost() {
		return selectedPost;
	}

	public void setSelectedPost(WebPost selectedPost) {
		this.selectedPost = selectedPost;
	}
	
	public void postMessage(){
		postService.post("jknyou", this.message);
		this.message = "";
		posts = postService.findPostsByName("jknyou");
	}
	
}
