package com.jince.site.entity;

import java.time.OffsetDateTime;

import lombok.Data;

/**
 * 历程里的一件事（journey_entries 表）。
 */
@Data
public class JourneyEntry {

	private Long id;

	private Integer year;

	/** 月份，可以不填 */
	private Integer month;

	private String title;

	private String description;

	private OffsetDateTime createdAt;

	private OffsetDateTime updatedAt;

}
