package com.jknyou.redis.domain;

import com.jknyou.redis.support.utils.WebUtils;

public class WebPost {

	private String content;
	private String name;
	private String replyTo;
	private String replyPid;
	private String pid;
	private String time;
	private String timeArg;

	public WebPost() {
	}

	public WebPost(Post post) {

		String tempTime = WebUtils.timeInWords(Long.valueOf(post.getTime()));
		int lastIndexOf = tempTime.lastIndexOf("#");
		if (lastIndexOf > 0) {
			this.time = tempTime.substring(0, lastIndexOf);
			this.timeArg = tempTime.substring(lastIndexOf + 1);
		}
		else {
			this.time = tempTime;
			this.timeArg = "";
		}
		this.replyPid = post.getReplyPid();
		this.content = post.getContent();
	}

	public String getReplyPid() {
		return replyPid;
	}
	public void setReplyPid(String replyPid) {
		this.replyPid = replyPid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReplyTo() {
		return replyTo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTimeArg() {
		return timeArg;
	}
	public void setReplyTo(String replyName) {
		this.replyTo = replyName;
	}

	public Post asPost() {
		Post post = new Post();
		post.setReplyPid(replyPid);
		post.setContent(content);
		return post;
	}

	@Override
	public String toString() {
		return "WebPost [content=" + content + ", name=" + name + ", pid=" + pid + ", replyTo=" + replyTo
				+ ", replyPid=" + replyPid + ", time=" + time + "]";
	}
}