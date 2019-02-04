package com.example.caizongming.oneplusone;

import java.util.Random;

/**
 * Created by caizongming on 15/5/20.
 */


public class QuestionBuilder {
    public static final int POSITION_NUMBER_A = 0;
    public static final int POSITION_OPERATION = 1;
    public static final int POSITION_NUMBER_B = 2;
    public static final int POSITION_RESULT = 3;



    private static String answer;
    private static int emptyPosition;

    public static Equation build() {
        Equation equation = new Equation(randomNumbers (1, 10), randomNumbers (1, 10), randomNumbers(0,3));
        int randomPosition = randomNumbers(0, 3);
        emptyPosition = randomPosition;
        switch (randomPosition) {
            case POSITION_NUMBER_A:
                answer = equation.getNumberA();
                break;
            case POSITION_OPERATION:
                int operation = equation.getOperation();
                if (operation == Equation.OPERATION_ADD) {
                    answer = "+";
                } else if (operation == Equation.OPERATION_SUB) {
                    answer = "-";
                } else if (operation == Equation.OPERATION_MUL) {
                    answer = "x";
                }
                break;
            case POSITION_NUMBER_B:
                answer = equation.getNumberB();
                break;
            case POSITION_RESULT:
                answer = equation.getResult();
                break;

        }
        return equation;
    }

    public static int randomNumbers (int from, int to) {
        Random random = new Random();
        return random.nextInt(to - from) + from;
    }

    public static boolean toCheckAnswer(String a) {
        if (answer.equals(a)) {
            return true;
        }
        return false;
    }

    public static String getAnswer() {
        return answer;
    }

    public static int getEmptyPosition() {
        return emptyPosition;
    }

}
