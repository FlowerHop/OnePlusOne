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
    private int changed = 0;
    
    private int score = 0;
    
    public Player (Socket s, String name, int icon) {
    	if (s != null && name != null && icon >= 0) {
    		socket = s;
    		userName = name;
    		this.icon = icon;
    		exit = false;
    		startSession();
    	}
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
    	readMsg.interrupt();
    	exit = true;
    }
    
    private Thread readMsg = new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				
				while (!exit) {
						
					String msg = br.readLine();
					String msgArray[] = msg.split(":");
					
					
					switch (Integer.parseInt(msgArray[0])) {
					    case EXIT:
					    	score = Integer.parseInt(msgArray[1]);
					    	stopSession();
					    	
					    	break;
					    case UPDATE:
					    	changed = Integer.parseInt(msgArray[1]);
					    	break;
					}
					
					

				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}
				
	}});
    
    private void startSession() {	
		readMsg.start();
    }
    
    public void stopSession() {
    	try {
			br.close();
			bw.close();
	    	socket.close();
	    	toExit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void sendMsg(String msg) {
    	try {
    		if (!socket.isClosed()) {
    			bw.write(msg + "\n");
    			bw.flush();
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void sendMsgAndQuit(String msg) {
    	try {
    		if (!socket.isClosed()) {
    			System.out.println("Rank msg: " + msg);
    			bw.write(msg + "\n");
    			bw.flush();
    		}
    		stopSession();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public int getChanged(){
    	int temp = changed;
    	changed = 0;
    	return temp;
    }
    
    public int getScore(){
    	return score;
    }
}
