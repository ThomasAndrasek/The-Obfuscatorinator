package com.theobfuscatorinator.codeInterpreter;


import java.lang.*;
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
            

            unCommentedCode = StringEncryption.encryptStrings(this);
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
     * Takes some java code as a string and removes all the spaces that need to be removed
     * 
     * @order MUST take place after comments are removed
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
        	//ignores comments
        	if (copy.charAt(j) == '"') {
            	while (copy.charAt(j) != '"') {
            		j++;
            	}
            }
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
     * Takes some java code and changes all variables in it
     * 
     * @order MUST take place after extra spaces, comments and string literals are removed
     * @param code Code the will have its variables changed
     * @return Copy of code with changed variables
     */
    public static String changeVar(String code) {
        String copy = code.substring(0);
        int j = 0;
        int start = 0;
        ArrayList<String> variables = new ArrayList<String>();
        boolean check = false;
        // Loop through the code until a space is found
        while (j < copy.length()) {
        	if (copy.charAt(j) == ' ') {
            	start = j+1;
            	check = true;
        	}
        	// Loop until a equals sign is found and collect the word at that point
        	if (copy.charAt(j) == '=' && check){
        		variables.add(copy.substring(start,j));
        		check = false;
        	}
        	// sort the variables ArrayList by length
        	variables.sort((var1,var2) -> Integer.compare(var1.length(),var2.length()));
        	//replace all instances of those variables in the code with a random value
        	for (int x = 0; x < variables.size(); x++) {
        		copy = copy.replaceAll(variables.get(variables.size()-x-1), replacement(variables.get(variables.size()-x-1)));
        	}
        	j++;
        }
        
        return copy;
    }
    
    /**
     * Creates a random string of characters
     * 
     * @param String name
     * @return random string of characters
     */
    private static String replacement(String name) {
    	//counts bytes
        byte[] creation = new byte[7];
        //changes the bytes randomly
        new Random().nextBytes(creation);
        //creates a string
        String who = new String(creation, Charset.forName("UTF-8"));
        //increases string length to increase chance of strange variables
        if (name.length() > who.length()) {
        	new Random().nextBytes(creation);
            who = who + new String(creation, Charset.forName("UTF-8"));
        }
        //removes problematic characters
        who = who.replace("?", "");
        who = who.replace("\\", "");
        return who;
    }
    
    /**
     * Adds comments to java code in string format
     * 
     * @order should take place after code is encrypted
     * @param code Code to have the comments removed from
     * @return Copy of code with new comments
     */
    public static String addComments(String code) {
        //copies the given code
        String copy = code.substring(0);
        int j = 0;
        boolean next = true;
        String add = "";
        //loops through the code adding comments
        while (j < copy.length()) {
            Random num = new Random();
            int r_num = num.nextInt(4); 
            //if these specific requirements are met add a fake comment
            if (copy.charAt(j) == ';' && r_num == 4 && next == false) {
                //moves it past the found end of line
                j++;
                add = createFComments(j);
                //places the newly generated comment in the String
                String begin = copy.substring(0, j);
                String end = copy.substring(j);
                copy = begin + add + end;
                //moves the counter to the end of the string
                j = j + add.length();
                //sets up for next comment
                next = true;
            //checks and adds an encrypted comment if these conditions are met
            }else if (next == true && copy.charAt(j) == ';') {
                //moves marker past the found end of line
                j++;
                //generates comment
                add = createEComments(j);
                //places the newly generated comment in the String
                String begin = copy.substring(0, j);
                String end = copy.substring(j);
                copy = begin + add + end;
                //moves the counter to the end of the string
                j = j + add.length();
                //sets up for next comment
                next = false;
            }else {
                //another way to set up for an encrypted comment
                if (j%1000 == 0) {
                    next = true;
                }
                j++;
            }
            
        }
        
        return copy;
    }
    
    /**
     * Create a new encrypted dummy comment 
     * 
     * @param int representing the space the comment will be placed
     * @return a string representing an encrypted comment
     */
    private static String createEComments(int pos) {
        //sets parameters for the function
        Random num = new Random();
        int r_num = num.nextInt(40); 
        int begin = 48;
        int end = 122;
        Random gen = new Random();
        //generates a new random string of length 40
        String encrypted = gen.ints(begin, end + 1)
          .filter(x -> (x <= 57 || x >= 65) && (x <= 90 || x >= 97))
          .limit(r_num)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString();
        int count = 0;
        for (int x = 0; x < encrypted.length(); x++) {
            //adds spaces into comments at random positions
            Random num2 = new Random();
            int r_num2 = num2.nextInt(10); 
            r_num2 = r_num2 + 5;
            if (count == r_num2) {
                String start = encrypted.substring(0, x);
                String finish = encrypted.substring(x);
                encrypted = start + " " + finish;
                x++;
                count = 0;
            }
            count++;
        }
        //returns new string
        encrypted = "//" + encrypted + "\n";
        return encrypted;
    }
    
    /**
     * Picks a new fake dummy comment from a list
     * 
     * @param int representing the space the comment will be placed
     * @return a string representing a fake comment
     */
    private static String createFComments(int pos) {
        //creates a bunch of fake strings
        ArrayList<String> comments = new ArrayList<String>();
        comments.add("//int x represents the newline that will be placed");
        comments.add("//generates new function");
        comments.add("//sets boolean greant to true");
        comments.add("//loops until x is found");
        comments.add("//creates a new arraylist to hold the variables");
        comments.add("//adds the values together");
        comments.add("//handles template arguments");
        comments.add("//checks for potential exceptions");
        comments.add("//creates the encryption");
        comments.add("//holds the value for the new function");
        comments.add("//ignore this line");
        comments.add("//update this");
        comments.add("//more work is required to make this function correctly");
        comments.add("//uses the code on the line previous to generate new colors");
        comments.add("//I think this is causing the error");
        comments.add("//sets parameters for the function");
        //picks and returns a random comment
        Random num = new Random();
        int r_num = num.nextInt(17); 
        return comments.get(r_num)+"\n";
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
        Pattern classFinder = Pattern.compile("\\s+class\\s+");
        Matcher classMatcher = classFinder.matcher(removedStringsCode);
        int index = 0;
        while(classMatcher.find(index)){
            int classStart = classMatcher.end();
            String className = removedStringsCode.substring(classStart, removedStringsCode
                                                 .indexOf("{", classStart)).trim();
            ArrayList<String> templates = new ArrayList<String>();
            //Handle template arguments
            if(className.contains("<")){
                className = removedStringsCode.substring(classStart, removedStringsCode
                        .indexOf("<", classStart)).trim();
                Pair<String, Integer> templateContents = getCodeBetweenBrackets(removedStringsCode, classStart, '<', '>');
                String arguments = templateContents.first;
                templates = getCommaSeparatedValues(arguments);
            }
            Pair<String, Integer> currentClass = getCodeBetweenBrackets(removedStringsCode,
                                                                        classStart, '{', '}');
            classes.add(new ClassStructure(currentClass.first, className, fileName,
                                           new ArrayList<ClassStructure>(), templates));
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
     * Gets a list of values separated by commas. Useful for quickly getting comma-separated arguments. This method is static
     * and is intended to be used as a utility.
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
}


