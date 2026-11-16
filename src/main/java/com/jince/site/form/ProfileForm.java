package com.jince.site.form;

import com.jince.site.entity.Profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理后台里个人信息表单的输入内容。
 */
@Data
public class ProfileForm {

	@NotBlank(message = "请填写名字")
	@Size(max = 50, message = "名字不能超过 50 个字")
	private String name;

	@NotBlank(message = "请填写自我介绍")
	private String intro;

	private String aboutMd;

	@Email(message = "邮箱格式不正确")
	@Size(max = 255, message = "邮箱不能超过 255 个字符")
	private String email;

	@Size(max = 255, message = "网址不能超过 255 个字符")
	@Pattern(regexp = "https?://.+", message = "请填写以 http:// 或 https:// 开头的网址")
	private String githubUrl;

	public static ProfileForm from(Profile profile) {
		ProfileForm form = new ProfileForm();
		form.setName(profile.getName());
		form.setIntro(profile.getIntro());
		form.setAboutMd(profile.getAboutMd());
		form.setEmail(profile.getEmail());
		form.setGithubUrl(profile.getGithubUrl());
		return form;
	}

	public void applyTo(Profile profile) {
		profile.setName(name);
		profile.setIntro(intro);
		profile.setAboutMd(aboutMd == null ? "" : aboutMd);
		profile.setEmail(email);
		profile.setGithubUrl(githubUrl);
	}

}
