package com.jince.site.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jince.site.config.AppProperties;
import com.jince.site.entity.Post;
import com.jince.site.entity.Profile;
import com.jince.site.service.MarkdownService;
import com.jince.site.service.PostService;
import com.jince.site.service.ProfileService;
import com.jince.site.support.DateFormatter;

import lombok.RequiredArgsConstructor;

/**
 * 文章的订阅源（Atom 格式）。读者可以用 RSS 阅读器订阅 /feed.xml。
 */
@Controller
@RequiredArgsConstructor
public class FeedController {

	private static final int ENTRY_COUNT = 20;

	private final PostService postService;

	private final ProfileService profileService;

	private final MarkdownService markdownService;

	private final DateFormatter dates;

	private final AppProperties properties;

	@GetMapping(value = "/feed.xml", produces = "application/atom+xml;charset=UTF-8")
	@ResponseBody
	public String feed() {
		Profile profile = profileService.get();
		List<Post> posts = postService.findLatestPublished(ENTRY_COUNT);
		String baseUrl = properties.site().baseUrl().replaceAll("/+$", "");
		OffsetDateTime updated = posts.isEmpty() ? OffsetDateTime.now() : posts.getFirst().getUpdatedAt();

		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		xml.append("<feed xmlns=\"http://www.w3.org/2005/Atom\">\n");
		xml.append("  <title>").append(escape(profile.getName())).append("</title>\n");
		xml.append("  <id>").append(escape(baseUrl)).append("/</id>\n");
		xml.append("  <link href=\"").append(escape(baseUrl)).append("/\"/>\n");
		xml.append("  <link rel=\"self\" href=\"").append(escape(baseUrl)).append("/feed.xml\"/>\n");
		xml.append("  <updated>").append(dates.iso(updated)).append("</updated>\n");
		xml.append("  <author><name>").append(escape(profile.getName())).append("</name></author>\n");
		for (Post post : posts) {
			String url = baseUrl + "/posts/" + post.getSlug();
			xml.append("  <entry>\n");
			xml.append("    <title>").append(escape(post.getTitle())).append("</title>\n");
			xml.append("    <id>").append(escape(url)).append("</id>\n");
			xml.append("    <link href=\"").append(escape(url)).append("\"/>\n");
			xml.append("    <published>").append(dates.iso(post.getPublishedAt())).append("</published>\n");
			xml.append("    <updated>").append(dates.iso(post.getUpdatedAt())).append("</updated>\n");
			if (post.getSummary() != null) {
				xml.append("    <summary>").append(escape(post.getSummary())).append("</summary>\n");
			}
			xml.append("    <content type=\"html\">")
				.append(escape(markdownService.toHtml(post.getBodyMd())))
				.append("</content>\n");
			xml.append("  </entry>\n");
		}
		xml.append("</feed>\n");
		return xml.toString();
	}

	/**
	 * 把 XML 里有特殊含义的字符换成安全的写法。
	 */
	private static String escape(String text) {
		return text
			.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;")
			.replace("'", "&apos;");
	}

}
