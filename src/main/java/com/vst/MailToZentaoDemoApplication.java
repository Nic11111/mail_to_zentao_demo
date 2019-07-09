package com.vst;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages="com.vst.mapper")
@ComponentScan
@EnableScheduling
@SpringBootApplication
public class MailToZentaoDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailToZentaoDemoApplication.class, args);
	}

}
