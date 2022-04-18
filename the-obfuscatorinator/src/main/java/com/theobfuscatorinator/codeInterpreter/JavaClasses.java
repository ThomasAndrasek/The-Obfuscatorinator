package com.theobfuscatorinator.codeInterpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class JavaClasses {
    private static Set<String> javaClasses;

    /**
     * Loads the list of Java classes.
     */
    public static void loadJavaClasses() {
        javaClasses = new java.util.HashSet<String>();

        File file = new File("src/main/res/java-classes.txt");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if (fileReader != null) {
            int c;
            StringBuilder sb = new StringBuilder();
            try {
                while ((c = fileReader.read()) != -1) {
                    sb.append((char) c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] lines = sb.toString().split("\n");
            for (String line : lines) {
                javaClasses.add(line);
            }
        }
    }

    /**
     * Checks if the given class is a Java class.
     * 
     * @param className The name of the class to check.
     * @return True if the class is a Java class, false otherwise.
     */
    public static boolean isJavaClass(String className) {
        return javaClasses.contains(className);
    }
}
