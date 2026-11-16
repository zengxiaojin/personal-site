package com.jince.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jince.site.service.MarkdownService;
import com.jince.site.service.ProfileService;

import lombok.RequiredArgsConstructor;

/**
 * 「关于」页。
 */
@Controller
@RequiredArgsConstructor
public class AboutController {

	private final ProfileService profileService;

	private final MarkdownService markdownService;

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("aboutHtml", markdownService.toHtml(profileService.get().getAboutMd()));
		return "about";
	}

}
