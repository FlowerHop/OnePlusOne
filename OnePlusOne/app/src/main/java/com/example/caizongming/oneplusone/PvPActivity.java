package com.example.caizongming.oneplusone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


public class PvPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PvPFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pvp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PvPFragment extends Fragment {
        private final static String TAG = "PvPActivity";
        private final static int PORT = 12345;
//        private final static String IP = "140.115.205.68";
        private final static String IP = "101.14.207.129";
        private static String username;
        private static int icon;

        private Socket socket;
        private BufferedWriter bw;
        private BufferedReader br;

        private Handler UIHandler;

        public final int GAME_INIT_TIME = 60;
        public final int GAME_INIT_SCORE = 0;
        public final int GAME_INIT_POLICY_TIME = 2; // it is used to calculate combo
        public final int GAME_LOADING_TIME = 3;
        public final double GAME_INIT_COMBO_P = 1;
        public final double GAME_SCORE_PER_QUESTION = 100;



        private ImageView img_user_icon;
        private ImageView img_enemy_icon;
        private TextView txt_user_name;
        private TextView txt_enemy_name;


        private TextView[] img_question;
        private TextView[] selection;
        private TextView txt_user_score;
        private TextView txt_enemy_score;
        private ProgressDialog.Builder dialogBuilder;
        private AlertDialog progressDialog;

        private int answerPosition;

        private int time;
        private int policyTime;
        private int loadingTime;
        private double comboP;




        private String ScoreA = "0";
        private String ScoreB = "0";


        String enemyName;
        int enemyIcon;

        public PvPFragment() {
            // todo get username


        }

        @Override
        public void onPause() {

            if (socket != null) {
                sendMsg("0:" + ScoreA);
            }
            stopGame();
            getActivity().finish();

            super.onPause();


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // init views
            View rootView = inflater.inflate(R.layout.fragment_pvp, container, false);

            UIHandler = new Handler();

            dialogBuilder = new ProgressDialog.Builder(getActivity());
            dialogBuilder.setTitle("等待");
            dialogBuilder.setMessage("Loading...");

            progressDialog = dialogBuilder.show();
            // todo waitingDialog.start();
            waitingDialog.start();


            // find view
            img_user_icon = (ImageView) rootView.findViewById(R.id.img_user_icon);
            img_enemy_icon = (ImageView) rootView.findViewById(R.id.img_enemy_icon);
            txt_user_name = (TextView) rootView.findViewById(R.id.txt_user_name);
            txt_enemy_name = (TextView) rootView.findViewById(R.id.txt_enemy_name);

            txt_user_score = (TextView) rootView.findViewById(R.id.txt_user_score);
            txt_enemy_score = (TextView) rootView.findViewById(R.id.txt_enemy_score);

            img_question = new TextView[5];
            img_question[0] = (TextView) rootView.findViewById(R.id.img_question_a);
            img_question[1] = (TextView) rootView.findViewById(R.id.img_question_operator);
            img_question[2] = (TextView) rootView.findViewById(R.id.img_question_b);
            img_question[3] = (TextView) rootView.findViewById(R.id.img_question_equal);
            img_question[4] = (TextView) rootView.findViewById(R.id.img_question_result);

            selection = new TextView[4];
            selection[0] = (TextView) rootView.findViewById(R.id.selection_a);
            selection[1] = (TextView) rootView.findViewById(R.id.selection_b);
            selection[2] = (TextView) rootView.findViewById(R.id.selection_c);
            selection[3] = (TextView) rootView.findViewById(R.id.selection_d);



            // init user info
            username = Pref.getUsername(getActivity());
            icon = Pref.getIcon(getActivity());


            setListeners();
            for (TextView tx : selection) {
                tx.setClickable(false);
            }
            // check network
            initConnection.start();
            // todo if connection succeful


            return rootView;
        }



        private Thread initConnection = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Log.e("DEBUG", "Start Connection.");
                    socket = new Socket(IP, PORT);


                    bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    sendMsg(username + ":" + icon);

                    // init enemy info
                    String enemyInfo = br.readLine();
                    String enemyArray[] = enemyInfo.split(":");
                    enemyName = enemyArray[0];
                    enemyIcon = Integer.parseInt(enemyArray[1]);

                    receiveMsg.start();
                    loadingUI.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        private Thread receiveMsg = new Thread (new Runnable(){
            @Override
            public void run() {
                while (br != null) {
                    try {
                        String msg = br.readLine();
                        // todo wait for 3 seconds
                        if (msg == null) {

                        } else {
                            // todo update UI
                            // todo start

                            String scores[] = msg.split(":");
                            ScoreA = Integer.toString(Integer.parseInt(ScoreA) + Integer.parseInt(scores[0]));
                            ScoreB = Integer.toString(Integer.parseInt(ScoreB) + Integer.parseInt(scores[1]));

                            UIHandler.post(updateUI);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        private void sendMsg(String msg) {
            try {
                bw.write(msg + "\n");
                bw.flush();
                Log.e("send", "send");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Runnable updateUI = new Runnable() {
            @Override
            public void run() {
                txt_user_score.setText(ScoreA);
                txt_enemy_score.setText(ScoreB);
            }
        };

        public void setListeners() {
            for (int i = 0; i < 4; i++) {
                selection[i].setOnClickListener(selectListener);
            }
        }

        private TextView.OnClickListener selectListener = new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.selection_a:
                        checkAnswer(0);
                        break;
                    case R.id.selection_b:
                        checkAnswer(1);
                        break;
                    case R.id.selection_c:
                        checkAnswer(2);
                        break;
                    case R.id.selection_d:
                        checkAnswer(3);
                        break;
                }
            }
        };

        private void initGame() {
            time = GAME_INIT_TIME;
            ScoreA = Integer.toString(GAME_INIT_SCORE);
            ScoreB = Integer.toString(GAME_INIT_SCORE);
            comboP = GAME_INIT_COMBO_P;

            UIHandler.post(new Runnable() {
                @Override
                public void run() {
                    txt_user_name.setText(username);
                    img_user_icon.setImageDrawable(getResources().getDrawable(Pref.getIconResourceById(icon)));
                    txt_enemy_name.setText(enemyName);
                    img_enemy_icon.setImageDrawable(getResources().getDrawable(Pref.getIconResourceById(enemyIcon)));
                }
            });
            // set selection clickable


            initQuestion();
        }

        private void initQuestion() {
            Equation equation = QuestionBuilder.build();
            String answer  = QuestionBuilder.getAnswer();
            int emptyPosition = QuestionBuilder.getEmptyPosition();

            System.out.println("Equation: " + equation.toString());
            System.out.println("Answer: " + QuestionBuilder.getAnswer());

            img_question[0].setText(equation.getNumberA());
            img_question[1].setText( (equation.getOperation() == 0) ?  "+" :
                    (equation.getOperation() == 1) ? "-" : "x");
            img_question[2].setText(equation.getNumberB());
            img_question[3].setText("=");
            img_question[4].setText(equation.getResult());

            img_question[emptyPosition].setText("_");

            answerPosition = QuestionBuilder.randomNumbers(0, 3);

            ArrayList<String> tempSelection = new ArrayList<String>();
            for (int i = 0; i < 13; i++) {
                String s;
                if (i == 10) {
                    s = "+";
                } else if (i == 11) {
                    s = "-";
                } else if (i == 12) {
                    s = "x";
                } else {
                    s = Integer.toString(i);
                }

                if (!s.equals(answer)) {
                    tempSelection.add(s);
                }
            }

            for (int i = 0; i < 4; i++) {
                if (i == answerPosition) {
                    selection[i].setText(answer);
                } else {
                    int random = QuestionBuilder.randomNumbers(0, tempSelection.size() - 1);
                    selection[i].setText(tempSelection.remove(random));
                }
            }

        }


        private void checkAnswer (int s) {
            if (s == answerPosition) {
                // todo add score
                scorePolicy(true);
                initQuestion();
            } else {
                scorePolicy(false);
            }
        }

        private void scorePolicy (boolean isRight) {

            if (policyTime > 0) {
                comboP = comboP + 0.1;
            } else if (policyTime == 0) {
                comboP = GAME_INIT_COMBO_P;
                policyTimeCount.interrupt();
                policyTimeCount = new Thread (policyTimeCountTask);
                policyTimeCount.start();
            }
            int score = (int)Math.round(GAME_SCORE_PER_QUESTION*comboP);

            if (isRight) {
                sendMsg("1:" + score);
            } else {
                sendMsg("1:" + (score*-1));
            }



            // todo send msg to server
        }

        private Thread timerCount = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    Thread.sleep(3000);
                    UIHandler.post(new Runnable(){

                        @Override
                        public void run() {
                            for (TextView tx : selection) {
                                tx.setClickable(true);
                            }
                            waitingDialog.interrupt();

                        }
                    });
                    while (time > 0) {
                        Thread.sleep(1000);
                        time--;
                        //System.out.println("Time: " + time);
                    }
                    //timerCount.stop();
                    endGame();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        private void endGame () {


            for (TextView tx : selection) {
                tx.setClickable(false);
            }
            // score
            UIHandler.post(new Runnable(){

                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("結算").setMessage("你的分數為: " + ScoreA + "\n" + ((Integer.parseInt(ScoreA) >= Integer.parseInt(ScoreB) ? "You Win!" : "You Lose!" ))).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //getActivity().finish();
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            getActivity().finish();
                        }
                    });
                    builder.show();

                }
            });
            sendMsg("0:" + ScoreA);


        }

        private void stopGame() {
            policyTimeCount.interrupt();
            timerCount.interrupt();
            loadingUI.interrupt();
            initConnection.interrupt();
        }

        private Runnable policyTimeCountTask = new Runnable() {
            Boolean isRunning = true;
            @Override
            public void run() {
                try {
                    //while(isRunning) {
                    policyTime = GAME_INIT_POLICY_TIME;
                    Log.e(TAG, "Policy Time Start");
                    while (policyTime > 0) {
                        Thread.sleep(1000);
                        policyTime--;
                        Log.e(TAG, "Policy time: " + policyTime);
                    }
                    Log.e(TAG, "Policy Time End");
                    //}
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            public void stopTask() {
                isRunning = false;
            }
        };

        private Thread policyTimeCount = new Thread (policyTimeCountTask);



        private Thread waitingDialog = new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    while(true) {
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException e) {
                    progressDialog.dismiss();
                }
            }
        });

        private Thread loadingUI = new Thread(new Runnable(){
            @Override
            public void run() {
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initGame();
                        timerCount.start();

                    }
                });
            }
        });



    }
}
