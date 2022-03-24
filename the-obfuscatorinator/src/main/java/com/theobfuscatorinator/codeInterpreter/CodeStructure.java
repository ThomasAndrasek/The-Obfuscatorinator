package com.theobfuscatorinator.codeInterpreter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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

    private ArrayList<String> identifyClasses(String code) {

        ArrayList<String> classes = new ArrayList<>();
        String[] codeBlocks = code.split("\\s+class\\s+");

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
