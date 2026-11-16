package com.jince.site.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jince.site.entity.Work;

/**
 * works 表的读写。SQL 写在 resources/mapper/WorkMapper.xml 里。
 */
@Mapper
public interface WorkMapper {

	/** 全部作品，按排序数字、年份排列 */
	List<Work> findAll();

	/** 排在最前面的几个 */
	List<Work> findTop(@Param("limit") int limit);

	Work findBySlug(@Param("slug") String slug);

	Work findById(@Param("id") Long id);

	/** 使用这个网址名的作品数量。excludeId 不为空时，不把这一个算在内 */
	int countBySlug(@Param("slug") String slug, @Param("excludeId") Long excludeId);

	int insert(Work work);

	int update(Work work);

	int delete(@Param("id") Long id);

}
