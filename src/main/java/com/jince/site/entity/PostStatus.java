package com.jince.site.entity;

/**
 * 文章的状态。
 */
public enum PostStatus {

	/** 草稿：只有管理后台能看到 */
	DRAFT("草稿"),

	/** 已发布：所有人都能看到 */
	PUBLISHED("已发布");

	private final String label;

	PostStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
