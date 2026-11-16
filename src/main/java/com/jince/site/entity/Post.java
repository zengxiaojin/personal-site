package com.jince.site.entity;

import java.time.OffsetDateTime;

import lombok.Data;

/**
 * 文章（posts 表）。
 */
@Data
public class Post {

	private Long id;

	/** 网址里使用的名字，例如 /posts/hello */
	private String slug;

	private String title;

	private String summary;

	/** 正文（Markdown） */
	private String bodyMd;

	private PostStatus status;

	/** 第一次发布的时间。草稿时为空 */
	private OffsetDateTime publishedAt;

	private OffsetDateTime createdAt;

	private OffsetDateTime updatedAt;

}
