package com.jince.site.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jince.site.entity.JourneyEntry;
import com.jince.site.exception.NotFoundException;
import com.jince.site.form.JourneyEntryForm;
import com.jince.site.mapper.JourneyEntryMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 历程的业务逻辑。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JourneyService {

	private final JourneyEntryMapper journeyEntryMapper;

	public List<JourneyEntry> findAll() {
		return journeyEntryMapper.findAll();
	}

	public List<JourneyEntry> findRecent(int limit) {
		return journeyEntryMapper.findRecent(limit);
	}

	/**
	 * 全部历程按年份分组，新的年份在前。
	 */
	public Map<Integer, List<JourneyEntry>> findAllGroupedByYear() {
		Map<Integer, List<JourneyEntry>> groups = new LinkedHashMap<>();
		for (JourneyEntry entry : journeyEntryMapper.findAll()) {
			groups.computeIfAbsent(entry.getYear(), year -> new ArrayList<>()).add(entry);
		}
		return groups;
	}

	public JourneyEntry getById(Long id) {
		JourneyEntry entry = journeyEntryMapper.findById(id);
		if (entry == null) {
			throw new NotFoundException("没有这条历程：id=" + id);
		}
		return entry;
	}

	@Transactional
	public JourneyEntry create(JourneyEntryForm form) {
		JourneyEntry entry = new JourneyEntry();
		form.applyTo(entry);
		journeyEntryMapper.insert(entry);
		log.info("历程已新建：id={}", entry.getId());
		return entry;
	}

	@Transactional
	public void update(Long id, JourneyEntryForm form) {
		JourneyEntry entry = getById(id);
		form.applyTo(entry);
		journeyEntryMapper.update(entry);
		log.info("历程已更新：id={}", id);
	}

	@Transactional
	public void delete(Long id) {
		journeyEntryMapper.delete(id);
		log.info("历程已删除：id={}", id);
	}

}
