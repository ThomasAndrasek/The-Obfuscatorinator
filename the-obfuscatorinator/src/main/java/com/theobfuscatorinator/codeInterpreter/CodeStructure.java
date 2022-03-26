package com.theobfuscatorinator.codeInterpreter;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private ArrayList<ClassStructure> classes;

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
                        if (j > 0 && copy.charAt(j-1) != '\\') {
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
    private ArrayList<ClassStructure> identifyClasses(String code) {
        ArrayList<ClassStructure> classes = new ArrayList<>();
        String removedStringsCode = removeStrings(code);
        Pattern classFinder = Pattern.compile("\\s+class\\s+");
        Matcher classMatcher = classFinder.matcher(removedStringsCode);
        int index = 0;
        while(classMatcher.find(index)){
            int classStart = classMatcher.end();
            String className = removedStringsCode.substring(classStart, removedStringsCode.indexOf("{", classStart)).trim();
            Pair<String, Integer> currentClass = getCodeBetweenBrackets(removedStringsCode, classStart, '{', '}');
            classes.add(new ClassStructure(currentClass.first, className, fileName));
            index = currentClass.second;
        }

        return classes;
    }

    public static Pair<String, Integer> getCodeBetweenBrackets(String code, int startIndex, char beginBracket, char endBracket){
        int index = startIndex;
        if(beginBracket == endBracket) throw new IllegalArgumentException("Starting and Ending brackets cannot be defined as the same character.");
        if(index >= code.length() || index < 0) throw new IllegalArgumentException("Given starting point out of bounds.");
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


    public String getUnCommentedCode(){
        return unCommentedCode;
    }

    public String getCodeFileName(){
        return fileName;
    }

    public ArrayList<ClassStructure> getClasses(){
        return classes;
    }

    static class Pair<K,V>{
        public K first;
        public V second;

        public Pair(K fst, V snd) {
            first = fst;
            second = snd;
        }
    }

}


