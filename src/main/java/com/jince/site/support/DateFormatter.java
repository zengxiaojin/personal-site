package com.jince.site.support;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.jince.site.config.AppProperties;

/**
 * 按网站设定的时区显示日期。
 * 在页面模板里用 ${@dates.date(...)} 的写法调用。
 */
@Component("dates")
public class DateFormatter {

	private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy.MM.dd");

	private static final DateTimeFormatter MONTH_DAY = DateTimeFormatter.ofPattern("MM.dd");

	private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy年M月");

	private final ZoneId zone;

	public DateFormatter(AppProperties properties) {
		this.zone = properties.site().timeZone();
	}

	/** 例：2026.11.16 */
	public String date(OffsetDateTime time) {
		return time == null ? "" : DATE.format(time.atZoneSameInstant(zone));
	}

	/** 例：11.16 */
	public String monthDay(OffsetDateTime time) {
		return time == null ? "" : MONTH_DAY.format(time.atZoneSameInstant(zone));
	}

	/** 例：2026年11月 */
	public String yearMonth(OffsetDateTime time) {
		return time == null ? "" : YEAR_MONTH.format(time.atZoneSameInstant(zone));
	}

	/** 例：2026 */
	public int year(OffsetDateTime time) {
		return time.atZoneSameInstant(zone).getYear();
	}

	/** 给 HTML 的 time 标签和 RSS 用的标准格式 */
	public String iso(OffsetDateTime time) {
		return time == null ? "" : DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(time.atZoneSameInstant(zone));
	}

	/** 现在的年份，用于页脚的版权年份 */
	public int currentYear() {
		return OffsetDateTime.now(zone).getYear();
	}

}
