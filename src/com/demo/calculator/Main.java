package com.demo.calculator;

public class Main {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        double x = 100;
        double y = 20;
        double result = 0;

        result = calculator.add(x, y);
        System.out.println("Calculator app, add " + x + "," + y + " = " + result);

        result = calculator.subtract(x, y);
        System.out.println("Calculator app, subtract " + x + "," + y + " = " + result);

        result = calculator.divide(x, y);
        System.out.println("Calculator app, divide " + x + "," + y + " = " + result);

        result = calculator.multiply(x, y);
        System.out.println("Calculator app, multiply " + x + "," + y + " = " + result);
    }

    private void testM() {

        System.out.println("test");
        
    }

}
