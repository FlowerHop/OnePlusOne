import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;


public class Server {
	private static ServerSocket serverSocket;
	private static final int PORT = 12345;
	
	private static ArrayList<RoomSession> rooms = new ArrayList<RoomSession>();
	private static ArrayList<Player> players = new ArrayList<Player>();
	private static boolean isRunning = false;
	
	
	
    public static void main(String[] args) {
    	try {
			serverSocket = new ServerSocket(PORT);
			waitForNewPlayers();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void waitForNewPlayers() {
    	
    	
    	Thread newPlayers = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//System.out.println("Start!");
				Thread exitPlayers = new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
                        while(true) {
                        	try {
								Thread.sleep(1000);
								if (players.size() != 0) {
	                        		for (Player p : players) {
	        							if (p.isExit()) {
	        								System.out.println("Remove " + p.getUserName());
	        								players.remove(p);
	        							}
	        						}
	                        	}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        	
                        	
                        }
						
					}
		    		
		    	});
				
				Thread endRooms = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						while(true) {
							
							try {
								Thread.sleep(1000);
								if (rooms.size() != 0) {
//							        for (RoomSession room : rooms) {
//									    if (room.isExit()) {
//									        rooms.remove(room);
//									        System.out.println("Remove room.");
//									    }
//									}
							        for (Iterator<RoomSession> it = rooms.iterator(); it.hasNext();) {
							        	
							        	if (it.next().isExit()) {
							        	    it.remove();
							        	    System.out.println("Remove room.");
							        	}
							        }
						    	}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						   
						}
					}});
				
				while (!serverSocket.isClosed()) {
					try {
						System.out.println("Wait.....");
						Socket socket = serverSocket.accept();
						System.out.println("Accept!");
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String playerInfo = br.readLine();
//						if (playerInfo != null) {}
						String[] playerInfoArray = playerInfo.split(":");
						System.out.println(playerInfoArray[0] + " is coming.");
						
						if (playerInfoArray[0].equals("[Rank]")) {
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
							bw.write(Rank.getString() + "\n");
							bw.flush();
							bw.close();
							socket.close();
						} else {
							Player player = new Player(socket, playerInfoArray[0], Integer.parseInt(playerInfoArray[1]));
							players.add(player);
							
							showInfo();
							
							if (!exitPlayers.isAlive()) {
								exitPlayers.start();
							}
							
							// if the number of people waiting for pvp > 2 then pair them random 
							if (players.size() >= 2) {
								Player a = players.remove(0);
								Player b = players.remove(0);
								RoomSession room = new RoomSession (a, b);
								Thread.sleep(3000);
								room.startSessions();
								rooms.add(room);
								showInfo();
								
								if(!endRooms.isAlive()) {
									endRooms.start();
								}
							}
						}
						
						
						
						
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}});
    	
    	newPlayers.start();
    }
    
    public static void showInfo() {
    	int number = players.size();
    	System.out.println("Players: ");
        System.out.println("    size: " + number);
    	
    	if (number != 0) {
    		for (Player p : players) {
    			System.out.println("          " + p.getUserName());
    		}
    	} 
    	
    	number = rooms.size();
    	System.out.println("Rooms size: " + number);
    	
    }
    
    
    
}
