import java.util.ArrayList;
import java.util.Iterator;



public class Rank {
	public final static int RANKNUMBER = 10;
	
    private static ArrayList<RankElement> rank = new ArrayList<RankElement>();
    
    public static void insertElement(RankElement element) {
    	if (rank.size() == 0){
    		rank.add(element);
    	} else {
    		for (int i = 0; i < rank.size(); i++) {
        		if (element.getPlayerName().equals(rank.get(i).getPlayerName())){
        			if (element.getPlayerScore() > rank.get(i).getPlayerScore()) {
        				rank.get(i).setPlayerScore(element.getPlayerScore());
        				rank.get(i).setPlayerIcon(element.getPlayerIcon());
        			}
        			break;
        		} else if (i == rank.size() - 1) {
        			rank.add(element);
        			reorder();
        			break;
        		}
        	}
        	
        	
    	}
    	
    	
    }
    
    private static void reorder() {
    	ArrayList<RankElement> newRank = new ArrayList<RankElement>();
    	
    	//System.out.println(rank.size());
    	
    	for (int i = 0; i < rank.size(); i++) {
    		RankElement og = rank.get(i);
    		if (i == 0) {
    			newRank.add(og);
    		} else {
    			
    			for (int j = 0; j < newRank.size(); j++) {
        			RankElement compare = newRank.get(j);
        			if (og.getPlayerScore() >= compare.getPlayerScore()) {
        				newRank.add(j, og);
        				break;
        			} else if (j == newRank.size() - 1) {
        				newRank.add(j + 1, og);
        				break;
        			} 
        		}
    			
    		}
    		
    	}
    	
    	rank = newRank;
    	if (rank.size() == 11) {
    		rank.remove(10);
    	}
    }
    
    public static String getString() {
    	
    	String result = "";
    	
    	Iterator<RankElement> iterator = rank.iterator();
    	while (iterator.hasNext()) {
    		RankElement element = iterator.next();
    		if (iterator.hasNext()) {
    			result += element.getPlayerName() + ":" + element.getPlayerIcon() + ":" + element.getPlayerScore() + ",";
    		} else {
    			result += element.getPlayerName() + ":" + element.getPlayerIcon() + ":" + element.getPlayerScore();
    		}
    		
    	}
    	
    	
    	
    	return result;
    }
}
