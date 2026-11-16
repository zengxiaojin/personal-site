package com.jince.site.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jince.site.entity.JourneyEntry;

/**
 * journey_entries 表的读写。SQL 写在 resources/mapper/JourneyEntryMapper.xml 里。
 */
@Mapper
public interface JourneyEntryMapper {

	/** 全部历程，新的在前 */
	List<JourneyEntry> findAll();

	/** 最近的几件事 */
	List<JourneyEntry> findRecent(@Param("limit") int limit);

	JourneyEntry findById(@Param("id") Long id);

	int insert(JourneyEntry entry);

	int update(JourneyEntry entry);

	int delete(@Param("id") Long id);

}
