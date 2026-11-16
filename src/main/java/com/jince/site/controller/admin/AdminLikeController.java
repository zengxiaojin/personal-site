package com.jince.site.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jince.site.form.LikeItemForm;
import com.jince.site.service.LikeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 管理后台：「喜欢的东西」的列表、新建、编辑、删除。
 */
@Controller
@RequestMapping("/admin/likes")
@RequiredArgsConstructor
public class AdminLikeController {

	private static final String FORM_VIEW = "admin/likes/form";

	private final LikeService likeService;

	@GetMapping
	public String list(Model model) {
		model.addAttribute("items", likeService.findAll());
		return "admin/likes/list";
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		model.addAttribute("form", new LikeItemForm());
		return FORM_VIEW;
	}

	@PostMapping
	public String create(@Valid @ModelAttribute("form") LikeItemForm form, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return FORM_VIEW;
		}
		likeService.create(form);
		redirectAttributes.addFlashAttribute("message", "已保存。");
		return "redirect:/admin/likes";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("form", LikeItemForm.from(likeService.getById(id)));
		model.addAttribute("id", id);
		return FORM_VIEW;
	}

	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("form") LikeItemForm form,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("id", id);
			return FORM_VIEW;
		}
		likeService.update(id, form);
		redirectAttributes.addFlashAttribute("message", "已保存。");
		return "redirect:/admin/likes";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		likeService.delete(id);
		redirectAttributes.addFlashAttribute("message", "已删除。");
		return "redirect:/admin/likes";
	}

}
