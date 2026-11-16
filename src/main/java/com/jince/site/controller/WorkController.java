package com.jince.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jince.site.entity.Work;
import com.jince.site.service.MarkdownService;
import com.jince.site.service.WorkService;

import lombok.RequiredArgsConstructor;

/**
 * 作品的列表页和详情页。
 */
@Controller
@RequiredArgsConstructor
public class WorkController {

	private final WorkService workService;

	private final MarkdownService markdownService;

	@GetMapping("/works")
	public String list(Model model) {
		model.addAttribute("works", workService.findAll());
		return "works/list";
	}

	@GetMapping("/works/{slug}")
	public String detail(@PathVariable String slug, Model model) {
		Work work = workService.getBySlug(slug);
		model.addAttribute("work", work);
		model.addAttribute("bodyHtml", markdownService.toHtml(work.getBodyMd()));
		return "works/detail";
	}

}
