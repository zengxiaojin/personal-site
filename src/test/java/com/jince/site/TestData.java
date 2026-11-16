package com.jince.site;

import java.time.OffsetDateTime;

import com.jince.site.entity.Post;
import com.jince.site.entity.PostStatus;
import com.jince.site.entity.Profile;

/**
 * 测试里反复用到的示例数据。
 */
public final class TestData {

	private TestData() {
	}

	public static Profile profile() {
		Profile profile = new Profile();
		profile.setId(1);
		profile.setName("测试用户");
		profile.setIntro("这是测试用的自我介绍。");
		profile.setAboutMd("关于我。");
		return profile;
	}

	public static Post publishedPost(String slug, String title, OffsetDateTime publishedAt) {
		Post post = new Post();
		post.setId(1L);
		post.setSlug(slug);
		post.setTitle(title);
		post.setBodyMd("正文的**内容**。");
		post.setStatus(PostStatus.PUBLISHED);
		post.setPublishedAt(publishedAt);
		post.setCreatedAt(publishedAt);
		post.setUpdatedAt(publishedAt);
		return post;
	}

}
