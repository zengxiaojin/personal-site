package com.jince.site.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 管理后台的登录页。账号密码的核对由 Spring Security 完成。
 */
@Controller
public class AdminLoginController {

	@GetMapping("/admin/login")
	public String login() {
		return "admin/login";
	}

}
