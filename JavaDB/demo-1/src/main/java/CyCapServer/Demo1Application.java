package CyCapServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.Model;

@SpringBootApplication
@ComponentScan
//@RestController
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@WebAppConfiguration 
public class Demo1Application extends SpringBootServletInitializer {

	
	
	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}
	
	/*
	@RequestMapping(value ="/", method = RequestMethod.GET)
	public String home(Model model){
		return "/button";
	}
	*/
	/*
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(Demo1Application.class);
	}
	*/
}
