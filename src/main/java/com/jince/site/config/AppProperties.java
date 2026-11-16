package com.jince.site.config;

import java.time.ZoneId;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 配置文件里 app 开头的设置。
 * 缺少必填项时，网站会在启动时直接报错，避免带着错误配置上线。
 */
@ConfigurationProperties(prefix = "app")
@Validated
public record AppProperties(@Valid @NotNull Site site, @Valid @NotNull Admin admin) {

	/**
	 * @param baseUrl  网站的正式地址（例：https://example.dev）
	 * @param timeZone 显示日期时使用的时区
	 */
	public record Site(@NotBlank String baseUrl, @NotNull ZoneId timeZone) {
	}

	/**
	 * 管理后台的登录账号。
	 */
	public record Admin(@NotBlank String username, @NotBlank String password) {
	}

}
