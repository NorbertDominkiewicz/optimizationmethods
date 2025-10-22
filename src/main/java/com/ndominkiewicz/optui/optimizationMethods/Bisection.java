package com.ndominkiewicz.optui.optimizationMethods;

import java.util.function.Function;
import com.ndominkiewicz.optui.models.Algorithm;
import com.ndominkiewicz.optui.utils.MINMAX;
import com.ndominkiewicz.optui.utils.Result;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

class QuadraticEquation {

}

public class Bisection implements Algorithm, Runnable {
    public double a, b;
    public double L;
    public double x1, x2;
    public double xsr;
    public double e;
    public double iterations;
    public MINMAX mode;
    public Function<Double, Double> function;
    boolean running;
    Thread algorithmThread;
    public Bisection(double a, double b, double e, String polynomialEquation) {
        this.a = a;
        this.b = b;
        this.mode = evaluateMode();
        this.e = e;
        this.function = x -> {
            Expression expression = new ExpressionBuilder(polynomialEquation)
                    .variable("x").build().setVariable("x", x);
            return expression.evaluate();
        };
    }
    public void startInThread() {
        if(mode != null) {
            algorithmThread = new Thread(this);
            algorithmThread.start();
        }
    }
    @Override
    public void run() {
        running = true;
        iterations = 1;
        step1();
        step2();
    }
    private void step1() {
        xsr = (a + b) / 2;
    }

    private void step2() {
        L = b - a;
        x1 = a + (L / 4);
        x2 = b - (L / 4);
        step3();
    }
    private void step3() {
        switch (mode) {
            case MINIMUM -> {
                if(function.apply(x1) < function.apply(xsr)) {
                    b = xsr;
                    xsr = x1;
                    step5();
                } else {
                    step4();
                }
            }
            case MAXIMUM -> {
                if(function.apply(x1) > function.apply(xsr)) {
                    b = xsr;
                    xsr = x1;
                    step5();
                } else {
                    step4();
                }
            }
        }
    }
    private void step4() {
        switch (mode) {
            case MINIMUM -> {
                if(function.apply(x2) < function.apply(xsr)) {
                    a = xsr;
                    xsr = x2;
                    step5();
                } else {
                    a = x1;
                    b = x2;
                    step5();
                }
            }
            case MAXIMUM -> {
                if(function.apply(x2) > function.apply(xsr)) {
                    a = xsr;
                    xsr = x2;
                    step5();
                } else {
                    a = x1;
                    b = x2;
                    step5();
                }
            }
        }
    }
    private void step5() {
        if (L <= e) {
            Result.writeOut(this);
            running = false;
        } else {
            iterations++;
            step2();
        }
    }
    public void stop() {
        running = false;
        if (algorithmThread != null) {
            algorithmThread.interrupt();
        }
    }
    private MINMAX evaluateMode() {
        return MINMAX.MAXIMUM;
    }
}