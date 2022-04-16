package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;

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

    private static void renameClass(ClassStructure classStruct, ArrayList<CodeStructure> codeStructures) {
        String og = classStruct.getClassName().substring(0);
        classStruct.setName(generateClassName());
        for (CodeStructure structure : codeStructures) {
            int index = structure.getUnCommentedCode().indexOf(og);
            while (index != -1) {
                String codeToUpdate = structure.getUnCommentedCode();
                String newCode = codeToUpdate.substring(0, index) + classStruct.getName() + codeToUpdate.substring(index + og.length());
                structure.setUnCommentedCode(newCode);
                index = structure.getUnCommentedCode().indexOf(og);
            }
        }
    }

    public static void renameClasses(ArrayList<CodeStructure> codeStructures) {
        for (CodeStructure codeStructure : codeStructures) {
            for (ClassStructure classStructure : codeStructure.getClassStructures()) {
                renameClass(classStructure, codeStructures);
            }
            // System.out.println(codeStructure.getUnCommentedCode() + "\n\n");
        }

        // for (CodeStructure codeStructure : codeStructures) {
        //     System.out.println(codeStructure.getUnCommentedCode() + "\n\n");
        // }
    }

}
