package CyCapServer;

/*

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;


@RestController*/
/*This annotation tells Spring Boot to “guess” how you will want to configure Spring, based on the jar
 *  dependencies that you have added. Since spring-boot-starter-web added Tomcat and Spring MVC, the 
 *  auto-configuration will assume that you are developing a web application and setup Spring accordingly.
 * 
 * *//*
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) //necessary for database connection of NONE

public class HelloWorld {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWorld.class, args);

    }*/
   /*
    * The @RequestMapping annotation provides “routing” information. It is telling Spring that any HTTP 
    * request with the path “/” should be mapped to the home method. The @RestController annotation tells
    *  Spring to render the resulting string directly back to the caller
    * */
/*


    @RequestMapping("/")
    String home(){
    	return "Hello World!";    }

}

*/




