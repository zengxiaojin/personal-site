package com.jince.site.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jince.site.TestData;
import com.jince.site.config.AppProperties;
import com.jince.site.entity.Post;
import com.jince.site.entity.PostStatus;
import com.jince.site.exception.NotFoundException;
import com.jince.site.form.PostForm;
import com.jince.site.mapper.PostMapper;
import com.jince.site.support.DateFormatter;

/**
 * PostService 的单元测试。数据库部分用 Mockito 做的替身代替，所以不需要启动数据库。
 */
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@Mock
	private PostMapper postMapper;

	private PostService postService;

	@BeforeEach
	void setUp() {
		AppProperties properties = new AppProperties(
				new AppProperties.Site("http://localhost:8080", ZoneId.of("Asia/Tokyo")),
				new AppProperties.Admin("admin", "password"));
		postService = new PostService(postMapper, new DateFormatter(properties));
	}

	@Test
	@DisplayName("以「已发布」保存新文章时，会记下发布时间")
	void createPublishedPostSetsPublishedAt() {
		PostForm form = form(PostStatus.PUBLISHED);

		postService.create(form);

		ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
		verify(postMapper).insert(captor.capture());
		assertThat(captor.getValue().getPublishedAt()).isNotNull();
		assertThat(captor.getValue().getSlug()).isEqualTo("hello");
	}

	@Test
	@DisplayName("以「草稿」保存新文章时，发布时间为空")
	void createDraftPostLeavesPublishedAtEmpty() {
		PostForm form = form(PostStatus.DRAFT);

		postService.create(form);

		ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
		verify(postMapper).insert(captor.capture());
		assertThat(captor.getValue().getPublishedAt()).isNull();
	}

	@Test
	@DisplayName("编辑已发布的文章时，发布时间保持不变")
	void updateKeepsOriginalPublishedAt() {
		OffsetDateTime publishedAt = OffsetDateTime.of(2026, 1, 15, 9, 0, 0, 0, ZoneOffset.ofHours(9));
		when(postMapper.findById(1L)).thenReturn(TestData.publishedPost("hello", "旧标题", publishedAt));
		PostForm form = form(PostStatus.PUBLISHED);
		form.setTitle("新标题");

		postService.update(1L, form);

		ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
		verify(postMapper).update(captor.capture());
		assertThat(captor.getValue().getTitle()).isEqualTo("新标题");
		assertThat(captor.getValue().getPublishedAt()).isEqualTo(publishedAt);
	}

	@Test
	@DisplayName("找不到文章时抛出 NotFoundException")
	void getPublishedBySlugThrowsWhenMissing() {
		when(postMapper.findPublishedBySlug("nothing")).thenReturn(null);

		assertThatThrownBy(() -> postService.getPublishedBySlug("nothing"))
			.isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("文章按发布年份分组，年份以网站设定的时区为准")
	void groupsPublishedPostsByYear() {
		// 世界标准时间 2025-12-31 16:00 在东京已经是 2026-01-01 01:00
		Post newYear = TestData.publishedPost("new-year", "新年",
				OffsetDateTime.of(2025, 12, 31, 16, 0, 0, 0, ZoneOffset.UTC));
		Post autumn = TestData.publishedPost("autumn", "秋天",
				OffsetDateTime.of(2025, 10, 1, 3, 0, 0, 0, ZoneOffset.UTC));
		when(postMapper.findPublished()).thenReturn(List.of(newYear, autumn));

		Map<Integer, List<Post>> groups = postService.findPublishedGroupedByYear();

		assertThat(groups.keySet()).containsExactly(2026, 2025);
		assertThat(groups.get(2026)).containsExactly(newYear);
		assertThat(groups.get(2025)).containsExactly(autumn);
	}

	@Test
	@DisplayName("网址名已被使用时返回 true")
	void isSlugTakenReturnsTrueWhenCountIsPositive() {
		when(postMapper.countBySlug("hello", null)).thenReturn(1);
		when(postMapper.countBySlug("free", null)).thenReturn(0);

		assertThat(postService.isSlugTaken("hello", null)).isTrue();
		assertThat(postService.isSlugTaken("free", null)).isFalse();
	}

	private static PostForm form(PostStatus status) {
		PostForm form = new PostForm();
		form.setSlug("hello");
		form.setTitle("标题");
		form.setBodyMd("正文");
		form.setStatus(status);
		return form;
	}

}
