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

import com.jince.site.form.NowEntryForm;
import com.jince.site.service.NowService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 管理后台：近况的列表、新建、编辑、删除。
 */
@Controller
@RequestMapping("/admin/now")
@RequiredArgsConstructor
public class AdminNowController {

	private static final String FORM_VIEW = "admin/now/form";

	private final NowService nowService;

	@GetMapping
	public String list(Model model) {
		model.addAttribute("entries", nowService.findAll());
		return "admin/now/list";
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		NowEntryForm form = new NowEntryForm();
		// 新写近况时，先把上一次的内容带出来，在它的基础上修改
		nowService.findLatest().ifPresent(latest -> form.setBodyMd(latest.getBodyMd()));
		model.addAttribute("form", form);
		return FORM_VIEW;
	}

	@PostMapping
	public String create(@Valid @ModelAttribute("form") NowEntryForm form, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return FORM_VIEW;
		}
		nowService.create(form);
		redirectAttributes.addFlashAttribute("message", "近况已保存。");
		return "redirect:/admin/now";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("form", NowEntryForm.from(nowService.getById(id)));
		model.addAttribute("id", id);
		return FORM_VIEW;
	}

	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @Valid @ModelAttribute("form") NowEntryForm form,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("id", id);
			return FORM_VIEW;
		}
		nowService.update(id, form);
		redirectAttributes.addFlashAttribute("message", "近况已保存。");
		return "redirect:/admin/now";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		nowService.delete(id);
		redirectAttributes.addFlashAttribute("message", "近况已删除。");
		return "redirect:/admin/now";
	}

}
