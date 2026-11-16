package com.jince.site.dto;

import com.jince.site.entity.NowEntry;

/**
 * 页面显示用的近况：原始数据加上转换好的 HTML。
 */
public record NowView(NowEntry entry, String html) {
}
