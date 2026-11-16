package com.jince.site.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import com.jince.site.TestcontainersConfiguration;
import com.jince.site.entity.Post;
import com.jince.site.entity.PostStatus;

/**
 * PostMapper 的测试：在真正的 PostgreSQL 上执行 SQL。
 * 每个测试结束后数据会自动恢复原状，测试之间互不影响。
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class PostMapperTest {

	@Autowired
	private PostMapper postMapper;

	@Test
	@DisplayName("保存文章后会得到自动生成的 id，并且能按 id 取回")
	void insertGeneratesIdAndCanBeFound() {
		Post post = newPost("mapper-test", "测试文章", PostStatus.PUBLISHED, OffsetDateTime.now());

		postMapper.insert(post);

		assertThat(post.getId()).isNotNull();
		Post found = postMapper.findById(post.getId());
		assertThat(found.getTitle()).isEqualTo("测试文章");
		assertThat(found.getStatus()).isEqualTo(PostStatus.PUBLISHED);
		assertThat(found.getPublishedAt()).isNotNull();
		assertThat(found.getCreatedAt()).isNotNull();
	}

	@Test
	@DisplayName("公开页面只能取到已发布的文章，取不到草稿")
	void findPublishedExcludesDrafts() {
		postMapper.insert(newPost("published-one", "已发布的文章", PostStatus.PUBLISHED, OffsetDateTime.now()));
		postMapper.insert(newPost("draft-one", "草稿", PostStatus.DRAFT, null));

		List<Post> published = postMapper.findPublished();

		assertThat(published).extracting(Post::getSlug).contains("published-one").doesNotContain("draft-one");
		assertThat(postMapper.findPublishedBySlug("draft-one")).isNull();
		assertThat(postMapper.findAll()).extracting(Post::getSlug).contains("published-one", "draft-one");
	}

	@Test
	@DisplayName("已发布的文章按发布时间从新到旧排列")
	void findLatestPublishedOrdersByPublishedAtDescending() {
		OffsetDateTime now = OffsetDateTime.now();
		postMapper.insert(newPost("older", "旧文章", PostStatus.PUBLISHED, now.plusDays(1)));
		postMapper.insert(newPost("newer", "新文章", PostStatus.PUBLISHED, now.plusDays(2)));

		List<Post> latest = postMapper.findLatestPublished(2);

		assertThat(latest).extracting(Post::getSlug).containsExactly("newer", "older");
	}

	@Test
	@DisplayName("检查网址名是否重复时，可以不把正在编辑的那一篇算在内")
	void countBySlugCanExcludeOnePost() {
		Post post = newPost("unique-slug", "测试文章", PostStatus.DRAFT, null);
		postMapper.insert(post);

		assertThat(postMapper.countBySlug("unique-slug", null)).isEqualTo(1);
		assertThat(postMapper.countBySlug("unique-slug", post.getId())).isZero();
		assertThat(postMapper.countBySlug("not-used", null)).isZero();
	}

	@Test
	@DisplayName("可以修改和删除文章")
	void updateAndDelete() {
		Post post = newPost("to-change", "修改前", PostStatus.DRAFT, null);
		postMapper.insert(post);

		post.setTitle("修改后");
		post.setSummary("摘要");
		postMapper.update(post);
		assertThat(postMapper.findById(post.getId()).getTitle()).isEqualTo("修改后");

		postMapper.delete(post.getId());
		assertThat(postMapper.findById(post.getId())).isNull();
	}

	private static Post newPost(String slug, String title, PostStatus status, OffsetDateTime publishedAt) {
		Post post = new Post();
		post.setSlug(slug);
		post.setTitle(title);
		post.setBodyMd("正文");
		post.setStatus(status);
		post.setPublishedAt(publishedAt);
		return post;
	}

}
