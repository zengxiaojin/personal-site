package com.jince.site.entity;

import java.time.OffsetDateTime;

import lombok.Data;

/**
 * 喜欢的东西（like_items 表）。
 */
@Data
public class LikeItem {

	private Long id;

	/** 类别，例如「书」「摄影」 */
	private String category;

	private String title;

	/** 一句话感想 */
	private String note;

	private String url;

	/** 排序用的数字，小的排在前面 */
	private Integer sortOrder;

	private OffsetDateTime createdAt;

	private OffsetDateTime updatedAt;

}
