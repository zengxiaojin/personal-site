package com.jince.site.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.jince.site.entity.Profile;

/**
 * profile 表的读写。SQL 写在 resources/mapper/ProfileMapper.xml 里。
 */
@Mapper
public interface ProfileMapper {

	Profile find();

	int update(Profile profile);

}
