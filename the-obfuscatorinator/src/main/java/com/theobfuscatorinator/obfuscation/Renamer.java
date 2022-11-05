package com.theobfuscatorinator.obfuscation;

import com.theobfuscatorinator.codegraph.CodeGraph;

public class Renamer {
    /**
     * Generate random name for methods or variables.
     * <br/><br/>
     * 
     * Names generated with a length of random characters between 10 and 110. All generated names
     * start with a lower case letter. The rest of the name is made up of random letters, upper or
     * lower case, and numbers between 0 and 9.
     * 
     * @return A random name.
     */
    public static String generateName() {
        String charactersToChooseFrom =
         "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String basicLetters = "abcdefghijklmnopqrstuvwxyz";

        StringBuilder className = new StringBuilder();

        className.append(basicLetters.charAt((int) (Math.random() * basicLetters.length())));

        int numberOfLetters = (int) (Math.random() * 100) + 10;
        for (int i = 0; i < numberOfLetters; i++) {
            className.append(charactersToChooseFrom.charAt(
                (int) (Math.random() * charactersToChooseFrom.length())));
        }

        return className.toString();
    }

    public static void renamePrivateStaticVariables(CodeGraph graph) {
        
    }

    public static void renameCodeGraph(CodeGraph graph) {
        
    }
}
