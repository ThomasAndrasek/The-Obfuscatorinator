package com.theobfuscatorinator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.codeInterpreter.Renamer;
import com.theobfuscatorinator.codeInterpreter.Unicoder;
import com.theobfuscatorinator.stringencryption.StringEncryption;
import com.theobfuscatorinator.insertcode.InsertCode;

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
            boolean renameClasses = true, renameMethods = true, insertCode = true,
                    addDecryption = true, unicode = true;
            int percentUnicode = 5;
            for(int i = 0; i < args.length; i++){
                if(args[i].equals("--target")){
                    i++;
                    if(i >= args.length) throw new IllegalArgumentException("Target Missing");
                    copyPath = args[i];
                }
                else if(args[i].equalsIgnoreCase("--nomethodrenames")) renameMethods = false;
                else if(args[i].equalsIgnoreCase("--nofakecode")) insertCode = false;
                else if(args[i].equalsIgnoreCase("--nounicode")) unicode = false;
                else if(args[i].equalsIgnoreCase("--unicodeFreq")){
                    i++;
                    if(i >= args.length)
                        throw new IllegalArgumentException("Unicode Char Frequency Missing");
                    percentUnicode = Integer.parseInt(args[i]);
                }
                else{
                    if(!(new File(args[i])).exists())
                         throw new IllegalArgumentException("Source File " + args[i] +
                                                             " does not exist.");
                    inputFiles.add(args[i]);
                }
            }
            if(inputFiles.size() == 0)
                 throw new IllegalArgumentException("No Source Files Provided");

            File targetDirectory = FileManager.copyAndStoreFiles(inputFiles, copyPath);

            ArrayList<CodeStructure> codeStructures = new ArrayList<CodeStructure>();
            HashSet<File> fileSet = FileManager.getAllFilesFromDirectory(targetDirectory);
            for(File f : fileSet){
                if (f.getName().endsWith(".java")){
                    System.out.println("Constructing Code Structure for " + f.getName() + " ...");
                    codeStructures.add(new CodeStructure(f));
                }
            }

            if(renameClasses){
                System.out.println("Renaming Classes...");
                Renamer.renameClasses(codeStructures);
            }
            if(renameMethods) {
                System.out.println("Renaming Methods...");
                Renamer.renameMethods(codeStructures);
            }
            if(insertCode){
                System.out.println("Inserting Dummy Code...");
                InsertCode.insertCode(codeStructures);
            }
            if(addDecryption){
                System.out.println("Adding Decryption Methods...");
                StringEncryption.addDecryptionMethod(codeStructures);
            }
            if(unicode) {
                System.out.println("Swapping in Unicode...");
                Unicoder.swapForUnicode(codeStructures, percentUnicode);
            }
            System.out.println("Writing Files...");
            FileManager.writeToFiles(codeStructures);

            System.out.println("Done!");
        }
        catch(Exception e){
            System.err.println("Exception thrown: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
