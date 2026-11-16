package com.jince.site.form;

import com.jince.site.entity.NowEntry;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 管理后台里近况表单的输入内容。
 */
@Data
public class NowEntryForm {

	@NotBlank(message = "请填写内容")
	private String bodyMd;

	public static NowEntryForm from(NowEntry entry) {
		NowEntryForm form = new NowEntryForm();
		form.setBodyMd(entry.getBodyMd());
		return form;
	}

	public void applyTo(NowEntry entry) {
		entry.setBodyMd(bodyMd);
	}

}
