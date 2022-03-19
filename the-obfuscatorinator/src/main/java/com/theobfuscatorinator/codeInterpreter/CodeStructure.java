package com.theobfuscatorinator.codeInterpreter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;

public class CodeStructure {

    File codeFile;
    String fileName;
    public String originalCode;
    String unCommentedCode;

    public CodeStructure(File input) throws IllegalArgumentException, IOException {
        if(!input.isDirectory()){
            codeFile = input;
            fileName = codeFile.getName();
            originalCode = new String(Files.readAllBytes(input.toPath()));
            unCommentedCode = removeComments(originalCode);
            System.out.println(unCommentedCode);
        }
        else throw new IllegalArgumentException("Cannot make a code structure out of a directory.");
    }

    private String removeComments(String code){
        String copy = code.trim();
        String output = "";
        String[] remainingCode = copy.split("//.*\\n?|(/\\*[\\S\\s]*\\*/)");

        for(String codeBlock : remainingCode){
            output += codeBlock;
        }

        return output;
    }

    public String getUnCommentedCode(){
        return unCommentedCode;
    }

    public String getCodeFileName(){
        return fileName;
    }
}
