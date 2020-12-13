package com.sekwah.mira4j.utils;

public class GameUtils {
    private static final int[] CHAR_MAP = { 25, 21, 19, 10, 8, 11, 12, 13, 22, 15, 16, 6, 24, 23, 18, 7, 0, 3, 9, 4, 14, 20, 1, 2, 5, 17 };
    private static final char[] CHAR_SET = "QWXRTYLPESDFGHUJKZOCVBINMA".toCharArray();
    
    public static String getStringFromGameId(long gameId) {
        if (gameId < -1) {
            // Version 2 codes will always be negative
            int firstTwo = (int) (gameId & 0x3FF);
            int lastFour = (int) ((gameId >> 10) & 0xFFFFF);
            
            return String.valueOf(new char[] {
                CHAR_SET[firstTwo % 26],
                CHAR_SET[firstTwo / 26],
                CHAR_SET[lastFour % 26],
                CHAR_SET[(lastFour /= 26) % 26],
                CHAR_SET[(lastFour /= 26) % 26],
                CHAR_SET[lastFour / 26 % 26]
            });
        } else {
            return String.valueOf(new char[] {
                (char)(gameId & 0xff),
                (char)((gameId >>>  8) & 0xff),
                (char)((gameId >>> 16) & 0xff),
                (char)((gameId >>> 24) & 0xff)
            });
        }
    }
    
    public static int getIntFromGameString(String code) {
        code = code.toUpperCase();
        if(code.length() == 4) {
            return ((code.charAt(0) & 0xff))
                 | ((code.charAt(1) & 0xff) <<  8)
                 | ((code.charAt(2) & 0xff) << 16)
                 | ((code.charAt(3) & 0xff) << 24);
        }
        
        if(code.length() != 6) {
            throw new IllegalArgumentException("Invalid game code string '" + code + "'");
        }
        
        int first = CHAR_MAP[(int)code.charAt(0) - 65];
        int second = CHAR_MAP[(int)code.charAt(1) - 65];
        int third = CHAR_MAP[(int)code.charAt(2) - 65];
        int fourth = CHAR_MAP[(int)code.charAt(3) - 65];
        int fifth = CHAR_MAP[(int)code.charAt(4) - 65];
        int sixth = CHAR_MAP[(int)code.charAt(5) - 65];
        
        int firstTwo = (first + 26 * second) & 0x3FF;
        int lastFour = (third + 26 * (fourth + 26 * (fifth + 26 * sixth)));
        
        return firstTwo | ((lastFour << 10) & 0x3FFFFC00) | 0x80000000;
    }
    
    public static String generateGameString(int value) {
        char[] chars = new char[6];
        for(int i = 0; i < 6; i++) {
            int letter = (value % CHAR_SET.length);
            
            chars[i] = CHAR_SET[letter];
            value /= CHAR_SET.length;
            if(value != 0) {
                value -= 1;
            }
        }
        
        return new String(chars);
    }
}
