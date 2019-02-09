# OnePlusOne
開發於 Android 上的四則運算遊戲 APP，可以個人單機遊玩，或是雙人連線對戰。

## 遊戲方式
給定一段時間內，系統會自動產生四則運算式於遊戲畫面，挖空其中一個運算元或運算子，並在式子下方提供四個選項供玩家選擇，答對加分，答錯扣分，連續答題成功或失敗會有額外分數上的獎勵或懲罰。

## 雙人連線對戰機制
### Server (Java)
<ol>
  <li>Player A initConnection(), and writeMsg(Player A's info: name, icon) to Server</li>
  <li>Server new Player(socket, Play A's info), and add to the player list</li>
  <li>Server check the player list -> if the size of list >= 2 then pair with 2 players into a RoomSession and remove from the player list</li>
  <li>Server new RoomSession() and add to the RoomSession list, and these 2 paired players regiter this RoomSession</li>
  <li>RoomSession.startSesssion() which involkes initilizeUI() to send msg [enemy's name]:[enemy's icon] to the client</li> 
  <li>RoomSession new thread called handleTwoPlayersMsg which invokes initializeUI() and listens on the score changing for each Player. If changing, called updateUI to send msg [myScore]:[enemyScore] to update the Android App's screen.</li>
  <li>If handleTwoPlayersMsg get the exit accident(Player A and Player B exit), call endGame() and use Rank.insertElement(player's info and score) to update the Rank.</li>
</ol>

### Client
<ol>
  <li>When user gets into PvPActivity, thread initConnection starts. It creates a Socket(IP, PORT) and send the [username]:[icon] to the server, and starts two thread, receiveMsg and timeCounter. The thread timeCounter dismiss the dialog and set button clickable after wating for 3 seconds then tracking the game time. Call endGame() to send msg 0:Score to the server if time's up.</li>
  <li>In the battling time, user can click the four options(buttons) below the question. It trigger the function checkAnswer which sends msg(changed score) to the server. And, thread receiveMsg is listening on the total scores info from the server.</li>
</ol>
