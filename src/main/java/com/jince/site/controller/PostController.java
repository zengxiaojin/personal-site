package com.jince.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jince.site.entity.Post;
import com.jince.site.service.MarkdownService;
import com.jince.site.service.PostService;

import lombok.RequiredArgsConstructor;

/**
 * 文章的列表页和正文页。
 */
@Controller
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	private final MarkdownService markdownService;

	@GetMapping("/posts")
	public String list(Model model) {
		model.addAttribute("postsByYear", postService.findPublishedGroupedByYear());
		return "posts/list";
	}

	@GetMapping("/posts/{slug}")
	public String detail(@PathVariable String slug, Model model) {
		Post post = postService.getPublishedBySlug(slug);
		model.addAttribute("post", post);
		model.addAttribute("bodyHtml", markdownService.toHtml(post.getBodyMd()));
		return "posts/detail";
	}

}
