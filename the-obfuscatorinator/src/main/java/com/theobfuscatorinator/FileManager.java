package com.theobfuscatorinator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Vector;

public class FileManager {

    /**
     * Copies a source file into a new file created in the directory specified by destinationDirectory.
     * This will copy the file as well as all of its contents, but should not be used for directories.
     * @param source The file from which the content should be copied.
     * @param destinationDirectory  The directory in which to create the copied file.
     * @throws IOException
     */
    private static void copyFile(File source, String destinationDirectory) throws IOException {
        File destination = new File(destinationDirectory + "/" +  source.getName());
        if (destination.exists()) destination.delete();
        //destination.createNewFile();
        System.out.println("Copying File " + source.toString() + " to destination " + destination.toString());
        Files.copy(source.toPath(), destination.toPath());
    }

    /**
     * Copies a source directory into another directory.
     * This will recursively copy all files and other directories contained in this directory.
     * @param source The directory from which content should be copied. Providing a file here will call copyFile on that file.
     * @param destinationDirectory  The directory in which to create the copied file.
     * @throws IOException
     */
    private static void copyDirectory(File source, String destinationDirectory) throws IOException {
        if (source.isDirectory()) {
            File destination = new File(destinationDirectory + "/" +  source.getName());
            if (destination.exists()) destination.delete();
            destination.mkdir();
            for (File child : source.listFiles()) copyDirectory(child, destination.toString());
        } else copyFile(source, destinationDirectory);
    }

    /**
     * Iterates over a vector of relative file and/or directory paths and copies them into another directory.
     * @param files Vector of file paths to be copied.
     * @param targetDir Path of directory in which the files will be copied
     * @return A File object of the destination directory
     * @throws IOException
     */
    public static File copyAndStoreFiles(Vector<String> files, String targetDir) throws IOException {
        File target = new File(targetDir);
        if (!target.exists()) {
            target.mkdir();
        }
        if (!target.isDirectory()) throw new IllegalArgumentException("Target File is not a directory ");

        for (String path : files) {
            File tmpFile = new File(path);
            if(!tmpFile.exists()) throw new IllegalArgumentException("Source file " + tmpFile.getName() + " does not exist.");
            if (tmpFile.isDirectory()) {
                copyDirectory(tmpFile, targetDir);
            } else {
                copyFile(tmpFile, targetDir);
            }

        }
        return target;
    }

}
