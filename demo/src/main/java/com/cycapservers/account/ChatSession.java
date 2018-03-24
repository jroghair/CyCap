package com.cycapservers.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChatSession {
	
	private ArrayList<Message> messages = new ArrayList<Message>();
	
	private ArrayList<WebSocketSession> s = new ArrayList<WebSocketSession>();
	
	//Maybe have a list of people in the session so a message can be sent to all subscribed.
	
	private String SessionID;
	
	
	public void SendMostRecentMessage() throws IOException{
		Message message = messages.get(messages.size() - 1);
		for(int i = 0; i < s.size(); i++){
			s.get(i).sendMessage(new TextMessage(message.User + "," + message.Message));
		}
	}
	
	public ChatSession(){
		this.SessionID = GenerateID(10);
		
	}
	
	public void addSession(WebSocketSession s){
		this.s.add(s);
	}
	
	public void addAndCreateMessage(String User, String Message){
		this.messages.add(new Message(User,Message));
	}
	
	public void addMessage(Message e){
		this.messages.add(e);
	}
	
	private String GenerateID(int length){
		String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*";
		String pass = "";
		Random rand = new Random();
		for(int i = 0; i < length; i++){
			pass += s.charAt(rand.nextInt(s.length()));	
		}
		return pass;
	}
	
	public String GetID(){
		return this.SessionID;
	}
	
	public Message getMessage(int index){
		return messages.get(index);
	}
	
	public int getMessageAmount(){
		return this.messages.size();
	}
	
}
