package com.theobfuscatorinator.insertcode;

import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import com.theobfuscatorinator.codeInterpreter.CodeStructure;

public class InsertCode {
	
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
        while (str.length() < 50) {
            int index = (int) (rnd.nextFloat() * alphabet.length());
            str.append(alphabet.charAt(index));
        }
        String randomString = str.toString();
        String finalString = String.format("System.out.println(\"%s\");", randomString);
        return finalString;

    }
    
    public static String insertCode(File file, CodeStructure codeStructure) {
    	String code = codeStructure.getUnCommentedCode();

        //Inserting dummy strings throughout the code
        String[] codeByLines = code.split("\r\n");
        for (int i = 0; i < codeByLines.length; i++) {
            if (codeByLines[i].endsWith("}")){
                continue;
            }
            if (codeByLines[i].contains("public") && codeByLines[i].contains("{") && codeByLines[i].indexOf("public") != 0) {
                codeByLines[i] = codeByLines[i] + "\n" + " ".repeat(codeByLines[i].indexOf("public") + 4) + getRandomString();
            }
        }
        String newCode = new String();
        for (String line:codeByLines) {
            newCode += line + "\n";
        }
        // Inserting Dummy Class
        int index = newCode.indexOf("{\n");
        String dummyCode = generateDummyClass();
        newCode = newCode.substring(0, index + 1) + dummyCode + newCode.substring(index + 1);

        //Modify File
        FileWriter writer;
        try {
            writer = new FileWriter(file, false);
            writer.write(newCode);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	return newCode;
    }
    
    
}