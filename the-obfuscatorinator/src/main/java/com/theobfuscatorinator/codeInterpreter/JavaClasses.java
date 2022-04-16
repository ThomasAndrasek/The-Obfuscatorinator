package com.theobfuscatorinator.codeInterpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class JavaClasses {
    private static Set<String> javaClasses;

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

    public static boolean isJavaClass(String className) {
        return javaClasses.contains(className);
    }
}
