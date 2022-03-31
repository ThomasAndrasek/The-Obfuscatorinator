package com.theobfuscatorinator.stringencryption;

import java.util.ArrayList;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;

public class StringEncryption {

    /**
     * Templated custom pair class, because there does not seem to be a reasonable way to do this 
     * with this version of the JDK.
     * @param <K> Type of the first element in the pair
     * @param <V> Type of the second element in the pair
     */
    static class Pair<K,V>{
        public K first;
        public V second;

        public Pair(K fst, V snd) {
            first = fst;
            second = snd;
        }
    }

    private static ArrayList<Pair<String, Integer>> findStrings(CodeStructure codeStructure) {
        ArrayList<Pair<String, Integer>> strings = new ArrayList<Pair<String, Integer>>();
        String code = codeStructure.getUnCommentedCode();

        int i = 0;
        while (i < code.length()) {
            if (code.charAt(i) == '"') {
                int j = i + 1;
                while (j < code.length() && (code.charAt(j) != '"' || code.charAt(j) == '\\')) {
                    j++;
                }
                if (j < code.length()) {
                    strings.add(new Pair<String, Integer>(code.substring(i + 1, j), i));
                    i = j + 1;
                }
            }
            i++;
        }

        return strings;
    }

    public static String encryptStrings(CodeStructure codeStructure) {
        
        ArrayList<Pair<String, Integer>> strings = findStrings(codeStructure);
        String code = codeStructure.getUnCommentedCode();
        for (int i = strings.size() - 1; i >= 0; i--) {
            String string = strings.get(i).first;
            int index = strings.get(i).second;

            byte[] byteArrray = string.getBytes();
            String byteString = "";
            for (byte b : byteArrray) {
                int num = b * 42 - 27000;
                byteString += num + ", ";
            }
            byteString = byteString.substring(0, byteString.length() - 2);
            String encrypted = "werturtgfhbhxcvghwertyhs(new int[]{" + byteString + "})";

            code = code.substring(0, index) + encrypted + code.substring(index + string.length() + 2);
        }

        String decryptMethod = "public static String werturtgfhbhxcvghwertyhs(int[] zrtsafgsdfgds3453) { " +
                "String wertufghbdfghgwertyrityjkfafdgaduhys = \"\"; " +
                "for (int yuiiortyhfbnartydfghsdfgsd = 0; yuiiortyhfbnartydfghsdfgsd < zrtsafgsdfgds3453.length; yuiiortyhfbnartydfghsdfgsd++) { " +
                "int xcfvhktyertgsdfgsdffgsd = zrtsafgsdfgds3453[yuiiortyhfbnartydfghsdfgsd] + 27000; " +
                "xcfvhktyertgsdfgsdffgsd /= 42; " +
                "wertufghbdfghgwertyrityjkfafdgaduhys += (char) xcfvhktyertgsdfgsdffgsd; " +
                "} " +
                "return wertufghbdfghgwertyrityjkfafdgaduhys; " +
                "}";

        int end = code.lastIndexOf("}");

        return code.substring(0, end) + "\n" + decryptMethod + "\n" + code.substring(end);
    }

}
