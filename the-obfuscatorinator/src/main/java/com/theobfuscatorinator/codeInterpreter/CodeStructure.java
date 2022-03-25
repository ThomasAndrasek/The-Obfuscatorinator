package com.theobfuscatorinator.codeInterpreter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * This class will take a java code file and will find important characteristics in that file which can be
 * accessed by a user of this class.
 *
 * @author Carter Del Ciello
 */

public class CodeStructure {

    private File codeFile;
    private String fileName;
    private String originalCode;
    private String unCommentedCode;

    private ArrayList<String> classes;

    /**
     * Constructor will build all of the structure for you
     * @param input File to be searched for code elements
     * @throws IllegalArgumentException thrown if a directory is given instead of a regular file
     * @throws IOException thrown from Files.readAllBytes
     */
    public CodeStructure(File input) throws IllegalArgumentException, IOException {
        if(!input.isDirectory()){
            codeFile = input;
            fileName = codeFile.getName();
            originalCode = new String(Files.readAllBytes(input.toPath()));
            unCommentedCode = removeComments(originalCode);

            classes = identifyClasses(unCommentedCode);
        }
        else throw new IllegalArgumentException("Cannot make a code structure out of a directory.");
    }

    /**
     * Takes some java code as a string and removes the comments from it. Does not modify the original code.
     * @param code Code to have the comments removed from
     * @return Copy of code without any comments
     */
    private String removeComments(String code){
        String copy = code.trim();
        String output = "";
        String[] remainingCode = copy.split("//.*\\n?|(/\\*[\\S\\s]*\\*/)");

        for(String codeBlock : remainingCode){
            output += codeBlock;
        }

        return output;
    }

    /**
     * Takes some java code as a string and identifies all of the string literals that are in the
     * code. Remove all of the string literals from the code and returns the code without the
     * string literals.
     * 
     * @param code Code to have the string literals removed from
     * @return Copy of code without any string literals
     */
    private String removeStrings(String code) {
        String copy = code.substring(0);
        String output = "";
        
        int j = 0;
        // Loop through the code and find all of the string literals.
        // Only keep the code that is not a string literal.
        while (j < copy.length()) {
            // If the current character is a double quote, then we are in a string literal.
            if (copy.charAt(j) == '"') {
                j++;

                boolean foundEnd = false;

                // Loop until we find the end of the string literal.
                while (!foundEnd) {
                    // If we find a double quote, then we have found the end of the string literal.
                    // Only if we find a double quote that is not escaped by a backslash.
                    if (copy.charAt(j) == '"') {
                        if (copy.charAt(j-1) != '\\') {
                            foundEnd = true;
                        }
                    } else {
                        j++;
                    }
                }
                
                j++;
            }
            // If the current character is not a double quote, then we are not in a string literal.
            else {
                output += copy.charAt(j);
                j++;
            }
        }

        return output;
    }

    /**
     * Takes some java code as a string and identifies all of the class names that are in the
     * code.
     * 
     * @param code Code to have the class names identified.
     * @return ArrayList of all of the class names in the code.
     */
    private ArrayList<String> identifyClasses(String code) {
        ArrayList<String> classes = new ArrayList<>();
        String removedStringsCode = removeStrings(code);
        System.out.println(removedStringsCode);
        String[] codeBlocks = removedStringsCode.split("\\s+class\\s+");

        // Loop through the code blocks and find all of the class names.
        for(String codeBlock : codeBlocks){
            if(codeBlock.contains("{")){
                String classString = codeBlock.substring(0, codeBlock.indexOf("{"));
                classString = classString.trim();
                classes.add(classString);
            }
        }

        return classes;
    }

    public String getUnCommentedCode(){
        return unCommentedCode;
    }

    public String getCodeFileName(){
        return fileName;
    }

    public ArrayList<String> getClasses(){
        return classes;
    }
}
