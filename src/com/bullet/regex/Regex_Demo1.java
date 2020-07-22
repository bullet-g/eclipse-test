package com.bullet.regex;

import com.bullet.bean.Person;

public class Regex_Demo1 {

    @SuppressWarnings("unused")
    public static void main(String[] args) {


          demo1();

          Person p = new Person();

    }

    private static void demo1() {
        String regex = "\\d";
        System.out.print("3".matches(regex));
    }

}
