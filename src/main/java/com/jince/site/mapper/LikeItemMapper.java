package com.jince.site.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jince.site.entity.LikeItem;

/**
 * like_items 表的读写。SQL 写在 resources/mapper/LikeItemMapper.xml 里。
 */
@Mapper
public interface LikeItemMapper {

	/** 全部，按类别、排序数字排列 */
	List<LikeItem> findAll();

	/** 最近添加的几个 */
	List<LikeItem> findRecent(@Param("limit") int limit);

	LikeItem findById(@Param("id") Long id);

	int insert(LikeItem item);

	int update(LikeItem item);

	int delete(@Param("id") Long id);

}
