package com.jince.site.service;

import java.util.List;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

/**
 * 把 Markdown 转成 HTML。
 */
@Service
public class MarkdownService {

	private final Parser parser;

	private final HtmlRenderer renderer;

	public MarkdownService() {
		List<Extension> extensions = List.of(TablesExtension.create(), AutolinkExtension.create());
		this.parser = Parser.builder().extensions(extensions).build();
		// 正文里直接写的 HTML 标签不会生效，危险的链接（javascript: 等）也会被去掉
		this.renderer = HtmlRenderer.builder()
				.extensions(extensions)
				.escapeHtml(true)
				.sanitizeUrls(true)
				.build();
	}

	public String toHtml(String markdown) {
		if (markdown == null || markdown.isBlank()) {
			return "";
		}
		return renderer.render(parser.parse(markdown));
	}

}
