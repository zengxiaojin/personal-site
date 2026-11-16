package com.jince.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jince.site.dto.NowView;
import com.jince.site.service.JourneyService;
import com.jince.site.service.LikeService;
import com.jince.site.service.MarkdownService;
import com.jince.site.service.NowService;
import com.jince.site.service.PostService;
import com.jince.site.service.WorkService;

import lombok.RequiredArgsConstructor;

/**
 * 首页。
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

	private static final int POST_COUNT = 5;

	private static final int WORK_COUNT = 4;

	private static final int JOURNEY_COUNT = 4;

	private static final int LIKE_COUNT = 6;

	private final NowService nowService;

	private final PostService postService;

	private final WorkService workService;

	private final JourneyService journeyService;

	private final LikeService likeService;

	private final MarkdownService markdownService;

	@GetMapping("/")
	public String index(Model model) {
		nowService.findLatest()
			.map(entry -> new NowView(entry, markdownService.toHtml(entry.getBodyMd())))
			.ifPresent(now -> model.addAttribute("now", now));
		model.addAttribute("posts", postService.findLatestPublished(POST_COUNT));
		model.addAttribute("works", workService.findTop(WORK_COUNT));
		model.addAttribute("journey", journeyService.findRecent(JOURNEY_COUNT));
		model.addAttribute("likes", likeService.findRecent(LIKE_COUNT));
		return "index";
	}

}
