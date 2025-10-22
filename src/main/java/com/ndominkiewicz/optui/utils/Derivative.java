package com.ndominkiewicz.optui.utils;

import com.ndominkiewicz.optui.models.EquationComponent;
import java.util.ArrayList;
import java.util.List;

class Symbol implements EquationComponent {
    private final char symbol;
    public Symbol(char symbol) {
        this.symbol = symbol;
    }
    public char getSymbol() {
        return symbol;
    }
    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
class Operation implements EquationComponent {
    private final char operator;
    public Operation(char operator) {
        this.operator = operator;
    }
    public char getOperator() {
        return operator;
    }
    @Override
    public String toString() {
        return String.valueOf(operator);
    }
}

class Coefficient implements EquationComponent {
    private final double value;
    public Coefficient(double value) {
        this.value = value;
    }
    public Coefficient(String number) {
        this.value = Double.parseDouble(number);
    }
    public double getValue() {
        return value;
    }
    public static String buildNumber(List<String> digits) {
        StringBuilder result = new StringBuilder();
        for(String digit : digits) {
            result.append(digit);
        }
        return result.toString();
    }

    @Override
    public String toString() {
        if (value == (int) value) {
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }
}

class Power implements EquationComponent {
    private final int exponent;
    public Power(int exponent) {
        this.exponent = exponent;
    }
    public int getExponent() {
        return exponent;
    }
    @Override
    public String toString() {
        return "^" + exponent;
    }
}

public class Derivative {
    private List<String> elements = new ArrayList<>();
    private List<EquationComponent> components = new ArrayList<>();
    public Derivative(String function) {
        function = function.replaceAll("\\s+", "");
        for(int i = 0; i < function.length(); i++) {
            elements.add(Character.toString(function.charAt(i)));
        }
        build();
    }
    private void build() {
        List<String> currentNumber = new ArrayList<>();
        for(int i = 0; i < elements.size(); i++) {
            String current = elements.get(i);
            char c = current.charAt(0);
            if (Character.isDigit(c) || c == '.') {
                currentNumber.add(current);
            } else {
                if (!currentNumber.isEmpty()) {
                    String numberStr = Coefficient.buildNumber(currentNumber);
                    components.add(new Coefficient(numberStr));
                    currentNumber.clear();
                }
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    components.add(new Operation(c));
                } else if (c == '^') {
                    if (i + 1 < elements.size()) {
                        String next = elements.get(i + 1);
                        if (Character.isDigit(next.charAt(0))) {
                            components.add(new Power(Integer.parseInt(next)));
                            i++;
                        }
                    }
                } else if (Character.isLetter(c)) {
                    components.add(new Symbol(c));
                }
            }
        }
        if (!currentNumber.isEmpty()) {
            String numberStr = Coefficient.buildNumber(currentNumber);
            components.add(new Coefficient(numberStr));
        }
    }
    public void writeOut() {
        System.out.println("Elementy:");
        for (String s : elements) {
            System.out.print(s + " ");
        }
        System.out.println("\n\nKomponenty:");
        for (EquationComponent component : components) {
            System.out.print(component + " ");
        }
        System.out.println();
    }
    public void writeDetailed() {
        for (EquationComponent component : components) {
            if (component instanceof Coefficient) {
                System.out.println("Współczynnik: " + component);
            } else if (component instanceof Symbol) {
                System.out.println("Symbol: " + component);
            } else if (component instanceof Operation) {
                System.out.println("Operacja: " + component);
            } else if (component instanceof Power) {
                System.out.println("Potęga: " + component);
            }
        }
    }
    public String getFunctionString() {
        StringBuilder sb = new StringBuilder();
        for (EquationComponent component : components) {
            sb.append(component);
        }
        return sb.toString();
    }
    public List<EquationComponent> getComponents() {
        return components;
    }
    public static void main(String[] args) {
        Derivative derivative = new Derivative("x^3-3x^2-20*x+1");
        System.out.println("\nOdtworzona funkcja: " + derivative.getFunctionString());
    }
}
