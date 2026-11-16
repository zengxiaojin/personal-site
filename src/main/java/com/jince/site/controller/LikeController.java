package com.jince.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jince.site.service.LikeService;

import lombok.RequiredArgsConstructor;

/**
 * 「喜欢」页：喜欢的东西。
 */
@Controller
@RequiredArgsConstructor
public class LikeController {

	private final LikeService likeService;

	@GetMapping("/likes")
	public String likes(Model model) {
		model.addAttribute("likesByCategory", likeService.findAllGroupedByCategory());
		return "likes";
	}

}
