package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.Random;



public class Unicoder {

    public static String getUnicode(char c) {
        return String.format("\\u%04x", (int) c);
    }

    public static void swapForUnicode(ArrayList<CodeStructure> codeStructures) {
        String valid = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789{}[]()<>.,;:+-*/%&|^!~?_";

        Random rand = new Random();
        for (CodeStructure struct : codeStructures) {
            String code = struct.getUnCommentedCode();
            int i = 0;
            while (i < code.length()) {
                int randInt = rand.nextInt(20);
                if (randInt == 0) {
                    char c = code.charAt(i);
                    if (valid.indexOf(c) == -1) {
                        i++;
                        continue;
                    }
                    String unicode = getUnicode(c);
                    code = code.substring(0, i) + unicode + code.substring(i + 1);
                    i += unicode.length();
                }
                i++;
            }
            struct.setUnCommentedCode(code);
        }
    }
}
