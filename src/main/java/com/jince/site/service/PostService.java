package com.jince.site.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jince.site.entity.Post;
import com.jince.site.entity.PostStatus;
import com.jince.site.exception.NotFoundException;
import com.jince.site.form.PostForm;
import com.jince.site.mapper.PostMapper;
import com.jince.site.support.DateFormatter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 文章的业务逻辑。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

	private final PostMapper postMapper;

	private final DateFormatter dates;

	public List<Post> findPublished() {
		return postMapper.findPublished();
	}

	public List<Post> findLatestPublished(int limit) {
		return postMapper.findLatestPublished(limit);
	}

	/**
	 * 已发布的文章按发布年份分组，新的年份在前。
	 */
	public Map<Integer, List<Post>> findPublishedGroupedByYear() {
		Map<Integer, List<Post>> groups = new LinkedHashMap<>();
		for (Post post : postMapper.findPublished()) {
			groups.computeIfAbsent(dates.year(post.getPublishedAt()), year -> new ArrayList<>()).add(post);
		}
		return groups;
	}

	public Post getPublishedBySlug(String slug) {
		Post post = postMapper.findPublishedBySlug(slug);
		if (post == null) {
			throw new NotFoundException("没有这篇文章：" + slug);
		}
		return post;
	}

	public List<Post> findAll() {
		return postMapper.findAll();
	}

	public Post getById(Long id) {
		Post post = postMapper.findById(id);
		if (post == null) {
			throw new NotFoundException("没有这篇文章：id=" + id);
		}
		return post;
	}

	/**
	 * 这个网址名是否已经被别的文章使用。
	 *
	 * @param excludeId 正在编辑的文章的 id（新建时为 null）
	 */
	public boolean isSlugTaken(String slug, Long excludeId) {
		return postMapper.countBySlug(slug, excludeId) > 0;
	}

	@Transactional
	public Post create(PostForm form) {
		Post post = new Post();
		form.applyTo(post);
		setPublishedAtIfNeeded(post);
		postMapper.insert(post);
		log.info("文章已新建：id={}, slug={}", post.getId(), post.getSlug());
		return post;
	}

	@Transactional
	public void update(Long id, PostForm form) {
		Post post = getById(id);
		form.applyTo(post);
		setPublishedAtIfNeeded(post);
		postMapper.update(post);
		log.info("文章已更新：id={}, slug={}", id, post.getSlug());
	}

	@Transactional
	public void delete(Long id) {
		postMapper.delete(id);
		log.info("文章已删除：id={}", id);
	}

	/**
	 * 第一次发布时记下发布时间。之后再编辑，发布时间保持不变。
	 */
	private void setPublishedAtIfNeeded(Post post) {
		if (post.getStatus() == PostStatus.PUBLISHED && post.getPublishedAt() == null) {
			post.setPublishedAt(OffsetDateTime.now());
		}
	}

}
