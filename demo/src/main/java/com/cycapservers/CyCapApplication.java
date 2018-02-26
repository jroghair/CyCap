package com.cycapservers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cycapservers.account.Account;

	/**
	 *CyCap Spring Boot Application.
	 * 
	 * @author Jeremy Roghair
	 */
//@ComponentScan({ "com/cycapserver.system", "com.cycapserver.account", "com.cycapserver.app"})
@SpringBootApplication(scanBasePackages = { "com.cycapservers" })
//@SpringBootApplication(scanBasePackages = {"com.cycapservers"} , exclude = JpaRepositoriesAutoConfiguration.class) 
@EnableJpaRepositories({"com.cycapservers.account.AccountRepository","com.cycapservers.account"})
//@EnableJpaRepositories(basePackageClasses=com.cycapservers.account.AccountRepository.class)
@EntityScan(basePackageClasses=Account.class)
//@SpringBootApplication(scanBasePackages = {"boot.registration"} , exclude = JpaRepositoriesAutoConfiguration.class)
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class CyCapApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CyCapApplication.class, args);
    }
}
