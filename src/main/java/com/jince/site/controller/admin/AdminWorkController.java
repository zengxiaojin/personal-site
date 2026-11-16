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

import com.jince.site.form.WorkForm;
import com.jince.site.service.WorkService;
import com.jince.site.support.DateFormatter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 管理后台：作品的列表、新建、编辑、删除。
 */
@Controller
@RequestMapping("/admin/works")
@RequiredArgsConstructor
public class AdminWorkController {

	private static final String FORM_VIEW = "admin/works/form";

	private final WorkService workService;

	private final DateFormatter dates;

	@GetMapping
	public String list(Model model) {
		model.addAttribute("works", workService.findAll());
		return "admin/works/list";
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		WorkForm form = new WorkForm();
		form.setYear(dates.currentYear());
		model.addAttribute("form", form);
		return FORM_VIEW;
	}

	@PostMapping
	public String create(@Valid @ModelAttribute("form") WorkForm form, BindingResult result,
			RedirectAttributes redirectAttributes) {
		rejectIfSlugTaken(form, null, result);
		if (result.hasErrors()) {
			return FORM_VIEW;
		}
		workService.create(form);
		redirectAttributes.addFlashAttribute("message", "作品已保存。");
		return "redirect:/admin/works";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("form", WorkForm.from(workService.getById(id)));
		model.addAttribute("id", id);
		return FORM_VIEW;
	}

	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("form") WorkForm form, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		rejectIfSlugTaken(form, id, result);
		if (result.hasErrors()) {
			model.addAttribute("id", id);
			return FORM_VIEW;
		}
		workService.update(id, form);
		redirectAttributes.addFlashAttribute("message", "作品已保存。");
		return "redirect:/admin/works";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		workService.delete(id);
		redirectAttributes.addFlashAttribute("message", "作品已删除。");
		return "redirect:/admin/works";
	}

	/**
	 * 网址名不能和别的作品重复。重复时在「网址名」下面显示错误提示。
	 */
	private void rejectIfSlugTaken(WorkForm form, Long excludeId, BindingResult result) {
		if (form.getSlug() != null && !result.hasFieldErrors("slug")
				&& workService.isSlugTaken(form.getSlug(), excludeId)) {
			result.rejectValue("slug", "duplicate", "这个网址名已经被别的作品使用了");
		}
	}

}
