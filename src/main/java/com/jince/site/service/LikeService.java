package com.jince.site.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jince.site.entity.LikeItem;
import com.jince.site.exception.NotFoundException;
import com.jince.site.form.LikeItemForm;
import com.jince.site.mapper.LikeItemMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 「喜欢的东西」的业务逻辑。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

	private final LikeItemMapper likeItemMapper;

	public List<LikeItem> findAll() {
		return likeItemMapper.findAll();
	}

	public List<LikeItem> findRecent(int limit) {
		return likeItemMapper.findRecent(limit);
	}

	/**
	 * 全部按类别分组。
	 */
	public Map<String, List<LikeItem>> findAllGroupedByCategory() {
		Map<String, List<LikeItem>> groups = new LinkedHashMap<>();
		for (LikeItem item : likeItemMapper.findAll()) {
			groups.computeIfAbsent(item.getCategory(), category -> new ArrayList<>()).add(item);
		}
		return groups;
	}

	public LikeItem getById(Long id) {
		LikeItem item = likeItemMapper.findById(id);
		if (item == null) {
			throw new NotFoundException("没有这一项：id=" + id);
		}
		return item;
	}

	@Transactional
	public LikeItem create(LikeItemForm form) {
		LikeItem item = new LikeItem();
		form.applyTo(item);
		likeItemMapper.insert(item);
		log.info("喜欢的东西已新建：id={}", item.getId());
		return item;
	}

	@Transactional
	public void update(Long id, LikeItemForm form) {
		LikeItem item = getById(id);
		form.applyTo(item);
		likeItemMapper.update(item);
		log.info("喜欢的东西已更新：id={}", id);
	}

	@Transactional
	public void delete(Long id) {
		likeItemMapper.delete(id);
		log.info("喜欢的东西已删除：id={}", id);
	}

}
