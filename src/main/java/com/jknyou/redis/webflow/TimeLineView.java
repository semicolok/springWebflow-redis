package com.jknyou.redis.webflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.event.DragDropEvent;
import org.springframework.stereotype.Component;

import com.jknyou.redis.domain.WebPost;
import com.jknyou.redis.service.PostService;
import com.jknyou.redis.service.UserService;

@Component
public class TimeLineView implements Serializable {
	private static final long serialVersionUID = 2963924562881086500L;
	
	@Inject private PostService postService;
	@Inject private UserService userService;
	
    private List<WebPost> posts;
    private List<WebPost> droppedPosts;
    private List<String> users;
    
    private boolean searchPostChck = false;
    private boolean searchUserChck = false;
    
    public TimeLineView() {
		this.posts = new ArrayList<WebPost>();
		this.droppedPosts = new ArrayList<WebPost>();
		this.users = new ArrayList<String>();
	}  
    
    public List<WebPost> getPosts() {
    	if(!searchPostChck) {
    		posts = postService.getTimeLine();
    		searchPostChck = true;
    	}
        return posts;  
    }
    
    public List<WebPost> getDroppedPosts() {
		return droppedPosts;
	}
    
	public List<String> getUsers() {
		if(!searchUserChck) {
			users = userService.getNewUsers();
			searchUserChck = true;
		}
		return users;
	}

	public void onPostDrop(DragDropEvent ddEvent) {  
		WebPost post = ((WebPost) ddEvent.getData());  
    	droppedPosts.add(post);  
    	posts.remove(post);  
    }
	
	public void removePerson(){
		this.droppedPosts = new ArrayList<WebPost>();
	}
}