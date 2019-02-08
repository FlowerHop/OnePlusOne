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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new GameFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
    public static class GameFragment extends Fragment {
        private final String TAG = "GameActivity";

        public final int GAME_INIT_TIME = 60;
        public final int GAME_INIT_SCORE = 0;
        public final int GAME_INIT_POLICY_TIME = 2; // it is used to calculate combo
        public final int GAME_LOADING_TIME = 3;
        public final double GAME_INIT_COMBO_P = 1;
        public final double GAME_SCORE_PER_QUESTION = 100;

        private ImageView img_user_icon;
        private TextView txt_user_name;


        private TextView[] img_question;
        private TextView[] selection;
        private TextView txt_score;
        private ProgressBar progressBar_timer;
        private Handler mHandler;
        private ProgressDialog.Builder dialogBuilder;
        private AlertDialog progressDialog;

        private int answerPosition;

        private int time;
        private int policyTime;
        private int score;
        private double comboP;

        public GameFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_game, container, false);

            mHandler = new Handler();
            //final ProgressDialog dialog = ProgressDialog.show(getActivity(), "等待", "Loading...", true);
            dialogBuilder = new ProgressDialog.Builder(getActivity());
            dialogBuilder.setTitle("等待");
            dialogBuilder.setMessage("Loading...");

            progressDialog = dialogBuilder.show();

            waitingDialog.start();

            img_user_icon = (ImageView) rootView.findViewById(R.id.img_user_icon);
            txt_user_name = (TextView) rootView.findViewById(R.id.txt_user_name);

            txt_score = (TextView) rootView.findViewById(R.id.txt_user_score);
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


            txt_user_name.setText(Pref.getUsername(getActivity()));
            img_user_icon.setImageDrawable(getResources().getDrawable(Pref.getIconResource(getActivity())));

            progressBar_timer = (ProgressBar) rootView.findViewById(R.id.progressBar_timer);
            progressBar_timer.setProgress(100);
            setListeners();
            for (TextView tx : selection) {
                tx.setClickable(false);
            }
            //initGame();
            loadingUI.start();
            return rootView;
        }



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
            score = GAME_INIT_SCORE;

            comboP = GAME_INIT_COMBO_P;
            // todo init info
            // set selection clickable

            // todo count for 3 seconds and start
            //timerCount.start();

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
            if (isRight) {
                if (policyTime > 0) {
                    comboP = comboP + 0.1;
                } else if (policyTime == 0) {
                    comboP = GAME_INIT_COMBO_P;
                    policyTimeCount.interrupt();
                    policyTimeCount = new Thread (policyTimeCountTask);
                    policyTimeCount.start();
                }
                score += GAME_SCORE_PER_QUESTION*comboP;
            } else {

            }

            System.out.println("Score: " + score);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    txt_score.setText(Integer.toString(score));
                }
            });
        }

        private Thread timerCount = new Thread(new Runnable(){
            @Override
            public void run() {
                try {

                    Thread.sleep(3000);
                    mHandler.post(new Runnable(){

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
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                progressBar_timer.setProgress((int) (time*100/GAME_INIT_TIME));
                            }
                        });
                        //System.out.println("Time: " + time);
                    }
                    //timerCount.stop();
                    endGame();



                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

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




        @Override
        public void onPause() {
            //time = 0;
            stopGame();
            getActivity().finish();
            super.onPause();
        }

        @Override
        public void onStop() {
            //time = 0;
            stopGame();
            getActivity().finish();
            super.onStop();
        }

        private void endGame () {


                for (TextView tx : selection) {
                    tx.setClickable(false);
                }
                // score
                mHandler.post(new Runnable(){

                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("結算").setMessage("你的分數為: " + score).setPositiveButton("OK", new DialogInterface.OnClickListener() {
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



        }

        private void stopGame() {
            policyTimeCount.interrupt();
            timerCount.interrupt();
            loadingUI.interrupt();
        }

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
                mHandler.post(new Runnable() {
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
