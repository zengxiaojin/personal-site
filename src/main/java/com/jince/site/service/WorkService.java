package com.jince.site.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jince.site.entity.Work;
import com.jince.site.exception.NotFoundException;
import com.jince.site.form.WorkForm;
import com.jince.site.mapper.WorkMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 作品的业务逻辑。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkService {

	private final WorkMapper workMapper;

	public List<Work> findAll() {
		return workMapper.findAll();
	}

	public List<Work> findTop(int limit) {
		return workMapper.findTop(limit);
	}

	public Work getBySlug(String slug) {
		Work work = workMapper.findBySlug(slug);
		if (work == null) {
			throw new NotFoundException("没有这个作品：" + slug);
		}
		return work;
	}

	public Work getById(Long id) {
		Work work = workMapper.findById(id);
		if (work == null) {
			throw new NotFoundException("没有这个作品：id=" + id);
		}
		return work;
	}

	/**
	 * 这个网址名是否已经被别的作品使用。
	 *
	 * @param excludeId 正在编辑的作品的 id（新建时为 null）
	 */
	public boolean isSlugTaken(String slug, Long excludeId) {
		return workMapper.countBySlug(slug, excludeId) > 0;
	}

	@Transactional
	public Work create(WorkForm form) {
		Work work = new Work();
		form.applyTo(work);
		workMapper.insert(work);
		log.info("作品已新建：id={}, slug={}", work.getId(), work.getSlug());
		return work;
	}

	@Transactional
	public void update(Long id, WorkForm form) {
		Work work = getById(id);
		form.applyTo(work);
		workMapper.update(work);
		log.info("作品已更新：id={}, slug={}", id, work.getSlug());
	}

	@Transactional
	public void delete(Long id) {
		workMapper.delete(id);
		log.info("作品已删除：id={}", id);
	}

}
