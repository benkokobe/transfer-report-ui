package com.bko.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;



@Configuration
@ImportResource("classpath:root-context.xml")
//@ComponentScan(basePackages = "com.bko.viewresolver")
//@ComponentScan({ "com.bko.service", "com.bko.persistence" })
//@ComponentScan({ "com.bko", "com.bko.config", "com.bko.viewresolver" })

public class MyBeansDefinition {
	

}
