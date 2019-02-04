
public class RankElement {
    private String playerName;
    private int playerIcon;
    private int playerScore;
    
    public RankElement(String name, int icon, int score) {
    	playerName = name;
    	playerIcon = icon;
    	playerScore = score;
    }
    
    public String getPlayerName() {
    	return playerName;
    }
    
    public int getPlayerIcon() {
    	return playerIcon;
    }
    
    public int getPlayerScore(){
    	return playerScore;
    }
    
    public void setPlayerName(String name){
    	playerName = name;
    }
    
    public void setPlayerIcon(int icon) {
    	playerIcon = icon;
    }
    
    public void setPlayerScore(int score){
    	playerScore = score;
    }
}
