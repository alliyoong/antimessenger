package com.khanh.antimessenger.utilities;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class PasswordService {
    public static final char[] NUMBERS = "0123456789".toCharArray();
    public static final char[] LOWER_CASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static final char[] UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public static final char[] SPECIAL = "!@#$%*.-_?".toCharArray();
    public static final char[] ALL_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%*.-_?".toCharArray();
    public static String generate() {
        Random dice = new SecureRandom();
        int length = dice.nextInt(23) + 8;
        List<Character> charList = new ArrayList<>(length);
        // make sure password contain at least 1 required char
        charList.add(NUMBERS[dice.nextInt(NUMBERS.length)]);
        charList.add(LOWER_CASE[dice.nextInt(LOWER_CASE.length)]);
        charList.add(UPPER_CASE[dice.nextInt(UPPER_CASE.length)]);
        charList.add(SPECIAL[dice.nextInt(SPECIAL.length)]);
        // add random char to password
        while (length-4 > 0) {
            charList.add(ALL_CHAR[dice.nextInt(ALL_CHAR.length)]);
            length--;
        }
        // shuffle order of char
        Collections.shuffle(charList);
        return charList.stream().map(String::valueOf).collect(Collectors.joining());
    }
}
