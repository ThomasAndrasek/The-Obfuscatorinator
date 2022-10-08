package com.theobfuscatorinator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;

/**
 * This class implements a library of static methods useful for managing project files.
 *
 * @author Carter Del Ciello
 */

public class FileManager {

    /**
     * Copies a source file into a new file created in the directory specified by
     *  destinationDirectory. This will copy the file as well as all of its contents, but should
     *  not be used for directories.
     * @param source The file from which the content should be copied.
     * @param destinationDirectory  The directory in which to create the copied file.
     * @throws IOException
     */
    private static void copyFile(File source, String destinationDirectory) throws IOException {
        File destination = new File(destinationDirectory + "/" +  source.getName());
        if (destination.exists()) destination.delete();
        //destination.createNewFile();
        System.out.println("Copying File " + source.toString() + " to destination " +
                            destination.toString());
        Files.copy(source.toPath(), destination.toPath());
    }

    /**
     * Copies a source directory into another directory.
     * This will recursively copy all files and other directories contained in this directory.
     * @param source The directory from which content should be copied. Providing a file here will
     *  call copyFile on that file.
     * @param destinationDirectory  The directory in which to create the copied file.
     * @throws IOException
     */
    private static void copyDirectory(File source, String destinationDirectory)
     throws IOException {
        if (source.isDirectory()) {
            File destination = new File(destinationDirectory + "/" +  source.getName());
            if (destination.exists()) {
                Path directory = destination.toPath();
                Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                destination.delete();
            }
            System.out.println("Creating Directory: " + destination.getPath());
            if(!destination.mkdir()){
                throw new IOException("Directory " + destination.getAbsolutePath() + " could not be created.");
            }
            for (File child : source.listFiles()) copyDirectory(child, destination.toString());
        } else copyFile(source, destinationDirectory);
    }

    /**
     * Iterates over a vector of relative file and/or directory paths and copies them into another
     * directory.
     * 
     * @param files Vector of file paths to be copied.
     * @param targetDir Path of directory in which the files will be copied
     * @return A File object of the destination directory
     * @throws IOException
     */
    public static File copyAndStoreFiles(Vector<String> files, String targetDir)
     throws IOException {
        File target = new File(targetDir);
        if (target.exists()) {
            Path directory = target.toPath();
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            target.delete();
        }
        target.mkdir();
        if (!target.isDirectory())
             throw new IllegalArgumentException("Target File is not a directory ");

        for (String path : files) {
            File tmpFile = new File(path);
            if(!tmpFile.exists()) throw new IllegalArgumentException("Source file " +
                                                                      tmpFile.getName() +
                                                                       " does not exist.");
            if (tmpFile.isDirectory()) {
                copyDirectory(tmpFile, targetDir);
            } else {
                copyFile(tmpFile, targetDir);
            }

        }
        return target;
    }

    /**
     * Returns all non-directory files in a directory tree that stems from the given root.
     * Files that do not end in .java will not be included.
     * @param directory - Root of the target directory tree
     * @return - Set of all non-directory code files in the tree
     */
    public static HashSet<File> getAllFilesFromDirectory(File directory){
        HashSet<File> output = new HashSet<File>();
        if(directory.isDirectory()){
            for(File child : directory.listFiles()) {
                output.addAll(getAllFilesFromDirectory(child));
            }
        }
        else if(directory.getPath().endsWith(".java")) output.add(directory);
        return output;
    }


    /**
     * Writes the obfuscated code to their respect files, also renaming files to match their
     * obfuscated class names.
     * 
     * @param codeStructures - Vector of code structures to be written to files
     */
    public static void writeToFiles(ArrayList<CodeStructure> codeStructures) {
        for (CodeStructure codeStructure : codeStructures) {
            if (codeStructure.getClasses().size() == 0) {
                continue;
            }
            
            File file = new File(codeStructure.getCodeFile().getParent() + "/" +
                                 codeStructure.getClasses().get(0).getName() + ".java");

            FileWriter fw = null;
            try {
                fw = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                fw.write(codeStructure.getUnCommentedCode());
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File ogFile = codeStructure.getCodeFile();
            ogFile.delete();
        }
    }
}
