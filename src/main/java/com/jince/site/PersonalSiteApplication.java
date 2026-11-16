package com.jince.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * 网站的启动类。
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class PersonalSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalSiteApplication.class, args);
	}

}
