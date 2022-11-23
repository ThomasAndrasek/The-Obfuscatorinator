package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Renamer {
    
    /**
     * Generate random class name.
     * <br/><br/>
     * 
     * Class names generated with a length of random characters between 10 and 110. All generated
     * names start with a capital letter. The rest of the name is made up of random letters, upper
     * or lower case, and numbers between 0 and 9.
     * 
     * @return A random class name.
     */
    public static String generateClassName() {
        String charactersToChooseFrom =
         "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        char[] letters = String.valueOf("abcdefghijklmnopqrstuvwxyz").toCharArray();

        StringBuilder className = new StringBuilder();
        className.append(Character.toUpperCase(letters[(int) (Math.random() * letters.length)]));

        int numberOfLetters = (int) (Math.random() * 100) + 10;
        for (int i = 0; i < numberOfLetters; i++) {
            className.append(charactersToChooseFrom.charAt(
                                (int) (Math.random() * charactersToChooseFrom.length())));
        }

        return className.toString();
    }

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

    /**
     * Rename class and all matching instances.
     * <br/><br/>
     * 
     * Generate a random class name and replace all instances of the old class name with the new.
     * 
     * @param classStruct The class to rename.
     * @param codeStructures The code structures to replace instances of the old class name with
     *  the new.
     */
    private static void renameClass(ClassStructure classStruct,
                                    ArrayList<CodeStructure> codeStructures) {
        // Original class name.
        String og = classStruct.getName().substring(0);
        // Generate new random class name.
        classStruct.setName(generateClassName());
        // Replace all instances of the old class name with the new.
        for (CodeStructure structure : codeStructures) {
            // Get the index of the first instance of the old class name.
            int index = structure.getUnCommentedCode().indexOf(og);
            while (index != -1) {
                String codeToUpdate = structure.getUnCommentedCode();
                
                // Check if the index is valid before the class name.
                boolean validBefore = false;
                if (index - 1 >= 0) {
                    char charBefore = codeToUpdate.charAt(index - 1);
                    String before = String.valueOf(charBefore);
                    Pattern methodFinder = Pattern.compile("[^a-zA-Z@#$\\^0-9]{1}");
                    Matcher matcher = methodFinder.matcher(before);
                    if (matcher.matches()) {
                        validBefore = true;
                    }
                }

                // Check if the index is valid after the class name.
                boolean validAfter = false;
                if (index + og.length() < codeToUpdate.length()) {
                    char charAfter = codeToUpdate.charAt(index + og.length());
                    String after = String.valueOf(charAfter);
                    Pattern methodFinder = Pattern.compile("[^a-zA-Z@#$\\^0-9]{1}");
                    Matcher matcher = methodFinder.matcher(after);
                    if (matcher.matches()) {
                        validAfter = true;
                    }
                }

                // If the index is valid before and after the class name, replace the class name.
                if (validBefore && validAfter) {
                    String newName = classStruct.getName();
                    String code = codeToUpdate.substring(0, index) + newName +
                                                          codeToUpdate.substring(index + og.length());
                    structure.setUnCommentedCode(code);
                }

                // Find the next instance of the class name.
                index = structure.getUnCommentedCode().indexOf(og, index + 1);
            }
        }
    }

    /**
     * Rename all classes and all matching instances.
     * <br/><br/>
     * 
     * For every class in the code structures, generate a random class name and replace all
     * instances.
     * 
     * @param codeStructures The code structures to replace instances of the old class name with
     *  the new.
     */
    public static void renameClasses(ArrayList<CodeStructure> codeStructures) {
        for (CodeStructure codeStructure : codeStructures) {
            for (ClassStructure classStructure : codeStructure.getClasses()) {
                renameClass(classStructure, codeStructures);
            }
        }
    }


    /**
     * Rename method and all matching instances.
     * <br/><br/>
     * 
     * Generate a random method name and replace all instances of the old method name with the new.
     * 
     * @param methodStructure The method to rename.
     * @param codeStructures The code structures to replace instances of the old method name with
     *  the new.
     */
    private static void renameMethod(MethodStructure methodStructure,
                                     ArrayList<CodeStructure> codeStructures) {
        // Original method name.
        String og = methodStructure.getMethodName().substring(0);
        // Skip the main method.
        if (og.equals("main")) {
            return;
        }
        // Generate new random method name.
        methodStructure.setMethodName(generateName());
        // Replace all instances of the old method name with the new.
        for (CodeStructure structure : codeStructures) {
            // Get the index of the first instance of the old method name.
            int index = structure.getUnCommentedCode().indexOf(og);
            while (index != -1) {
                String codeToUpdate = structure.getUnCommentedCode();
                // Check if the index is valid before the method name.
                boolean validBefore = false;
                if (index - 1 >= 0) {
                    char charBefore = codeToUpdate.charAt(index - 1);
                    String before = String.valueOf(charBefore);
                    Pattern methodFinder = Pattern.compile("[^a-zA-Z@#$0-9]{1}");
                    Matcher matcher = methodFinder.matcher(before);
                    if (matcher.matches()) {
                        validBefore = true;
                    }
                }

                // Check if the index is valid after the method name.
                boolean validAfter = false;
                if (index + og.length() < codeToUpdate.length()) {
                    char charAfter = codeToUpdate.charAt(index + og.length());
                    String after = String.valueOf(charAfter);
                    Pattern methodFinder = Pattern.compile("[\\s]*[\\(]{1}");
                    Matcher matcher = methodFinder.matcher(after);
                    if (matcher.matches()) {
                        validAfter = true;
                    }
                }

                // If the index is valid before and after the method name, replace the method name.
                if (validBefore && validAfter) {
                    String newName = methodStructure.getMethodName();
                    String code = codeToUpdate.substring(0, index) + newName +
                                                          codeToUpdate.substring(index +
                                                                                  og.length());
                    structure.setUnCommentedCode(code);
                }

                // Find the next instance of the method name.
                index = structure.getUnCommentedCode().indexOf(og, index + 1);
            }
        }
    }

    /**
     * Rename all methods and all matching instances.
     * <br/><br/>
     * 
     * For every method in the code structures, generate a random method name and replace all
     * instances.
     * 
     * @param codeStructures The code structures to replace instances of the old method name with
     *  the new.
     */ 
    public static void renameMethods(ArrayList<CodeStructure> codeStructures) {
        for (CodeStructure codeStructure : codeStructures) {
            for (ClassStructure classStructure : codeStructure.getClasses()) {
                for (MethodStructure methodStructure : classStructure.getMethods()) {
                    renameMethod(methodStructure, codeStructures);
                }
            }
        }
    }


    /**
     * Rename variable and all matching instances.
     * <br/><br/>
     * 
     * Generate a random variable name and replace all instances of the old variable name with the new.
     * 
     * @param codeStructures The code structures to replace instances of the old variable name with
     *  the new.
     */
    public static void renameVariables(ArrayList<CodeStructure> codeStructures) {
        // Find all variables in all code structures.
        // Only keep unique variable names.
        Set<String> variables = new HashSet<String>();
        for (CodeStructure codeStructure : codeStructures) {
            String code = codeStructure.getUnCommentedCode();
            // Find all the variables in the file.
            Pattern varFinder = Pattern.compile("([a-zA-Z\\d]+)[\\s]*[=]{1}[^=]{1}");
            Matcher matcher = varFinder.matcher(code);
            while (matcher.find()) {
                String var = matcher.group(1);
                variables.add(var);
            }
        }
        // Rename all variables.
        for (String var : variables) {
            // Do not attempt to rename the 'this' keyword.
            if (var.equals("this")) {
                continue;
            }
            String og = var;
            // Generate new random variable name.
            String newName = generateName();
            // Replace all instances of the old variable name with the new.
            for (CodeStructure struct : codeStructures) {
                String tempCode = struct.getUnCommentedCode();
                // Get the index of the first instance of the old variable name.
                int index = tempCode.indexOf(og);
                while (index != -1) {
                    String codeToUpdate = struct.getUnCommentedCode();
                    // Check if the index is valid before the variable name.
                    boolean validBefore = false;
                    if (index - 1 >= 0) {
                        char charBefore = codeToUpdate.charAt(index - 1);
                        String before = String.valueOf(charBefore);
                        Pattern methodFinder2 = Pattern.compile("[\\s\\.\\{\\(\\+\\-\\*\\/\\&\\^\\%\\!\\?\\;\\=\\,\\[\\<\\>]{1}");
                        Matcher matcher2 = methodFinder2.matcher(before);
                        if (matcher2.matches()) {
                            validBefore = true;
                        }
                    }

                    // Check if the index is valid after the variable name.
                    boolean validAfter = false;
                    if (index + og.length() < codeToUpdate.length()) {
                        char charAfter = codeToUpdate.charAt(index + og.length());
                        String after = String.valueOf(charAfter);
                        Pattern methodFinder2 = Pattern.compile("[\\s\\)\\+\\-\\*\\/\\%\\;\\!\\=\\^\\?\\|\\.\\,\\}\\]\\[\\<\\>]{1}");
                        Matcher matcher2 = methodFinder2.matcher(after);
                        if (matcher2.matches()) {
                            validAfter = true;
                        }
                    }

                    // If the index is valid before and after the variable name, replace the
                    // variable name.
                    if (validBefore && validAfter) {
                        String updated = codeToUpdate.substring(0, index) + newName +
                                                codeToUpdate.substring(index + og.length());
                        struct.setUnCommentedCode(updated);
                    }

                    // Find the next instance of the variable name.
                    index = struct.getUnCommentedCode().indexOf(og, index + og.length());
                }
            }
        }
    }


    /**
     * Rename all parameters and all matching instances.
     * <br/><br/>
     * 
     * For every method in the code structures and their parameters, generate a random parameter
     * name and replace all instances.
     * 
     * @param classStructure The class structure to replace instances of the old parameter name
     *  with the new. 
     * @param codeStructure The code structure to replace instances of the old parameter name with
     *  the new.
     */
    private static void renameParametersFromClass(ClassStructure classStructure,
                                                      CodeStructure codeStructure) {
        for (MethodStructure methodStructure : classStructure.getMethods()) {
            ArrayList<String> args = methodStructure.getArguments();

            if (args.size() == 0) {
                continue;
            }

            String code = codeStructure.getUnCommentedCode();

            // Find all the parameters.
            ArrayList<String> argNames = new ArrayList<>();
            for (String arg : args) {
                String[] split = arg.split(" ");
                argNames.add(split[split.length - 1]);
            }

            for (String argName : argNames) {
                // Generate new random parameter name.
                String newName = generateName();
                // Get the index of the first instance of the old parameter name.
                int index = code.indexOf(argName);
                while (index != -1) {
                    String codeToUpdate = codeStructure.getUnCommentedCode();
                    // Check if the index is valid before the parameter name.
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

                    // Check if the index is valid after the parameter name.
                    boolean validAfter = false;
                    if (index + argName.length() < codeToUpdate.length()) {
                        char charAfter = codeToUpdate.charAt(index + argName.length());
                        String after = String.valueOf(charAfter);
                        Pattern methodFinder = Pattern.compile("[^a-zA-Z!@#$%^&*0-9]*");
                        Matcher matcher = methodFinder.matcher(after);
                        if (matcher.matches()) {
                            validAfter = true;
                        }
                    }

                    // If the index is valid before and after the parameter name, replace the parameter name.
                    if (validBefore && validAfter) {
                        String code2 = codeToUpdate.substring(0, index) + newName +
                                         codeToUpdate.substring(index + argName.length());
                        codeStructure.setUnCommentedCode(code2);
                    }

                    // Find the next instance of the parameter name.
                    index = codeStructure.getUnCommentedCode().indexOf(argName, index + argName.length());
                }
            }
        }
    }


    /**
     * Rename all parameters and all matching instances.
     * <br/><br/>
     * 
     * For every method in the code structures and their parameters, generate a random parameter
     * name and replace all instances.
     * 
     * @param structures The code structures to replace instances of the old parameter name with
     *  the new. 
     */
    public static void renameParameters(ArrayList<CodeStructure> structures) {
        for (CodeStructure codeStructure : structures) {
            for (ClassStructure classStructure : codeStructure.getClasses()) {
                renameParametersFromClass(classStructure, codeStructure);
            }
        }
    }
}
