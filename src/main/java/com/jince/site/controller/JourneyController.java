package com.jince.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jince.site.service.JourneyService;

import lombok.RequiredArgsConstructor;

/**
 * 「历程」页：人生时间线。
 */
@Controller
@RequiredArgsConstructor
public class JourneyController {

	private final JourneyService journeyService;

	@GetMapping("/journey")
	public String journey(Model model) {
		model.addAttribute("journeyByYear", journeyService.findAllGroupedByYear());
		return "journey";
	}

}
