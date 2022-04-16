package com.theobfuscatorinator.insertcode;

import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import com.theobfuscatorinator.codeInterpreter.CodeStructure;

public class InsertCode {
	
    private File file;
    private String fileName;
    private String originalCode;

    public class Pair<T1, T2> {
        public T1 first;
        public T2 second;
    }

    public static String generateDummyClass() {
    	String className = "DummyCode";
    	String startClassCode = "public static class " + className + " {";
    	String dummyPrint = "";
    	String dummyLine = "\t\t\t" + "int dummyVar = 0;\n";
    	String tempLine = "";
    	int maxLines = 10;
    	for (int i = 1; i <= maxLines; i++) {
    		dummyPrint = "System.out.println(\"ij43otj8reiodfgj48gfsnlfkngldkfngrj4jt4\");";
    		dummyLine += "\t\t\t" + dummyPrint + "\n";
    		tempLine = String.format("dummyVar = %d;", i);
    		dummyLine += "\t\t\t" + tempLine + "\n";
    	}
    	
    	String allDummyCode = "\n\t" + startClassCode + "\n" + "\n";
    	allDummyCode += "\t\tpublic static void main(String[] args) {";
    	allDummyCode += "\n" + "\n" + dummyLine + "\n" + "\t" + "\t}";
    	allDummyCode += "\n" + "\t}" + "\n"; 
    	
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
        String finalString = String.format("\nSystem.out.println(\"%s\");\n", randomString);
        return finalString;

    }
    
    public static String insertCode(File file, CodeStructure codeStructure) {
    	String code = codeStructure.getUnCommentedCode();
        
        //Inserting dummy strings throughout the code
        int startIndex = 0;
        String newCode = code;
        while (code.indexOf('{', startIndex) != -1) {
            int tempIndex = newCode.indexOf('{', startIndex);
            String tempString = getRandomString();
            newCode = newCode.substring(0, tempIndex+1) + tempString + newCode.substring(tempIndex+1);
            startIndex = tempIndex + 1;        
        }
        System.out.println(newCode);
        // Inserting Dummy Class
        int index = code.indexOf("{\n");
        String dummyCode = generateDummyClass();
        newCode = code.substring(0, index + 1) + dummyCode + code.substring(index + 1);

        // //Modify File
        // FileWriter writer;
        // try {
        //     writer = new FileWriter(file, false);
        //     writer.write(newCode);
        //     writer.close();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    	return newCode;
    }
    
    
}