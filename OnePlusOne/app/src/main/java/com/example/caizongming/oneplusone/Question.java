package com.example.caizongming.oneplusone;

import java.util.Random;

/**
 * Created by caizongming on 15/5/20.
 */


public class Question { // to Question
    public static final int POSITION_NUMBER_A = 0;
    public static final int POSITION_OPERATION = 1;
    public static final int POSITION_NUMBER_B = 2;
    public static final int POSITION_RESULT = 3;
    public static final int OPTIONS_NUMBER = 4;


    private String answer;
    private Equation equation;
    private String[] options;
    private int answerPosition;
    private int emptyPosition;

    public Question(Equation eq, int ep) {
        equation = eq;
        emptyPosition = ep;
    }

    public static Question build() {
        Equation eq = new Equation(randomNumbers(1, 10), randomNumbers(1, 10), randomNumbers(0,3));
        int ep = randomNumbers(0, 3);
        String ans = "";

        switch (ep) {
            case POSITION_NUMBER_A:
                ans = eq.getNumberA();
                break;
            case POSITION_OPERATION:
                int operation = eq.getOperation();
                if (operation == Equation.OPERATION_ADD) {
                    ans = "+";
                } else if (operation == Equation.OPERATION_SUB) {
                    ans = "-";
                } else if (operation == Equation.OPERATION_MUL) {
                    ans = "x";
                }
                break;
            case POSITION_NUMBER_B:
                ans = eq.getNumberB();
                break;
            case POSITION_RESULT:
                ans = eq.getResult();
                break;
        }

        String ops[] = new String[OPTIONS_NUMBER];
        int ansPos = randomNumbers(0, 3);

        for (int i = 0; i < OPTIONS_NUMBER; i++) {
            if (i == ansPos) {
                ops[i] = ans;
            } else {
                do {
                    ops[i] = codeToOpers(randomNumbers(0, 13));
                } while (ops[i].equals(ans));
            }
        }

        Question question = new Question(eq, ep);
        question.setAnswer(ans);
        question.setEmptyPosition(ep);
        question.setOptions(ops);
        question.setAnswerPosition(ansPos);
        return question;
    } // return new Question();

    private static int randomNumbers (int from, int to) {
        Random random = new Random();
        return random.nextInt(to - from) + from;
    }

    private static String codeToOpers(int code) {
        String opStr = "";

        if (code < 10) {
            opStr = Integer.toString(code);
        } else {
            switch (code) {
                case 10:
                    opStr = "+";
                    break;
                case 11:
                    opStr = "-";
                    break;
                case 12:
                    opStr = "x";
                    break;
            }
        }
        return opStr;
    }
//    public static boolean toCheckAnswer(String a) {
//        if (answer.equals(a)) {
//            return true;
//        }
//        return false;
//    }

    public String getAnswer() {
        return answer;
    }
    public String[] getOptions() {
        return options;
    }

    public int getEmptyPosition() {
        return emptyPosition;
    }

    public int getAnswerPosition() {
        return answerPosition;
    }

    public void setAnswer(String a) {
        answer = a;
    }

    public void setOptions(String[] ops) {
        options = ops;
    }

    public void setEmptyPosition(int ep) {
        emptyPosition = ep;
    }

    public void setAnswerPosition(int ansPos) {
        answerPosition = ansPos;
    }

    public Equation getEquation() {
        return this.equation;
    }
}
