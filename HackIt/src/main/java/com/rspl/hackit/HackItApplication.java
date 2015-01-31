package com.rspl.hackit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@ComponentScan
@EnableAutoConfiguration
public class HackItApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(HackItApplication.class, args);
	}

}
