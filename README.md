# OnePlusOne
開發於 Android 上的四則運算遊戲 APP，可以個人單機遊玩，或是雙人連線對戰。

## 遊戲方式
給定一段時間內，系統會自動產生四則運算式於遊戲畫面，挖空其中一個運算元或運算子，並在式子下方提供四個選項供玩家選擇，答對加分，答錯扣分，連續答題成功或失敗會有額外分數上的獎勵或懲罰。

## 雙人連線對戰機制
### Server (Java)
<ol>
  <li>Player A initConnection(), and writeMsg(Player A's info: name, icon) to Server  </li>
  <li>Server new Player(socket, Play A's info), and add to the player list</li>
  <li>Server check the player list -> if the size of list >= 2 then pair with 2 players into a RoomSession and remove from the player list</li>
  <li>Server new RoomSession(Player A, Player B) and add to the RoomSession list
  <li>RoomSession.startSesssion() which involkes initilizeUI() to use playerA.sendMsg([player B's name]:[player B's icon]) and playerB.sendMsg([player A's name]:[player A's icon])</li> 
  <li>RoomSession new thread to handle a runnable task called handlePlayersAnswers which create a new thread to listening on the score changing for each Player. If changing, called updateUI to use player.sendMsg([myScore]:[enemyScore]) to update the Android App's screen. At the same time, handlePlayersAnswers is listening on the game exit accident<li/>
  <li>In the battling time, the sockets of these two Player uses a thread called readMsg to receive the msg from the client(Player from the Android App). The format of msg is [operation]:[number]. Case 1: 0:70, 0 is code EXIT, and 70 is the final score; Case 2: 1:-3. 1 is code UPDATE, -3 is the changing of the score</li>
  <li>If handlePlayersAnswers get the exit accident, interrupt the two threads of listening on the score changings. Then, called endGame() to use Rank.insertElement(player's info and score)</li>
</ol>

### Client
<ol>
  <li>When user gets into PvPActivity, start waitingDialog to wait for the new enemy.</li>
  <li>Thread initConnection starts. It creates a Socket(IP, PORT) and send the [username]:[icon] to the server, and starts two thread, receiveMsg and loadingUI. The thread loadingUI invokes initGame() to assign a runnable obj which init the UI components to UI handler and starts new thread called timerCount to interrupt the watingDialog and listen on the game time.</li>
  <li>In the battling time, user can click the four options(buttons) below the question. It trigger the function checkAnswer which sends msg(changed score) to the server. And, thread receiveMsg is listening on the total scores info from the server.</li>
  <li>If time's up, timerCounter call endGame() to send msg 0:Score to the server.</li>
</ol>
