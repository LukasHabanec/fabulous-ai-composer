package com.habanec.composer2.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Utils {

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz";

    public static String cleanUpString(String string) {
        string = string.replaceAll("[^A-Za-z]","");
        return string;
    }

    public static String setHash(int numOfSymbols) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < numOfSymbols; i++) {
            randomString.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return randomString.toString();
    }

    public static Integer[] getZeroArray(int length) {
        Integer[] shifters = new Integer[length];
        Arrays.fill(shifters, 0);
        return shifters;
    }

    public static String arrayToString(Integer[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]).append(" ");
        }
        return sb.toString();
    }

    public static Integer[] getIntArrayOutOfString(String s) {
        String[] strings = s.split("\\s");
        Integer[] ints = new Integer[strings.length];
        for (int i = 0; i < strings.length; i++) {
            ints[i] = Integer.parseInt(strings[i]);
        }
        return ints;
    }

    public static Integer[] concatArrays(Integer[] halfpattern1, Integer[] halfpattern2) {
        List<Integer> list = Stream.of(halfpattern1).collect(Collectors.toList());
        list.addAll(Stream.of(halfpattern2).collect(Collectors.toList()));
        return list.stream().toArray(Integer[]::new);
    }

}
