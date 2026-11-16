package com.jince.site.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jince.site.entity.NowEntry;
import com.jince.site.exception.NotFoundException;
import com.jince.site.form.NowEntryForm;
import com.jince.site.mapper.NowEntryMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 近况的业务逻辑。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NowService {

	private final NowEntryMapper nowEntryMapper;

	public Optional<NowEntry> findLatest() {
		return Optional.ofNullable(nowEntryMapper.findLatest());
	}

	public List<NowEntry> findAll() {
		return nowEntryMapper.findAll();
	}

	public NowEntry getById(Long id) {
		NowEntry entry = nowEntryMapper.findById(id);
		if (entry == null) {
			throw new NotFoundException("没有这条近况：id=" + id);
		}
		return entry;
	}

	@Transactional
	public NowEntry create(NowEntryForm form) {
		NowEntry entry = new NowEntry();
		form.applyTo(entry);
		nowEntryMapper.insert(entry);
		log.info("近况已新建：id={}", entry.getId());
		return entry;
	}

	@Transactional
	public void update(Long id, NowEntryForm form) {
		NowEntry entry = getById(id);
		form.applyTo(entry);
		nowEntryMapper.update(entry);
		log.info("近况已更新：id={}", id);
	}

	@Transactional
	public void delete(Long id) {
		nowEntryMapper.delete(id);
		log.info("近况已删除：id={}", id);
	}

}
