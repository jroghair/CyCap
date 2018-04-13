package com.cycapservers;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**Application class that is the starting point of running the project. Springboot will search through the subpackages to find 
 * all the controllers, models and views and execute them appropriately when called. 
 * @author Jeremy Roghair
 * */
@SpringBootApplication
public class CyCapApplication {
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(CyCapApplication.class, args);
    }
}
