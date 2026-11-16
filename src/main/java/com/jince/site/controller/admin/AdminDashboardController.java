package com.jince.site.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jince.site.entity.Post;
import com.jince.site.entity.PostStatus;
import com.jince.site.service.JourneyService;
import com.jince.site.service.LikeService;
import com.jince.site.service.NowService;
import com.jince.site.service.PostService;
import com.jince.site.service.WorkService;

import lombok.RequiredArgsConstructor;

/**
 * 管理后台的首页：各类内容的数量一览。
 */
@Controller
@RequiredArgsConstructor
public class AdminDashboardController {

	private final PostService postService;

	private final NowService nowService;

	private final WorkService workService;

	private final JourneyService journeyService;

	private final LikeService likeService;

	@GetMapping("/admin")
	public String dashboard(Model model) {
		List<Post> posts = postService.findAll();
		long draftCount = posts.stream().filter(post -> post.getStatus() == PostStatus.DRAFT).count();
		model.addAttribute("postCount", posts.size());
		model.addAttribute("draftCount", draftCount);
		model.addAttribute("nowCount", nowService.findAll().size());
		model.addAttribute("workCount", workService.findAll().size());
		model.addAttribute("journeyCount", journeyService.findAll().size());
		model.addAttribute("likeCount", likeService.findAll().size());
		return "admin/dashboard";
	}

}
