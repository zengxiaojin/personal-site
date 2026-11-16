package com.jince.site.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jince.site.TestData;
import com.jince.site.WebTestConfig;
import com.jince.site.entity.Post;
import com.jince.site.exception.NotFoundException;
import com.jince.site.service.PostService;
import com.jince.site.service.ProfileService;

/**
 * 文章页面的测试。只启动 Web 部分，Service 用替身代替。
 */
@WebMvcTest(PostController.class)
@Import(WebTestConfig.class)
class PostControllerTest {

	private static final OffsetDateTime PUBLISHED_AT = OffsetDateTime.of(2026, 11, 16, 3, 0, 0, 0, ZoneOffset.UTC);

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
	@DisplayName("文章列表页按年份显示文章")
	void listShowsPostsGroupedByYear() throws Exception {
		Post post = TestData.publishedPost("hello", "第一篇文章", PUBLISHED_AT);
		when(postService.findPublishedGroupedByYear()).thenReturn(Map.of(2026, List.of(post)));

		mockMvc.perform(get("/posts"))
			.andExpect(status().isOk())
			.andExpect(view().name("posts/list"))
			.andExpect(content().string(containsString("第一篇文章")))
			.andExpect(content().string(containsString("/posts/hello")));
	}

	@Test
	@DisplayName("文章正文页显示标题、日期和转成 HTML 的正文")
	void detailShowsPost() throws Exception {
		when(postService.getPublishedBySlug("hello"))
			.thenReturn(TestData.publishedPost("hello", "第一篇文章", PUBLISHED_AT));

		mockMvc.perform(get("/posts/hello"))
			.andExpect(status().isOk())
			.andExpect(view().name("posts/detail"))
			.andExpect(content().string(containsString("第一篇文章")))
			.andExpect(content().string(containsString("2026.11.16")))
			.andExpect(content().string(containsString("<strong>内容</strong>")));
	}

	@Test
	@DisplayName("文章不存在时返回 404")
	void detailReturns404WhenMissing() throws Exception {
		when(postService.getPublishedBySlug("nothing")).thenThrow(new NotFoundException("没有这篇文章"));

		mockMvc.perform(get("/posts/nothing"))
			.andExpect(status().isNotFound());
	}

}
