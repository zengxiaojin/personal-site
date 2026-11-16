package com.jince.site.entity;

import java.time.OffsetDateTime;

import lombok.Data;

/**
 * 个人信息（profile 表，只有一行）。
 */
@Data
public class Profile {

	private Integer id;

	/** 网站上显示的名字 */
	private String name;

	/** 首页的自我介绍 */
	private String intro;

	/** 「关于」页的正文（Markdown） */
	private String aboutMd;

	private String email;

	private String githubUrl;

	private OffsetDateTime updatedAt;

}
