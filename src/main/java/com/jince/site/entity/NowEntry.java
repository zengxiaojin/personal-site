package com.jince.site.entity;

import java.time.OffsetDateTime;

import lombok.Data;

/**
 * 近况（now_entries 表）。最新的一条显示在首页，其余作为存档。
 */
@Data
public class NowEntry {

	private Long id;

	/** 内容（Markdown） */
	private String bodyMd;

	private OffsetDateTime createdAt;

	private OffsetDateTime updatedAt;

}
