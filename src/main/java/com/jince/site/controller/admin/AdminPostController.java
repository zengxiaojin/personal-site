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

import com.jince.site.entity.PostStatus;
import com.jince.site.form.PostForm;
import com.jince.site.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 管理后台：文章的列表、新建、编辑、删除。
 */
@Controller
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

	private static final String FORM_VIEW = "admin/posts/form";

	private final PostService postService;

	/** 表单里「状态」下拉框的选项 */
	@ModelAttribute("statuses")
	public PostStatus[] statuses() {
		return PostStatus.values();
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("posts", postService.findAll());
		return "admin/posts/list";
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		model.addAttribute("form", new PostForm());
		return FORM_VIEW;
	}

	@PostMapping
	public String create(@Valid @ModelAttribute("form") PostForm form, BindingResult result,
			RedirectAttributes redirectAttributes) {
		rejectIfSlugTaken(form, null, result);
		if (result.hasErrors()) {
			return FORM_VIEW;
		}
		postService.create(form);
		redirectAttributes.addFlashAttribute("message", "文章已保存。");
		return "redirect:/admin/posts";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("form", PostForm.from(postService.getById(id)));
		model.addAttribute("id", id);
		return FORM_VIEW;
	}

	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("form") PostForm form, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		rejectIfSlugTaken(form, id, result);
		if (result.hasErrors()) {
			model.addAttribute("id", id);
			return FORM_VIEW;
		}
		postService.update(id, form);
		redirectAttributes.addFlashAttribute("message", "文章已保存。");
		return "redirect:/admin/posts";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		postService.delete(id);
		redirectAttributes.addFlashAttribute("message", "文章已删除。");
		return "redirect:/admin/posts";
	}

	/**
	 * 网址名不能和别的文章重复。重复时在「网址名」下面显示错误提示。
	 */
	private void rejectIfSlugTaken(PostForm form, Long excludeId, BindingResult result) {
		if (form.getSlug() != null && !result.hasFieldErrors("slug")
				&& postService.isSlugTaken(form.getSlug(), excludeId)) {
			result.rejectValue("slug", "duplicate", "这个网址名已经被别的文章使用了");
		}
	}

}
