package com.jince.site.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.jince.site.entity.Profile;
import com.jince.site.service.ProfileService;

import lombok.RequiredArgsConstructor;

/**
 * 所有页面共用的处理。
 */
@ControllerAdvice
@RequiredArgsConstructor
public class SiteModelAdvice {

	private static final String AVATAR_URL = "/images/avatar.jpg";

	private final ProfileService profileService;

	/** 头像照片放在 static/images/avatar.jpg。没有照片时页面上显示名字的第一个字 */
	private final boolean hasAvatar = new ClassPathResource("static" + AVATAR_URL).exists();

	/**
	 * 每个页面的页头和页脚都要用到名字等信息，所以统一放进来。
	 */
	@ModelAttribute("profile")
	public Profile profile() {
		return profileService.get();
	}

	@ModelAttribute("avatarUrl")
	public String avatarUrl() {
		return hasAvatar ? AVATAR_URL : null;
	}

	/**
	 * 表单里输入的文字去掉首尾空格；什么都没填时按「没有值」处理。
	 */
	@InitBinder
	public void trimStrings(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

}
