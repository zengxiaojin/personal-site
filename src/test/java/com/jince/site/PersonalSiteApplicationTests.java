package com.jince.site;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/**
 * 把整个网站启动起来的测试：建表脚本、数据库读写、页面模板全部连在一起确认。
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class PersonalSiteApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("首页能显示初始内容")
	void homePageShowsInitialContent() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("你好，我是曾进。")))
			.andExpect(content().string(containsString("网站开张了")))
			.andExpect(content().string(containsString("这个网站")));
	}

	@ParameterizedTest
	@ValueSource(strings = { "/about", "/posts", "/posts/hello", "/works", "/works/this-site", "/now", "/journey",
			"/likes" })
	@DisplayName("每个公开页面都能正常打开")
	void publicPagesAreAvailable(String path) throws Exception {
		mockMvc.perform(get(path))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("曾进")));
	}

	@Test
	@DisplayName("订阅源里有已发布的文章")
	void feedContainsPublishedPosts() throws Exception {
		mockMvc.perform(get("/feed.xml"))
			.andExpect(status().isOk())
			.andExpect(header().string("Content-Type", containsString("application/atom+xml")))
			.andExpect(content().string(containsString("<title>网站开张了</title>")));
	}

	@Test
	@DisplayName("不存在的文章返回 404")
	void missingPostReturns404() throws Exception {
		mockMvc.perform(get("/posts/no-such-post"))
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("没有登录时不能进入管理后台")
	void adminRequiresLogin() throws Exception {
		mockMvc.perform(get("/admin"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/admin/login"));
	}

	@Test
	@DisplayName("健康检查接口可以访问")
	void healthEndpointIsAvailable() throws Exception {
		mockMvc.perform(get("/actuator/health"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("UP")));
	}

}
