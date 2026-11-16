package com.jince.site.form;

import com.jince.site.entity.Work;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理后台里作品表单的输入内容。
 */
@Data
public class WorkForm {

	@NotBlank(message = "请填写网址名")
	@Size(max = 100, message = "网址名不能超过 100 个字符")
	@Pattern(regexp = "[a-z0-9]+(-[a-z0-9]+)*", message = "只能用小写英文字母、数字和连字符，例如 my-first-app")
	private String slug;

	@NotBlank(message = "请填写名字")
	@Size(max = 200, message = "名字不能超过 200 个字")
	private String title;

	@NotBlank(message = "请填写一句话说明")
	@Size(max = 300, message = "说明不能超过 300 个字")
	private String summary;

	private String bodyMd;

	@Size(max = 500, message = "网址不能超过 500 个字符")
	@Pattern(regexp = "https?://.+", message = "请填写以 http:// 或 https:// 开头的网址")
	private String siteUrl;

	@Size(max = 500, message = "网址不能超过 500 个字符")
	@Pattern(regexp = "https?://.+", message = "请填写以 http:// 或 https:// 开头的网址")
	private String repoUrl;

	@NotNull(message = "请填写年份")
	@Min(value = 1900, message = "年份不正确")
	@Max(value = 2200, message = "年份不正确")
	private Integer year;

	@NotNull(message = "请填写排序数字")
	private Integer sortOrder = 0;

	public static WorkForm from(Work work) {
		WorkForm form = new WorkForm();
		form.setSlug(work.getSlug());
		form.setTitle(work.getTitle());
		form.setSummary(work.getSummary());
		form.setBodyMd(work.getBodyMd());
		form.setSiteUrl(work.getSiteUrl());
		form.setRepoUrl(work.getRepoUrl());
		form.setYear(work.getYear());
		form.setSortOrder(work.getSortOrder());
		return form;
	}

	public void applyTo(Work work) {
		work.setSlug(slug);
		work.setTitle(title);
		work.setSummary(summary);
		work.setBodyMd(bodyMd == null ? "" : bodyMd);
		work.setSiteUrl(siteUrl);
		work.setRepoUrl(repoUrl);
		work.setYear(year);
		work.setSortOrder(sortOrder);
	}

}
