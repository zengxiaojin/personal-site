package com.jince.site.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jince.site.entity.Post;

/**
 * posts 表的读写。SQL 写在 resources/mapper/PostMapper.xml 里。
 */
@Mapper
public interface PostMapper {

	/** 已发布的全部文章，新的在前 */
	List<Post> findPublished();

	/** 已发布的最新几篇 */
	List<Post> findLatestPublished(@Param("limit") int limit);

	Post findPublishedBySlug(@Param("slug") String slug);

	/** 全部文章（包括草稿），供管理后台使用 */
	List<Post> findAll();

	Post findById(@Param("id") Long id);

	/** 使用这个网址名的文章数量。excludeId 不为空时，不把这一篇算在内 */
	int countBySlug(@Param("slug") String slug, @Param("excludeId") Long excludeId);

	int insert(Post post);

	int update(Post post);

	int delete(@Param("id") Long id);

}
