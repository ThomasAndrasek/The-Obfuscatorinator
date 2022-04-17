package com.theobfuscatorinator.stringencryption;

import java.util.ArrayList;
import java.util.Random;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.codeInterpreter.Renamer;

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
                if (j < code.length() && j != i + 1) {
                    strings.add(new Pair<String, Integer>(code.substring(i + 1, j), i));
                    i = j + 1;
                }
            }
            i++;
        }

        return strings;
    }

    public static String encryptStrings(CodeStructure codeStructure, String decryptionMethodName) {
        
        ArrayList<Pair<String, Integer>> strings = findStrings(codeStructure);
        String code = codeStructure.getUnCommentedCode();

        Random random = new Random();
        
        for (int i = strings.size() - 1; i >= 0; i--) {
            String string = strings.get(i).first;
            int index = strings.get(i).second;

            byte[] byteArrray = string.getBytes();
            String byteString = "";
            for (byte b : byteArrray) {
                int num = b * 42 + 27000;

                int attached = random.nextInt(100) + 1;

                num *= attached;

                num *= 100;

                num += attached;

                byteString += num + ", ";
            }
            byteString = byteString.substring(0, byteString.length() - 2);
            String encrypted = decryptionMethodName + "(new int[]{" + byteString + "})";

            code = code.substring(0, index) + encrypted + code.substring(index + string.length() + 2);
        }

        

        return code;
    }

    public static void addDecryptionMethod(ArrayList<CodeStructure> codeStructures) {
        String param = Renamer.generateClassName();
        String decrypted = Renamer.generateClassName();
        String iVar = Renamer.generateClassName();
        String second = Renamer.generateClassName();
        String first = Renamer.generateClassName();
        String completed = Renamer.generateClassName();
        String decryptMethodBody = param + ") { " +
                "String " + decrypted + " = \"\"; " +
                "for (int " + iVar + " = 0; " + iVar + " < " + param + ".length; " + iVar + "++) { " +
                "int " + first + " = " + param + "[" + iVar + "] % 100; " +
                "int " + second + " = " + param + "[" + iVar + "] - " + first + ";" +
                "" + second + " /= 100; " +
                "" + second + " /= " + first + "; " +
                "int " + completed + " = " + second + " - 27000; " +
                "" + completed + " /= 42; " +
                "" + decrypted + " += (char) " + completed + "; " +
                "} " +
                "return " + decrypted + "; " +
                "}";


        for (CodeStructure codeStructure : codeStructures) {
            int end = codeStructure.getUnCommentedCode().lastIndexOf("}");
            String code = codeStructure.getUnCommentedCode();
            String decryptMethod = "public static String " + codeStructure.getDecryptionMethodName() + "(int[] " + decryptMethodBody;
            code = code.substring(0, end) + decryptMethod + code.substring(end);
            codeStructure.setUnCommentedCode(code);
        }

        for (CodeStructure struct : codeStructures) {
            System.out.println(struct.getUnCommentedCode() + "\n\n");
        }
    }

}
