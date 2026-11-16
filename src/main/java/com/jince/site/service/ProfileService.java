package com.jince.site.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jince.site.entity.Profile;
import com.jince.site.form.ProfileForm;
import com.jince.site.mapper.ProfileMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 个人信息的业务逻辑。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

	private final ProfileMapper profileMapper;

	public Profile get() {
		return profileMapper.find();
	}

	@Transactional
	public void update(ProfileForm form) {
		Profile profile = profileMapper.find();
		form.applyTo(profile);
		profileMapper.update(profile);
		log.info("个人信息已更新");
	}

}
