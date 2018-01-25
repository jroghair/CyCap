package CyCapServer;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class HelloWorld {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWorld.class, args);
    }
  
    @RequestMapping("/")
    String home(){
    	return "Hello World!";    }

}




