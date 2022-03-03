package com.theobfuscatorinator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Vector;

public class FileManager {


    private static void copyFile(File source, String destinationDirectory) throws IOException {
        File destination = new File(destinationDirectory + "/" +  source.getName());
        if (destination.exists()) destination.delete();
        //destination.createNewFile();
        System.out.println("Copying File " + source.toString() + " to destination " + destination.toString());
        Files.copy(source.toPath(), destination.toPath());
    }

    private static void copyDirectory(File source, String destinationDirectory) throws IOException {
        if (source.isDirectory()) {
            File destination = new File(destinationDirectory + "/" +  source.getName());
            if (destination.exists()) destination.delete();
            destination.mkdir();
            for (File child : source.listFiles()) copyDirectory(child, destination.toString());
        } else copyFile(source, destinationDirectory);
    }

    public static File copyAndStoreFiles(Vector<String> files, String targetDir) throws IOException {
        File target = new File(targetDir);
        if (!target.exists()) {
            target.mkdir();
        }
        if (!target.isDirectory()) throw new IllegalArgumentException("Target File is not a directory ");

        for (String path : files) {
            File tmpFile = new File(path);
            if (tmpFile.isDirectory()) {
                copyDirectory(tmpFile, targetDir);
            } else {
                copyFile(tmpFile, targetDir);
            }

        }
        return target;
    }

}
