package com.jince.site;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import com.jince.site.config.AppProperties;
import com.jince.site.config.SecurityConfig;
import com.jince.site.service.MarkdownService;
import com.jince.site.support.DateFormatter;

/**
 * Controller 测试共用的设置。
 * 这类测试只启动 Web 相关的部分，所以把页面模板会用到的几个类手动加进来。
 */
@TestConfiguration(proxyBeanMethods = false)
@Import({ SecurityConfig.class, DateFormatter.class, MarkdownService.class })
@EnableConfigurationProperties(AppProperties.class)
public class WebTestConfig {
}
