package com.jince.site.entity;

import java.time.OffsetDateTime;

import lombok.Data;

/**
 * 作品（works 表）。
 */
@Data
public class Work {

	private Long id;

	/** 网址里使用的名字，例如 /works/this-site */
	private String slug;

	private String title;

	/** 一句话说明 */
	private String summary;

	/** 详细介绍（Markdown） */
	private String bodyMd;

	/** 作品的网址 */
	private String siteUrl;

	/** 源代码的网址 */
	private String repoUrl;

	/** 制作的年份 */
	private Integer year;

	/** 排序用的数字，小的排在前面 */
	private Integer sortOrder;

	private OffsetDateTime createdAt;

	private OffsetDateTime updatedAt;

}
