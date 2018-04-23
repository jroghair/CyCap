package com.cycapservers.account;

/**ChatMessage class for chat events such as messages, user join, user leave.
 * @author Jeremy 
 */
public class ChatMessage {
	/**Describes the type of message being sent either CHAT, JOIN, or LEAVE
	 * */
    private MessageType type;
    /**Holds the contents of the message
     * */
    private String content;
    /**Defines the sender of the message
     * */
    private String sender;

    /**Defines the message type
     * */
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    /**Returns the type of the message
     * @return MessageType
     * */
    public MessageType getType() {
        return type;
    }
    
    /**Setter method for a specific message type
     * @param type message type being set 
     * @return void
     * */
    public void setType(MessageType type) {
        this.type = type;
    }

    /**Returns the content of the message
     * @return contents
     * */
    public String getContent() {
        return content;
    }

    /**Sets the content of the message
     * @return void
     * */
    public void setContent(String content) {
        this.content = content;
    }
    /**Gets the sender of the message
     * @return sender 
     * */
    public String getSender() {
        return sender;
    }

    /**Returns the sender of the messages
     * @param sender 
     * @return void
     * */
    public void setSender(String sender) {
        this.sender = sender;
    }
}