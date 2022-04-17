package com.theobfuscatorinator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import com.sun.tools.javac.jvm.Code;
import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.codeInterpreter.Renamer;
import com.theobfuscatorinator.codegraph.CodeGraph;
import com.theobfuscatorinator.stringencryption.StringEncryption;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        /* Try/Catch Block for custom error message.*/
        try{
            if(args.length == 0) throw new IllegalArgumentException("Not enough arguments");
            String copyPath = "output";
            Vector<String> inputFiles = new Vector<String>();

            //Argument Handling
            for(int i = 0; i < args.length; i++){
                if(args[i].equals("--target")){
                    i++;
                    if(i >= args.length) throw new IllegalArgumentException("Target Missing");
                    copyPath = args[i];
                }
                else{
                    if(!(new File(args[i])).exists()) throw new IllegalArgumentException("Source File " + args[i] + " does not exist.");
                    inputFiles.add(args[i]);
                }
            }
            if(inputFiles.size() == 0) throw new IllegalArgumentException("No Source Files Provided");

            File targetDirectory = FileManager.copyAndStoreFiles(inputFiles, copyPath);

            ArrayList<CodeStructure> codeStructures = new ArrayList<CodeStructure>();
            HashSet<File> fileSet = FileManager.getAllFilesFromDirectory(targetDirectory);
            for(File f : fileSet){
                codeStructures.add(new CodeStructure(f));
            }

            // CodeGraph projectGraph = new CodeGraph(targetDirectory.toString(), codeStructures);

            Renamer.renameClasses(codeStructures);

            Renamer.renameMethods(codeStructures);

            Renamer.renameVariables(codeStructures);

            StringEncryption.addDecryptionMethod(codeStructures);

        }
        catch(Exception e){
            System.err.println("Exception thrown: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
