package com.market.common.utils;

import java.util.Random;

/**
 * @author ph
 * @version 1.0
 * @date 2023-04-18 10:04
 */
public class CommonUtils {

    private static final String BASIC = "123456789QWERTYUIOPASDFGHJKLZXCVBNM";

    public static String getRandom(int size){
        char[] basicArray = BASIC.toCharArray();
        Random random = new Random();
        char[] result = new char[size];
        for (int i = 0; i < result.length; i++) {
            int index = random.nextInt(100) % (basicArray.length);
            result[i] = basicArray[index];
        }
        return String.valueOf(result);
    }

    public static void main(String[] args) {
        System.out.println(getRandom(64).toLowerCase());
    }
}
