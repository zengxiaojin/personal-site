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

import com.jince.site.form.JourneyEntryForm;
import com.jince.site.service.JourneyService;
import com.jince.site.support.DateFormatter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 管理后台：历程的列表、新建、编辑、删除。
 */
@Controller
@RequestMapping("/admin/journey")
@RequiredArgsConstructor
public class AdminJourneyController {

	private static final String FORM_VIEW = "admin/journey/form";

	private final JourneyService journeyService;

	private final DateFormatter dates;

	@GetMapping
	public String list(Model model) {
		model.addAttribute("entries", journeyService.findAll());
		return "admin/journey/list";
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		JourneyEntryForm form = new JourneyEntryForm();
		form.setYear(dates.currentYear());
		model.addAttribute("form", form);
		return FORM_VIEW;
	}

	@PostMapping
	public String create(@Valid @ModelAttribute("form") JourneyEntryForm form, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return FORM_VIEW;
		}
		journeyService.create(form);
		redirectAttributes.addFlashAttribute("message", "历程已保存。");
		return "redirect:/admin/journey";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("form", JourneyEntryForm.from(journeyService.getById(id)));
		model.addAttribute("id", id);
		return FORM_VIEW;
	}

	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("form") JourneyEntryForm form,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("id", id);
			return FORM_VIEW;
		}
		journeyService.update(id, form);
		redirectAttributes.addFlashAttribute("message", "历程已保存。");
		return "redirect:/admin/journey";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		journeyService.delete(id);
		redirectAttributes.addFlashAttribute("message", "历程已删除。");
		return "redirect:/admin/journey";
	}

}
