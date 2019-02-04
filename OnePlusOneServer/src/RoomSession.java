


public class RoomSession {
	private final static int PLAYER_A = 0;
	private final static int PLAYER_B = 1;
	
    private Player[] players;
    private boolean exit;
    
    public RoomSession(Player a, Player b) {
    	players = new Player[2];
    	players[PLAYER_A] = a;
    	players[PLAYER_B] = b;
    	exit = false;
    	
    	
    }
    
    public void startSessions() {
    	
    	// TODO initial
    	
    	Thread t = new Thread(handlePlayersAnswers);
    	t.start();
    	initializeUI();
    }
    
    private Runnable handlePlayersAnswers = new Runnable() {
        
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Thread playerA = new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (!players[PLAYER_A].isExit()) {
						int changed = players[PLAYER_A].getChanged();
						if (changed != 0) updateUI(changed, 0);
					}
				}});
			
			Thread playerB = new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (!players[PLAYER_B].isExit()) {
						int changed = players[PLAYER_B].getChanged();
						if (changed != 0) updateUI(0, changed);
					}
				}});
			
			playerA.start();
			playerB.start();
			
			while(true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (players[PLAYER_A].isExit() && players[PLAYER_B].isExit()) {
					break;
				}
			}
			
			playerA.interrupt();
			playerB.interrupt();
			endGame();
		}
    	
    };
    
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
