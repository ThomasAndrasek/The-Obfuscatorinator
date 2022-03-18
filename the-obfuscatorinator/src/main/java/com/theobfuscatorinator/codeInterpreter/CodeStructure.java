package com.theobfuscatorinator.codeInterpreter;

import java.io.File;

public class CodeStructure {

    File codeFile;

    public CodeStructure(File input) throws IllegalArgumentException{
        if(!input.isDirectory()){
            codeFile = input;
        }
        else throw new IllegalArgumentException("Cannot make a code structure out of a directory.");
    }
}
