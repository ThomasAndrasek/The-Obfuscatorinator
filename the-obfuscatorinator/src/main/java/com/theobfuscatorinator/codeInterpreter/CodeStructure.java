package com.theobfuscatorinator.codeInterpreter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.theobfuscatorinator.stringencryption.StringEncryption;

/**
 * This class will take a java code file and will find important characteristics in that file which
 * can be accessed by a user of this class.
 *
 * @author Carter Del Ciello
 */

public class CodeStructure {

    private File codeFile;
    private String fileName;
    private String originalCode;
    private String unCommentedCode;

    private ArrayList<ClassStructure> classes;

    private String decryptionMethodName;

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

            classes = identifyClasses(originalCode);

            this.decryptionMethodName = Renamer.generateClassName();
            
            unCommentedCode = StringEncryption.encryptStrings(this, this.decryptionMethodName);

            unCommentedCode = removeComments(unCommentedCode);
        }
        else {
            throw new IllegalArgumentException("Cannot make a code structure out of a directory.");
        } 
    }

    /**
     * Get the original code.
     * 
     * @return The original code.
     */
    public String getOriginalCode() {
        return originalCode;
    }

    /**
     * Takes some java code as a string and removes the comments from it. Does not modify the
     * original code.
     * @param code Code to have the comments removed from
     * @return Copy of code without any comments
     */
    private String removeComments(String code){
        String copy = code.trim();
        String output = "";
        String[] remainingCode = copy.split("\\/\\*[\\s\\S]*?\\*\\/|\\/\\/[\\s\\S]*?[\\n]{1}");

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
        String output = copy.substring(0);
        
        int j = copy.length() - 1;
        // Loop through the code and find all of the string literals.
        // Only keep the code that is not a string literal.
        while (j >= 0) {
            // If the current character is a double quote, then we are in a string literal.
            if (copy.charAt(j) == '"') {
                int i = j;
                i--;

                boolean foundEnd = false;

                // Loop until we find the end of the string literal.
                while (!foundEnd && i >= 0) {
                    // If we find a double quote, then we have found the end of the string literal.
                    // Only if we find a double quote that is not escaped by a backslash.
                    if (copy.charAt(i) == '"') {
                        if (i - 1 >= 0 && copy.charAt(i - 1) == '\\') {
                            i--;
                        }
                        else {
                            foundEnd = true;
                        }
                    }
                    else {
                        i--;
                    }
                }
                // Remove the string literal from the code.
                output = output.substring(0, i) + output.substring(j + 1);

                j = i;
                j--;
            }
            // If the current character is not a double quote, then we are not in a string literal.
            else {
                j--;
            }
        }

        return output;
    }
    
    /**
     * Takes some java code as a string and removes all the spaces that need to be removed
     * 
     * @order MUST take place after comments and string literals are removed
     * @param code Code to have the spaces removed from
     * @return Copy of code without any extra spaces and newlines
     */
    public static String removeSpaces(String code) {
        String copy = code.substring(0).replace("\n", "");
        String output = "";
        
        int j = 0;
        Boolean equ = false;
        // Loop through the code and delete any space that isn't required for the code to run
        while (j < copy.length()) {
            //checks if there is an equals sign and ignores the next space based on that
            if (copy.charAt(j) == '=') {
                equ = true;
            }
            // If a space is found check if the next character is another word if it's not remove
            // the space
            if (copy.charAt(j) == ' ') {
            	j++;
                if (equ == true){
                    equ = false;
                    continue;
                }
                //copies the next character into the new string
            	if (Character.isLetter(copy.charAt(j))) {
            		j--;
            		output += copy.charAt(j);
            		j++;
                }
            //copies the character into the new string
            }else {
            	output += copy.charAt(j);
                j++;
            }
            
        }

        return output;
    }
    

    /**
     * Takes some java code as a string and identifies all of the classes that are in the
     * code.
     * 
     * @param code Code to have the classes identified.
     * @return ArrayList of all of the classes in the code as ClassStructure objects
     */
    private ArrayList<ClassStructure> identifyClasses(String code) {
        ArrayList<ClassStructure> classes = new ArrayList<>();
        String removedStringsCode = removeStrings(code);
        removedStringsCode = removeComments(removedStringsCode);
        Pattern classFinder = Pattern.compile("\\s+class\\s+");
        Matcher classMatcher = classFinder.matcher(removedStringsCode);
        int index = 0;
        while(classMatcher.find(index)){
            int classStart = classMatcher.end();
            int i = 0;
            while (i < removedStringsCode.length() && removedStringsCode.charAt(i) != '{') {
                i++;
            }
            String className = removedStringsCode.substring(classStart, i);
                                                 
            String full = className.substring(0);
            
            ArrayList<String> templates = new ArrayList<String>();
            //Handle template arguments
            if(className.contains("<")){
                className = removedStringsCode.substring(classStart, removedStringsCode
                        .indexOf("<", classStart)).trim();
                Pair<String, Integer> templateContents =
                     getCodeBetweenBrackets(removedStringsCode, classStart, '<', '>');
                String arguments = templateContents.first;
                templates = getCommaSeparatedValues(arguments);
            }
            Pair<String, Integer> currentClass = getCodeBetweenBrackets(removedStringsCode,
                                                                        classStart, '{', '}');

            int classEnd = className.indexOf(" ");
            
            if (classEnd == -1) {
                classEnd = className.length();
            }
            className = className.substring(0, classEnd);
            className = className.replaceAll("\\s+", "");
            boolean implement = full.indexOf("implements") != -1;
            String[] implementedClassesArray = new String[0];
            if (implement) {
                String implementedClasses = full.substring(full.indexOf("implements") + 10);
                implementedClasses.trim();
                implementedClassesArray = implementedClasses.split(",");
            }
            classes.add(new ClassStructure(currentClass.first, className, fileName,
                                           new ArrayList<ClassStructure>(), templates, implement, implementedClassesArray));
            
            index = currentClass.second;
        }
        

        return classes;
    }

    /**
     * Finds the next set of brackets in a string of code and gets the code that exists between
     * these brackets. This method is static and is intended to be used in other parts of the 
     * CodeStructure class library.
     * 
     * @param code Code to be searched
     * @param startIndex Index in the string to start at, the next opening bracket in the code
     *                   after this index will be the starting point.
     * @param beginBracket Definition of which character an opening bracket is.
     * @param endBracket Definition of which character a closing bracket is.
     * @return A custom Pair object containing the code in the first position, and the index of 
     *         the final closing bracket in the second position.
     * @throws IllegalArgumentException when the opening and closing brackets are the same or the
     *                                  starting index is negative/greater than the string length.
     * @throws RuntimeException when the starting bracket is never found or a sufficient number of
     *                          closing brackets to have a complete container is never found.
     */
    public static Pair<String, Integer> getCodeBetweenBrackets(String code, int startIndex,
                                                               char beginBracket, char endBracket){
        int index = startIndex;
        if(beginBracket == endBracket) {
            throw new IllegalArgumentException(
                "Starting and Ending brackets cannot be defined as the same character.");
        }

        if(index >= code.length() || index < 0) {
            throw new IllegalArgumentException("Given starting point out of bounds.");
        }

        while(code.charAt(index) != beginBracket){
             index++;
             if(index >= code.length()) throw new RuntimeException("Starting Bracket never found");
        }

        index++;
        int start = index;
        int nestedBrackets = 1;
        while(nestedBrackets > 0){
            char current = code.charAt(index);
            if(current == beginBracket && (index == 0 | code.charAt(index - 1) != '\'')) {
                nestedBrackets++;
            }
            if(current == endBracket && (index == 0 | code.charAt(index - 1) != '\'')) {
                nestedBrackets--;
            }
            if(nestedBrackets == 0) break;
            index++;
            if(index >= code.length()) throw new RuntimeException("Ending Bracket never found");
        }
        int end = index;
        String output = code.substring(start, end);

        return new Pair<String, Integer>(output, index);
    }

    /**
     * Gets a list of values separated by commas. Useful for quickly getting comma-separated
     * arguments. This method is static and is intended to be used as a utility.
     * @param s String of values to be separated
     * @return List of strings separated by commas in the input string
     */
    public static ArrayList<String> getCommaSeparatedValues(String s){
        Pattern comma = Pattern.compile(",");
        Matcher commaFinder = comma.matcher(s);
        ArrayList<String> output = new ArrayList<String>();
        int index = 0;
        while(commaFinder.find(index)){
            int commaIndex = commaFinder.start();
            output.add(s.substring(index, commaIndex));
            index = commaFinder.end();
        }
        output.add(s.substring(index));

        return output;
    }

    /**
     * Returns true if the main method exists anywhere within this code structure.
     * @return True if the main method exists anywhere within this code structure.
     */
    public boolean containsMainMethod(){
        for(ClassStructure c : classes){
            if(c.containsMainMethod()) return true;
        }
        return false;
    }


    public String getUnCommentedCode(){
        return unCommentedCode;
    }

    public String getCodeFileName(){
        return fileName;
    }

    public ArrayList<ClassStructure> getClasses(){
        return classes;
    }

    public String getDecryptionMethodName() {
        return decryptionMethodName;
    }

    /**
     * Templated custom pair class, because there does not seem to be a reasonable way to do this 
     * with this version of the JDK.
     * @param <K> Type of the first element in the pair
     * @param <V> Type of the second element in the pair
     */
    public static class Pair<K,V>{
        public K first;
        public V second;

        public Pair(K fst, V snd) {
            first = fst;
            second = snd;
        }
    }

    public ArrayList<ClassStructure> getClassStructures() {
        return classes;
    }

    public void setUnCommentedCode(String newCode) {
        unCommentedCode = newCode;
    }

    public File getCodeFile() {
        return this.codeFile;
    }
}

    


