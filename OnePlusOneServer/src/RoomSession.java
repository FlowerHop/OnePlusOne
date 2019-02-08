import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RoomSession {
	public static final int EXIT = 0;
	public static final int UPDATE = 1;
	private final static int PLAYER_A = 0;
	private final static int PLAYER_B = 1;
	
    private Player[] players;
	private BufferedWriter[] bws;
	private BufferedReader[] brs;
    private boolean exit;
    
    private Thread handleTwoPlayersMsg = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			initializeUI();
			while(!players[PLAYER_A].isExit() && !players[PLAYER_B].isExit()) {
				for (int i = 0; i < players.length; i++) {
					if (!players[i].isExit()) {
						try {
							String msg = brs[i].readLine();
							String msgArray[] = msg.split(":");
							
							switch (Integer.parseInt(msgArray[0])) {
							    case EXIT:
									brs[i].close();
									bws[i].close();
							    	players[i].toExit();
							    	break;
							    case UPDATE:
							    	players[i].addScore(Integer.parseInt(msgArray[1]));
							    	updateUI();
							    	break;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
			}
			
			endGame();
		}});

    public RoomSession() {
    	players = new Player[2];
    	exit = false;
    }
    
    public void register(Player player) throws IOException {
    	for (int i = 0; i < players.length; i++) {
    		if (players[i] == null) {
    			players[i] = player;
    			bws[i] = new BufferedWriter(new OutputStreamWriter(players[i].getSocket().getOutputStream()));
        	    brs[i] = new BufferedReader(new InputStreamReader(players[i].getSocket().getInputStream()));
    		}
    	}
    }
    
    public void startSessions() {
    
    	// TODO initial
    	initializeUI();
    	handleTwoPlayersMsg.start();
    }

    public boolean isExit() {
    	return exit;
    }
    
    private void updateUI(){
    	sendMsgToPlayer(PLAYER_A, Integer.toString(players[PLAYER_A].getScore()) + ":" + Integer.toString(players[PLAYER_B].getScore()));
    	sendMsgToPlayer(PLAYER_B, Integer.toString(players[PLAYER_B].getScore()) + ":" + Integer.toString(players[PLAYER_A].getScore()));
    }
    
    private void initializeUI(){
    	sendMsgToPlayer(PLAYER_A, players[PLAYER_B].getUserName() + ":" + players[PLAYER_B].getIcon());
    	sendMsgToPlayer(PLAYER_B, players[PLAYER_A].getUserName() + ":" + players[PLAYER_A].getIcon());
    }
    
    private void sendMsgToPlayer(int playerID, String msg) {
    	try {
			bws[playerID].write(msg + "\n");
			bws[playerID].flush();
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void endGame() {
        Rank.insertElement(new RankElement(players[PLAYER_A].getUserName(), players[PLAYER_A].getIcon(), players[PLAYER_A].getScore()));
        Rank.insertElement(new RankElement(players[PLAYER_B].getUserName(), players[PLAYER_B].getIcon(), players[PLAYER_B].getScore()));
        
    	exit = true;
    }
}
