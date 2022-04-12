package com.theobfuscatorinator.insertcode;

import java.util.ArrayList;

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
    
    public static String generateCode() {
    	String className = "DummyCode";
    	String startClassCode = "public class" + className + " {";
    }
    
    public static String insertCode(CodeStructure codeStructure) {
    	ArrayList<Pair<String, Integer>> strings = findStrings(codeStructure);
    	String code = codeStructure.getUnCommentedCode();
    	return code;
    }
    
    
}