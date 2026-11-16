package com.jince.site.form;

import com.jince.site.entity.JourneyEntry;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理后台里历程表单的输入内容。
 */
@Data
public class JourneyEntryForm {

	@NotNull(message = "请填写年份")
	@Min(value = 1900, message = "年份不正确")
	@Max(value = 2200, message = "年份不正确")
	private Integer year;

	@Min(value = 1, message = "月份要在 1 到 12 之间")
	@Max(value = 12, message = "月份要在 1 到 12 之间")
	private Integer month;

	@NotBlank(message = "请填写发生了什么")
	@Size(max = 200, message = "不能超过 200 个字")
	private String title;

	private String description;

	public static JourneyEntryForm from(JourneyEntry entry) {
		JourneyEntryForm form = new JourneyEntryForm();
		form.setYear(entry.getYear());
		form.setMonth(entry.getMonth());
		form.setTitle(entry.getTitle());
		form.setDescription(entry.getDescription());
		return form;
	}

	public void applyTo(JourneyEntry entry) {
		entry.setYear(year);
		entry.setMonth(month);
		entry.setTitle(title);
		entry.setDescription(description);
	}

}
