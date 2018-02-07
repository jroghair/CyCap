package CyCapServer;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import com.google.gson.Gson;
import com.sun.javafx.collections.MappingChange.Map;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.EnableScheduling;

@Controller
@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
public class button {
	
	public String name;
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("/button")
	@SendToUser("/button")
	public String processMessageFromClient(@Payload String message)throws Exception{
		/*
		String name =  new Gson().fromJson(message, Map.class).get("name").toString();
		messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/reply", name);
		*/
		System.out.println(message);
		name = message + " hello";
		return name;
	}
	
	
	/*
	@RequestMapping(value = "/{button}", method = RequestMethod.GET)
	public String getId(){
		System.out.println("button");
		return "button";
	}
	/*
	@RequestMapping(value = "/{message}", method = RequestMethod.GET)
	public void getMessage(String message){
		System.out.println(message);
		message = "fuck";
		System.out.println("hello");
		return;
	}
	*/
}
