package com.jince.site.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jince.site.dto.NowView;
import com.jince.site.service.MarkdownService;
import com.jince.site.service.NowService;

import lombok.RequiredArgsConstructor;

/**
 * 「近况」页：最新的一条和以前的存档。
 */
@Controller
@RequiredArgsConstructor
public class NowController {

	private final NowService nowService;

	private final MarkdownService markdownService;

	@GetMapping("/now")
	public String now(Model model) {
		List<NowView> entries = nowService.findAll().stream()
			.map(entry -> new NowView(entry, markdownService.toHtml(entry.getBodyMd())))
			.toList();
		if (!entries.isEmpty()) {
			model.addAttribute("now", entries.getFirst());
			model.addAttribute("archive", entries.subList(1, entries.size()));
		}
		return "now";
	}

}
