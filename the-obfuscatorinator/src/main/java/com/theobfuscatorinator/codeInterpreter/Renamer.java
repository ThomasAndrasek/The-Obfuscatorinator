package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Renamer {
    
    public static String generateClassName() {
        String charactersToChooseFrom = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        char[] letters = String.valueOf("abcdefghijklmnopqrstuvwxyz").toCharArray();

        StringBuilder className = new StringBuilder();
        className.append(Character.toUpperCase(letters[(int) (Math.random() * letters.length)]));

        int numberOfLetters = (int) (Math.random() * 100) + 10;
        for (int i = 0; i < numberOfLetters; i++) {
            className.append(charactersToChooseFrom.charAt((int) (Math.random() * charactersToChooseFrom.length())));
        }

        return className.toString();
    }

    public static String generateName() {
        String charactersToChooseFrom = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder className = new StringBuilder();

        int numberOfLetters = (int) (Math.random() * 100) + 10;
        for (int i = 0; i < numberOfLetters; i++) {
            className.append(charactersToChooseFrom.charAt((int) (Math.random() * charactersToChooseFrom.length())));
        }

        return className.toString();
    }

    private static void renameClass(ClassStructure classStruct, ArrayList<CodeStructure> codeStructures) {
        String og = classStruct.getClassName().substring(0);
        classStruct.setName(generateClassName());
        for (CodeStructure structure : codeStructures) {
            int index = structure.getUnCommentedCode().indexOf(og);
            while (index != -1) {
                String codeToUpdate = structure.getUnCommentedCode();
                boolean validBefore = false;
                if (index - 1 >= 0) {
                    char charBefore = codeToUpdate.charAt(index - 1);
                    String before = String.valueOf(charBefore);
                    Pattern methodFinder = Pattern.compile("[^a-zA-Z!@#$%\\^&*0-9]*");
                    Matcher matcher = methodFinder.matcher(before);
                    if (matcher.matches()) {
                        validBefore = true;
                    }
                }

                boolean validAfter = false;
                if (index + og.length() < codeToUpdate.length()) {
                    char charAfter = codeToUpdate.charAt(index + og.length());
                    String after = String.valueOf(charAfter);
                    Pattern methodFinder = Pattern.compile("[^a-zA-Z!@#$%^&*0-9]*");
                    Matcher matcher = methodFinder.matcher(after);
                    if (matcher.matches()) {
                        validAfter = true;
                    }
                }

                if (validBefore && validAfter) {
                    String newName = classStruct.getName();
                    String code = codeToUpdate.substring(0, index) + newName + codeToUpdate.substring(index + og.length());
                    structure.setUnCommentedCode(code);
                }

                index = structure.getUnCommentedCode().indexOf(og, index + og.length());
            }
        }
    }

    public static void renameClasses(ArrayList<CodeStructure> codeStructures) {
        for (CodeStructure codeStructure : codeStructures) {
            for (ClassStructure classStructure : codeStructure.getClassStructures()) {
                renameClass(classStructure, codeStructures);
            }
        }
    }


    private static void renameMethod(MethodStructure methodStructure, ArrayList<CodeStructure> codeStructures) {
        String og = methodStructure.getMethodName().substring(0);
        if (og.equals("main")) {
            return;
        }
        methodStructure.setMethodName(generateName());
        for (CodeStructure structure : codeStructures) {
            int index = structure.getUnCommentedCode().indexOf(og);
            while (index != -1) {
                String codeToUpdate = structure.getUnCommentedCode();
                boolean validBefore = false;
                if (index - 1 >= 0) {
                    char charBefore = codeToUpdate.charAt(index - 1);
                    String before = String.valueOf(charBefore);
                    Pattern methodFinder = Pattern.compile("[^a-zA-Z!@#$%\\^&*0-9]*");
                    Matcher matcher = methodFinder.matcher(before);
                    if (matcher.matches()) {
                        validBefore = true;
                    }
                }

                boolean validAfter = false;
                if (index + og.length() < codeToUpdate.length()) {
                    char charAfter = codeToUpdate.charAt(index + og.length());
                    String after = String.valueOf(charAfter);
                    Pattern methodFinder = Pattern.compile("[^a-zA-Z!@#$%^&*0-9]*");
                    Matcher matcher = methodFinder.matcher(after);
                    if (matcher.matches()) {
                        validAfter = true;
                    }
                }

                if (validBefore && validAfter) {
                    String newName = methodStructure.getMethodName();
                    String code = codeToUpdate.substring(0, index) + newName + codeToUpdate.substring(index + og.length());
                    structure.setUnCommentedCode(code);
                }

                index = structure.getUnCommentedCode().indexOf(og, index + og.length());
            }
        }
    }

    public static void renameMethods(ArrayList<CodeStructure> codeStructures) {
        for (CodeStructure codeStructure : codeStructures) {
            for (ClassStructure classStructure : codeStructure.getClassStructures()) {
                for (MethodStructure methodStructure : classStructure.getMethodStructures()) {
                    renameMethod(methodStructure, codeStructures);
                }
            }
        }
    }


    public static void renameVariables(ArrayList<CodeStructure> codeStructures) {
        for (CodeStructure codeStructure : codeStructures) {
            String code = codeStructure.getUnCommentedCode();
            Pattern methodFinder = Pattern.compile("([a-zA-Z\\d])+[\\s]*[=]{1}[^=]");
            Matcher matcher = methodFinder.matcher(code);
            while (matcher.find()) {
                String og = matcher.group();
                String newName = generateName();
                for (CodeStructure struct : codeStructures) {
                    String tempCode = struct.getUnCommentedCode();
                    int index = tempCode.indexOf(og);
                    while (index != -1) {
                        String codeToUpdate = struct.getUnCommentedCode();
                        boolean validBefore = false;
                        if (index - 1 >= 0) {
                            char charBefore = codeToUpdate.charAt(index - 1);
                            String before = String.valueOf(charBefore);
                            Pattern methodFinder2 = Pattern.compile("[^a-zA-Z!@#$%\\^&*0-9]*");
                            Matcher matcher2 = methodFinder2.matcher(before);
                            if (matcher2.matches()) {
                                validBefore = true;
                            }
                        }

                        boolean validAfter = false;
                        if (index + og.length() < codeToUpdate.length()) {
                            char charAfter = codeToUpdate.charAt(index + og.length());
                            String after = String.valueOf(charAfter);
                            Pattern methodFinder2 = Pattern.compile("[^a-zA-Z!@#$%^&*0-9]*");
                            Matcher matcher2 = methodFinder2.matcher(after);
                            if (matcher2.matches()) {
                                validAfter = true;
                            }
                        }

                        if (validBefore && validAfter) {
                            String updated = codeToUpdate.substring(0, index) + newName + codeToUpdate.substring(index + og.length());
                            struct.setUnCommentedCode(updated);
                        }

                        index = struct.getUnCommentedCode().indexOf(og, index + og.length());
                    }
                }
            }
        }
    }
}
