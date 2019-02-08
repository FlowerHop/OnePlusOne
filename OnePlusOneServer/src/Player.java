import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class Player {
	public static final int EXIT = 0;
	public static final int UPDATE = 1;
	
    private Socket socket;
    private String userName;
    private int icon;
    private boolean exit;
    private BufferedWriter bw;
    private BufferedReader br;
    private RoomSession roomSession;
    private int score = 0;
    
    public Player (Socket s, String name, int icon) {
    	if (s != null && name != null && icon >= 0) {
    		socket = s;
    		userName = name;
    		this.icon = icon;
    		exit = false;
    	}
    }
    
    public void setRoomSession(RoomSession rs) throws IOException {
    	this.roomSession = rs;
    	this.roomSession.register(this);
    }
    
    public String getUserName() {
    	return userName;
    }
    
    public int getIcon() {
    	return icon;
    }
    
    public Socket getSocket() {
    	return socket;
    }
    
    public Boolean isExit() {
    	return exit;
    }
    
    public void toExit() {
    	try {
			socket.close();
			exit = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public int addScore(int s) {
    	score += s;
    	return score;
    }
    
    public int getScore(){
    	return score;
    }
}
