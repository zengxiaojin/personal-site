package com.jince.site.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jince.site.form.ProfileForm;
import com.jince.site.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 管理后台：编辑名字、自我介绍、「关于」页和联系方式。
 */
@Controller
@RequestMapping("/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

	private static final String FORM_VIEW = "admin/profile";

	private final ProfileService profileService;

	@GetMapping
	public String editForm(Model model) {
		model.addAttribute("form", ProfileForm.from(profileService.get()));
		return FORM_VIEW;
	}

	@PostMapping
	public String update(@Valid @ModelAttribute("form") ProfileForm form, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return FORM_VIEW;
		}
		profileService.update(form);
		redirectAttributes.addFlashAttribute("message", "个人信息已保存。");
		return "redirect:/admin/profile";
	}

}
