package com.theobfuscatorinator.insertcode;

import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;

/** 
 * This class will insert dummy code throughout a java code file.
 * It will add one dummy class and dummy print statements within methods.
 */

public class InsertCode {
	
    /**
     * getRandomString generates a random string of length 50 made up of letters and digits
     * @return A randomly generated string
     */
    
    public static String getRandomString() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder str = new StringBuilder();
        Random rnd = new Random();
        while (str.length() < 50) {
            int index = (int) (rnd.nextFloat() * alphabet.length());
            str.append(alphabet.charAt(index));
        }
        String randomString = str.toString();
        return randomString;
    }

    /** 
     * Gets a randomly generated string and formats it properly into a java print statement
     * @return String that represents a print statement with a random string
     */
    public static String generateDummyString() {
        String randomString = getRandomString();
        String finalString = String.format("System.out.println(\"%s\");", randomString);
        return finalString;
    }
    
    /**
     * Builds a properly-made dummy class that includes random java print statements and variable sets.
     * Adds 30 lines of dummy code
     * @return a String that represents an entire class
     */

    public static String generateDummyClass() {
    	String className = getRandomString();
    	String startClassCode = "public static class " + className + " {";
    	String dummyPrint = "";
    	String dummyLine = "\t\t\t" + "int dummyVar = 0;\n";
    	String tempLine = "";
    	int maxLines = 30;
    	for (int i = 1; i <= maxLines; i++) {
    		dummyPrint = String.format("System.out.println(\"%s\");", getRandomString());
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
    
    public static void modifyFile(File file, String code) {
        //Modify File
            // Path filePath = Paths.get("./src/test/res/individual-files/TestClassIdentifying.java");
            // FileChannel fileSize = FileChannel.open(filePath);
            // System.out.println(fileSize.size());
            FileWriter writer;
            try {
                writer = new FileWriter(file, false);
                writer.write(code);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // System.out.println("final");
            // System.out.println(fileSize.size());
    }

    /**
     * Injects code throughout an inputted java file and modifies the file
     * @param file Java file to have dummy code inserted to
     * @param codeStructure Code that represents the java code of the file
     */
    public static void insertStrings(File f, CodeStructure codeStructure) {
    	String code = codeStructure.getUnCommentedCode();

        //Inserting dummy strings throughout the code
        String[] codeByLines = code.split("\r\n");
        for (int i = 0; i < codeByLines.length; i++) {
            if (codeByLines[i].endsWith("}")){
                continue;
            }
            if (codeByLines[i].contains("public") && codeByLines[i].contains("{") && codeByLines[i].indexOf("public") != 0) {
                codeByLines[i] = codeByLines[i] + "\n" + " ".repeat(codeByLines[i].indexOf("public") + 4) + generateDummyString();
            }
            if (codeByLines[i].contains("private") && codeByLines[i].contains("{") && codeByLines[i].indexOf("private") != 0) {
                codeByLines[i] = codeByLines[i] + "\n" + " ".repeat(codeByLines[i].indexOf("private") + 4) + generateDummyString();
            }
        }

        //Converts the entire code file back into a string
        String newCode = new String();
        for (String line:codeByLines) {
            newCode += line + "\n";
        }
        // // Inserting Dummy Class
        // int index = newCode.indexOf("{\n");
        // String dummyCode = generateDummyClass();
        // newCode = newCode.substring(0, index + 1) + dummyCode + newCode.substring(index + 1);

        modifyFile(f, newCode);
    }

    public static int insertClass(File f, CodeStructure codeStructure) {
    	String code = codeStructure.getUnCommentedCode();
        String newCode = new String();
        int count = 0;

        // Inserting Dummy Class
         int index = code.indexOf("{\n");
         String dummyCode = generateDummyClass();
         newCode = code.substring(0, index + 1) + dummyCode + code.substring(index + 1);
 
        modifyFile(f, newCode);
        return count;
    }

}