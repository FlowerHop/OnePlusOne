package com.example.caizongming.oneplusone;

/**
 * Created by caizongming on 15/5/20.
 */
public class Equation {


    public static final int OPERATION_ADD = 0;
    public static final int OPERATION_SUB = 1;
    public static final int OPERATION_MUL = 2;
    //private final int OPERATION_DIV = 3;


    private String numberA;
    private String numberB;
    private int operation;
    private String result;

    public Equation(int a, int b, int operation) {
        setNumberA(a);
        setNumberB(b);
        setOperation(operation);
        switch (operation) {
            case OPERATION_ADD:
                setResult(a + b);
                setOperation(operation);
                break;
            case OPERATION_SUB:
                setResult(a - b);
                setOperation(operation);
                break;
            case OPERATION_MUL:
                setResult(a*b);
                setOperation(operation);
                break;
            //case OPERATION_DIV:
            //    break;
        }
    }

    private void setNumberA(int a) {
        numberA = Integer.toString(a);
    }

    private void setNumberB(int b) {
        numberB = Integer.toString(b);
    }

    private void setResult(int result) {
        this.result = Integer.toString(result);
    }

    public String getNumberA() {
        return numberA;
    }

    public String getNumberB() {
        return numberB;
    }

    public String getResult() {
        return result;
    }

    public int getOperation() {return operation; };

    private void setOperation(int operation) {
        this.operation = operation;
    }

    public String toString() {
        String opStr = "";
        switch(operation) {
            case OPERATION_ADD:
                opStr = "+";
                break;
            case OPERATION_SUB:
                opStr = "-";
                break;
            case OPERATION_MUL:
                opStr = "x";
                break;
        }
        return numberA + opStr + numberB + "=" + result;
    }
}
