package CyCapServer.CyCap;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class Message {

	String User;
	
	String Message;
	
	
	public Message(String User, String Message){
		this.User = User;
		this.Message = Message;
	}
	
	public String getUser(){
		return this.User;
	}
	
	public String Message(){
		return this.Message;
	}
}
