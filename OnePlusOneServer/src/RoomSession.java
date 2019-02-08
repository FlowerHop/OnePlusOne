


public class RoomSession {
	private final static int PLAYER_A = 0;
	private final static int PLAYER_B = 1;
	
    private Player[] players;
    private boolean exit;
    
    private Thread handlePlayersAnswers = new Thread(new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!players[PLAYER_A].isExit() && !players[PLAYER_B].isExit()) {
				if (!players[PLAYER_A].isExit()) {
					// bad smell
					int changed = players[PLAYER_A].getChanged();
					if (changed != 0) updateUI(changed, 0);
				}
				
				if (!players[PLAYER_B].isExit()) {
					int changed = players[PLAYER_B].getChanged();
					if (changed != 0) updateUI(0, changed);
				}
			}
			
			endGame();
		}});
	

    public RoomSession(Player a, Player b) {
    	players = new Player[2];
    	players[PLAYER_A] = a;
    	players[PLAYER_B] = b;
    	exit = false;
    }
    
    public void startSessions() {
    
    	// TODO initial
    	handlePlayersAnswers.start();
    	initializeUI();
    }

    public boolean isExit() {
    	return exit;
    }
    
    private void updateUI(int changedA, int changedB){
    	players[PLAYER_A].sendMsg(Integer.toString(changedA) + ":" + Integer.toString(changedB));
    	players[PLAYER_B].sendMsg(Integer.toString(changedB) + ":" + Integer.toString(changedA));
    }
    
    private void initializeUI(){
    	players[PLAYER_A].sendMsg(players[PLAYER_B].getUserName() + ":" + players[PLAYER_B].getIcon());
    	players[PLAYER_B].sendMsg(players[PLAYER_A].getUserName() + ":" + players[PLAYER_A].getIcon());
    }
    
    public void endGame() {
//    	players[PLAYER_A].sendMsg("exit");
//    	players[PLAYER_B].sendMsg("exit");
//    	players[PLAYER_A].toExit();
//    	players[PLAYER_B].toExit();
        Rank.insertElement(new RankElement(players[PLAYER_A].getUserName(), players[PLAYER_A].getIcon(), players[PLAYER_A].getScore()));
        Rank.insertElement(new RankElement(players[PLAYER_B].getUserName(), players[PLAYER_B].getIcon(), players[PLAYER_B].getScore()));
        
    	exit = true;
    }
}
