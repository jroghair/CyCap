package CyCapServer.CyCap;

public class TestGameState {

	private int playerNum = 2;
	
	private Player[] usernames = new Player[playerNum];
	
	
	public void updateUserInfo(String user, int x, int y){
		for(int i = 0; i < playerNum; i++){
			if(user.equals(this.usernames[i].getUsername())){
				this.usernames[i].setX(x);
				this.usernames[i].setY(y);
			}
		}
	}
	
	public String getState(){
		String returned = "";
		for(int i = 0; i < playerNum; i++){
			returned += usernames[i].getUsername() + ","  + usernames[i].getX() + "," + usernames[i].getY() + ":";
		}
		return returned;
	}
}
