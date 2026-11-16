package com.jince.site.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MarkdownServiceTest {

	private final MarkdownService markdownService = new MarkdownService();

	@Test
	@DisplayName("Markdown 的标题和列表会转成 HTML")
	void convertsMarkdownToHtml() {
		String html = markdownService.toHtml("## 小标题\n\n- 第一项\n- 第二项");

		assertThat(html).contains("<h2>小标题</h2>");
		assertThat(html).contains("<li>第一项</li>");
	}

	@Test
	@DisplayName("正文里直接写的 HTML 标签不会生效")
	void escapesRawHtml() {
		String html = markdownService.toHtml("<script>alert('x')</script>");

		assertThat(html).doesNotContain("<script>");
		assertThat(html).contains("&lt;script&gt;");
	}

	@Test
	@DisplayName("危险的链接地址会被去掉")
	void sanitizesDangerousLinks() {
		String html = markdownService.toHtml("[点我](javascript:alert('x'))");

		assertThat(html).doesNotContain("javascript:");
	}

	@Test
	@DisplayName("没有内容时返回空字符串")
	void returnsEmptyStringForBlankInput() {
		assertThat(markdownService.toHtml(null)).isEmpty();
		assertThat(markdownService.toHtml("   ")).isEmpty();
	}

}
