package com.jince.site.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 登录和访问权限的设置。
 * 管理后台（/admin 开头）和公开页面的规则不同，所以分成两套。
 */
@Configuration
public class SecurityConfig {

	/**
	 * 管理后台：必须登录才能访问。
	 */
	@Bean
	@Order(1)
	SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatcher("/admin/**")
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/admin/login").permitAll()
				.anyRequest().authenticated())
			.formLogin(form -> form
				.loginPage("/admin/login")
				.loginProcessingUrl("/admin/login")
				.defaultSuccessUrl("/admin", true)
				.failureUrl("/admin/login?error"))
			.logout(logout -> logout
				.logoutUrl("/admin/logout")
				.logoutSuccessUrl("/"));
		return http.build();
	}

	/**
	 * 公开页面：所有人都能看。
	 * 不需要登录状态，所以不建立会话；也不加「禁止缓存」的响应头，方便 CDN 缓存页面。
	 */
	@Bean
	@Order(2)
	SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
			// 公开页面没有提交数据的表单，不需要防跨站请求伪造的令牌
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.requestCache(cache -> cache.disable())
			.headers(headers -> headers.cacheControl(cache -> cache.disable()));
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 管理员只有一个人，账号来自配置（线上来自环境变量）。
	 * 密码在内存里也只保存加密后的结果。
	 */
	@Bean
	UserDetailsService userDetailsService(AppProperties properties, PasswordEncoder passwordEncoder) {
		UserDetails admin = User.withUsername(properties.admin().username())
			.password(passwordEncoder.encode(properties.admin().password()))
			.roles("ADMIN")
			.build();
		return new InMemoryUserDetailsManager(admin);
	}

}
