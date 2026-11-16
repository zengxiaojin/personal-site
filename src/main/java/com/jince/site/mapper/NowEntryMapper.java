package com.jince.site.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jince.site.entity.NowEntry;

/**
 * now_entries 表的读写。SQL 写在 resources/mapper/NowEntryMapper.xml 里。
 */
@Mapper
public interface NowEntryMapper {

	/** 最新的一条。一条都没有时返回 null */
	NowEntry findLatest();

	/** 全部近况，新的在前 */
	List<NowEntry> findAll();

	NowEntry findById(@Param("id") Long id);

	int insert(NowEntry entry);

	int update(NowEntry entry);

	int delete(@Param("id") Long id);

}
