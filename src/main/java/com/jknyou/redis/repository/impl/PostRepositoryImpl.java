package com.jknyou.redis.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.springframework.data.redis.core.BulkMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.query.SortQueryBuilder;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.DefaultRedisSet;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.stereotype.Repository;

import com.jknyou.redis.domain.Post;
import com.jknyou.redis.domain.WebPost;
import com.jknyou.redis.repository.PostRepository;
import com.jknyou.redis.service.UserService;
import com.jknyou.redis.support.utils.KeyUtils;

@Repository
public class PostRepositoryImpl implements PostRepository {
	private static final Pattern MENTION_REGEX = Pattern.compile("@[\\w]+");
	@Inject private StringRedisTemplate redisTemplate;
	@Inject private UserService userService;
	
	private final HashMapper<Post, String, String> postMapper = new DecoratingStringHashMapper<Post>(
			new JacksonHashMapper<Post>(Post.class));

	@Override
	public List<WebPost> timeLine() {
		return convertPidsToPosts(KeyUtils.timeline());
	}
	
	private List<WebPost> convertPidsToPosts(String key) {
		String pid = "pid:*->";
		final String pidKey = "#";
		final String uid = "uid";
		final String content = "content";
		final String replyPid = "replyPid";
		final String replyUid = "replyUid";
		final String time = "time";

		SortQuery<String> query = SortQueryBuilder.sort(key).noSort().get(pidKey).get(pid + uid).get(pid + content).get(
				pid + replyPid).get(pid + replyUid).get(pid + time).limit(0, -1).build();
		BulkMapper<WebPost, String> hm = new BulkMapper<WebPost, String>() {
			@Override
			public WebPost mapBulk(List<String> bulk) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				Iterator<String> iterator = bulk.iterator();

				String pid = iterator.next();
				map.put(uid, iterator.next());
				map.put(content, iterator.next());
				map.put(replyPid, iterator.next());
				map.put(replyUid, iterator.next());
				map.put(time, iterator.next());

				return convertPost(pid, map);
			}
		};
		List<WebPost> sort = redisTemplate.sort(query, hm);

		return sort;
	}
	
	private WebPost convertPost(String pid, Map hash) {
		Post post = postMapper.fromHash(hash);
		WebPost wPost = new WebPost(post);
		wPost.setPid(pid);
		wPost.setName(userService.findName(post.getUid()));
		wPost.setReplyTo(userService.findName(post.getReplyUid()));
		wPost.setContent(replaceReplies(post.getContent()));
		return wPost;
	}
	
	
	private String replaceReplies(String content) {
		Matcher regexMatcher = MENTION_REGEX.matcher(content);
		while (regexMatcher.find()) {
			String match = regexMatcher.group();
			int start = regexMatcher.start();
			int stop = regexMatcher.end();

			String uName = match.substring(1);
			if (userService.isUserValid(uName)) {
				content = content.substring(0, start) + "<a href=\"!" + uName + "\">" + match + "</a>"
						+ content.substring(stop);
			}
		}
		return content;
	}
	public boolean isUserValid(String name) {
		return redisTemplate.hasKey(KeyUtils.user(name));
	}

	@Override
	public List<WebPost> getPosts(String uid) {
		return convertPidsToPosts(KeyUtils.posts(uid));
	}

	@Override
	public void post(String username, String message) {
		Post p = new Post();
		p.setContent(message);

		String uid = userService.findUidByName(username);
		p.setUid(uid);

		String pid = String.valueOf(new RedisAtomicLong(KeyUtils.globalPid(), redisTemplate.getConnectionFactory()).incrementAndGet());

		// add post
		post(pid).putAll(postMapper.toHash(p));

		// add links
		posts(uid).addFirst(pid);
		timeline(uid).addFirst(pid);

		// update followers
		for (String follower : followers(uid)) {
			timeline(follower).addFirst(pid);
		}

		new DefaultRedisList<String>(KeyUtils.timeline(), redisTemplate).addFirst(pid);
		handleMentions(p, pid, "");
	}
	private void handleMentions(Post post, String pid, String name) {
		// find mentions
		Collection<String> mentions = findMentions(post.getContent());

		for (String mention : mentions) {
			String uid = userService.findUidByName(mention);
			if (uid != null) {
				mentions(uid).addFirst(pid);
			}
		}
	}
	private RedisMap<String, String> post(String pid) {
		return new DefaultRedisMap<String, String>(KeyUtils.post(pid), redisTemplate);
	}
	private RedisList<String> posts(String uid) {
		return new DefaultRedisList<String>(KeyUtils.posts(uid), redisTemplate);
	}
	private RedisSet<String> followers(String uid) {
		return new DefaultRedisSet<String>(KeyUtils.followers(uid), redisTemplate);
	}
	private RedisList<String> timeline(String uid) {
		return new DefaultRedisList<String>(KeyUtils.timeline(uid), redisTemplate);
	}
	private RedisList<String> mentions(String uid) {
		return new DefaultRedisList<String>(KeyUtils.mentions(uid), redisTemplate);
	}
	public static Collection<String> findMentions(String content) {
		Matcher regexMatcher = MENTION_REGEX.matcher(content);
		List<String> mentions = new ArrayList<String>(4);

		while (regexMatcher.find()) {
			mentions.add(regexMatcher.group().substring(1));
		}

		return mentions;
	}
}
