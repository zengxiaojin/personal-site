package com.jince.site.controller.admin;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jince.site.TestData;
import com.jince.site.WebTestConfig;
import com.jince.site.form.PostForm;
import com.jince.site.service.PostService;
import com.jince.site.service.ProfileService;

/**
 * 管理后台文章页面的测试：登录保护、表单校验、保存。
 */
@WebMvcTest(AdminPostController.class)
@Import(WebTestConfig.class)
class AdminPostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PostService postService;

	@MockitoBean
	private ProfileService profileService;

	@BeforeEach
	void setUp() {
		when(profileService.get()).thenReturn(TestData.profile());
	}

	@Test
	@DisplayName("没有登录时，会被带到登录页")
	void redirectsToLoginWhenNotLoggedIn() throws Exception {
		mockMvc.perform(get("/admin/posts"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/login"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("登录后可以看到文章列表")
	void showsListWhenLoggedIn() throws Exception {
		when(postService.findAll()).thenReturn(List.of());

		mockMvc.perform(get("/admin/posts"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/posts/list"))
			.andExpect(content().string(containsString("还没有文章。")));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("内容填写正确时保存文章，并回到列表")
	void createsPostWhenFormIsValid() throws Exception {
		mockMvc.perform(post("/admin/posts").with(csrf())
				.param("title", "第一篇文章")
				.param("slug", "first-post")
				.param("bodyMd", "正文")
				.param("status", "PUBLISHED"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/posts"))
			.andExpect(flash().attribute("message", "文章已保存。"));

		verify(postService).create(any(PostForm.class));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("标题没填、网址名格式不对时，不保存并显示错误提示")
	void showsErrorsWhenFormIsInvalid() throws Exception {
		mockMvc.perform(post("/admin/posts").with(csrf())
				.param("title", "")
				.param("slug", "有中文 和空格")
				.param("bodyMd", "正文")
				.param("status", "DRAFT"))
			.andExpect(status().isOk())
			.andExpect(view().name("admin/posts/form"))
			.andExpect(model().attributeHasFieldErrors("form", "title", "slug"))
			.andExpect(content().string(containsString("请填写标题")));

		verify(postService, never()).create(any(PostForm.class));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("网址名和别的文章重复时，不保存并显示错误提示")
	void showsErrorWhenSlugIsTaken() throws Exception {
		when(postService.isSlugTaken("first-post", null)).thenReturn(true);

		mockMvc.perform(post("/admin/posts").with(csrf())
				.param("title", "第一篇文章")
				.param("slug", "first-post")
				.param("bodyMd", "正文")
				.param("status", "DRAFT"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("form", "slug"))
			.andExpect(content().string(containsString("这个网址名已经被别的文章使用了")));

		verify(postService, never()).create(any(PostForm.class));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("没有防伪造令牌的提交会被拒绝")
	void rejectsPostWithoutCsrfToken() throws Exception {
		mockMvc.perform(post("/admin/posts")
				.param("title", "第一篇文章")
				.param("slug", "first-post")
				.param("bodyMd", "正文")
				.param("status", "DRAFT"))
			.andExpect(status().isForbidden());

		verify(postService, never()).create(any(PostForm.class));
	}

}
