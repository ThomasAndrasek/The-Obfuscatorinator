package com.theobfuscatorinator.codegraph;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This class represents a directed graph of the file structure of a java project.
 * 
 * @author Thomas Andrasek
 */
public class CodeGraph {
    private String mainMethodFilePath;

    /**
     * Construct a new graph from the given file path.
     * 
     * The start of the graph will be from the first file found with a main method.
     */
    public CodeGraph(String sourceDirectory) {
        this.mainMethodFilePath = findMainMethodFileLocation(sourceDirectory);
    }

    /**
     * Read the content of the file at the given path.
     */
    private String readFile(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }

    /**
     * Find the location of the file with a main method.
     * 
     * @param sourceDirectory The directory to search in.
     * 
     * @return The path of the file with a main method.
     */
    private String findMainMethodFileLocation(String sourceDirectory) {
        // Path to the directory to search in.
        Path path = Path.of(sourceDirectory);

        // List of directories to search in.
        ArrayList<Path> paths = new ArrayList<>();
        paths.add(path);

        // The found file with a main method.
        boolean foundMainMethod = false;
        String mainMethodFileLocation = null;

        // Search for the file with a main method.
        // Search until a file with a main method is found or the search is complete.
        // The search is complete when there are no more directories to search in.
        while (!foundMainMethod && !paths.isEmpty()) {
            // Get the next directory to search in.
            Path currentPath = paths.remove(0);

            // Get the files in the current directory.
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)) {
                for (Path entry : stream) {
                    // If the entry is a directory, add it to the list of directories to search in.
                    if (Files.isDirectory(entry)) {
                        paths.add(entry);
                    } else {
                        // If the entry is a java file, read it.
                        if (entry.toString().endsWith(".java")) {
                            // Read the file.
                            String fileContents = readFile(entry);
                            // If the file contains a main method, set the foundMainMethod flag to 
                            // true.
                            // Also set the mainMethodFileLocation to the path of the file.
                            if (fileContents.contains("public static void main")) {
                                mainMethodFileLocation = entry.toString();

                                foundMainMethod = true;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mainMethodFileLocation;
    }
}
