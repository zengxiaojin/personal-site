package com.jince.site.form;

import com.jince.site.entity.LikeItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理后台里「喜欢的东西」表单的输入内容。
 */
@Data
public class LikeItemForm {

	@NotBlank(message = "请填写类别")
	@Size(max = 50, message = "类别不能超过 50 个字")
	private String category;

	@NotBlank(message = "请填写名字")
	@Size(max = 200, message = "名字不能超过 200 个字")
	private String title;

	@Size(max = 500, message = "感想不能超过 500 个字")
	private String note;

	@Size(max = 500, message = "网址不能超过 500 个字符")
	@Pattern(regexp = "https?://.+", message = "请填写以 http:// 或 https:// 开头的网址")
	private String url;

	@NotNull(message = "请填写排序数字")
	private Integer sortOrder = 0;

	public static LikeItemForm from(LikeItem item) {
		LikeItemForm form = new LikeItemForm();
		form.setCategory(item.getCategory());
		form.setTitle(item.getTitle());
		form.setNote(item.getNote());
		form.setUrl(item.getUrl());
		form.setSortOrder(item.getSortOrder());
		return form;
	}

	public void applyTo(LikeItem item) {
		item.setCategory(category);
		item.setTitle(title);
		item.setNote(note);
		item.setUrl(url);
		item.setSortOrder(sortOrder);
	}

}
