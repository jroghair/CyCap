package CyCapServer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class button {
	
	public String id;
	
	@RequestMapping(value = "{button}")
	public String getId(){
		System.out.println("button");
		return "button";
	}
	
}
