package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.Random;



public class Unicoder {

    /**
     * Converts character to Unicode string.
     * 
     * @param c The character to convert.
     * @return The Unicode string.
     */
    public static String getUnicode(char c) {
        return String.format("\\u%04x", (int) c);
    }

    /**
     * Randomly swaps characters in code to Unicode.
     * <br/><br/>
     * 
     * Goes through each code structure's code and randomly swaps characters to Unicode. It swaps
     * them at a rate of 1/20 characters.
     * 
     * @param codeStructures Code structures to obfuscate and swap.
     */
    public static void swapForUnicode(ArrayList<CodeStructure> codeStructures, int percentToReplace) {
        String valid =
         "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789{}[]()<>.,;:+-*/%&|^!~?_";
        Random rand = new Random();
        
        for (CodeStructure struct : codeStructures) {
            String code = struct.getUnCommentedCode();
            int i = 0;
            while (i < code.length()) {
                int randInt = rand.nextInt(100);
                if (randInt < percentToReplace) {
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
