/**
 * @author :  Dinuth Dheeraka
 * Created : 7/22/2023 2:09 PM
 */
package com.example.demo.constant;

import java.util.Base64;

public class App {

    public static void main(String[] args) {
        String myString = "USER:";
        String encodedString = Base64.getEncoder().encodeToString(myString.getBytes());
        System.out.println(encodedString);
    }
}
