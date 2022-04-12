package com.theobfuscatorinator.insertcode;

import java.util.ArrayList;
import java.util.Random;


import com.theobfuscatorinator.codeInterpreter.CodeStructure;
//import com.theobfuscatorinator.stringencryption.StringEncryption.Pair;

public class InsertCode {
	
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
        System.out.println(strings);
        return strings;
    }
    
    public static String generateDummyClass() {
    	String className = "DummyCode";
    	String startClassCode = "public class" + className + " {";
    	String dummyPrint = "";
    	String dummyLine = "\t" + "\t" + "int dummyVar = 0";
    	String tempLine = "";
    	int maxLines = 100;
    	for (int i = 1; i <= maxLines; i++) {
    		dummyPrint = "System.out.println(\"ij43otj8reiodfgj48gfsnlfkngldkfngrj4jt4\");";
    		dummyLine += "\t" + "\t" + dummyPrint + "\n";
    		tempLine = String.format("dummyVar = %6d", i);
    		dummyLine += "\t" + "\t" + tempLine + "\n";
    	}
    	
    	String allDummyCode = startClassCode + "\n" + "\n";
    	allDummyCode += "public static void main(String[] args) {";
    	allDummyCode += "\n" + "\n" + dummyLine + "\n" + "\t" + "}";
    	allDummyCode += "\n" + "}" + "\n"; 
    	
    	return allDummyCode;
    }
    
    public static String getRandomString() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder str = new StringBuilder();
        Random rnd = new Random();
        while (str.length() < 25) {
            int index = (int) (rnd.nextFloat() * alphabet.length());
            str.append(alphabet.charAt(index));
        }
        String randomString = str.toString();
        String finalString = String.format("System.out.println(\"%s\"", randomString);
        return finalString;

    }
    
    public static String insertCode(CodeStructure codeStructure) {
    	ArrayList<Pair<String, Integer>> strings = findStrings(codeStructure);
    	String code = codeStructure.getUnCommentedCode();
    	return code;
    }
    
    
}