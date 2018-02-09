package com.cycapservers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

	/**
	 *CyCap Spring Boot Application.
	 * 
	 * @author Jeremy Roghair
	 */
//@ComponentScan({ "com/cycapserver.system", "com.cycapserver.account", "com.cycapserver.app"})
@SpringBootApplication(scanBasePackages = { "com.cycapservers" })
@EnableJpaRepositories("com.cycapservers.accounts")
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class CyCapApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CyCapApplication.class, args);
    }
}

