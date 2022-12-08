package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Structure to represent a variable in Java.
 * 
 * VariableStructure objects are made to represent a variable used in a Java program. A variable is
 * made up of multiple attributes. The scope of the variable, whether the variable is static or
 * not, whether the variable is final or not, the type of the variable, the name of the variable,
 * and whether the variable is an array. A variable can also be separated into the category of
 * whether or not it is a method parameter.
 * 
 * @author Thomas Andrasek
 */
public class VariableStructure {
    private String scope;
    private boolean isStatic;
    private boolean isFinal;
    private String type;
    private String name;
    private boolean isArray;

    private boolean isParameter;

    /**
     * Constructs a variable structure of a Java variable.
     * 
     * @param scope The scope of the variable.
     * @param isStatic Whether the variable is static or not.
     * @param isFinal Whether the variable is final or not.
     * @param type The type of the variable.
     * @param name The name of the variable.
     * @param isArray Whether the variable is an array or not.
     * @param isParameter Whether the variable is a method parameter or not.
     */
    public VariableStructure(String scope, boolean isStatic, boolean isFinal, 
                             String type, String name, boolean isArray, boolean isParameter) {

        this.scope = scope;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.type = type;
        this.name = name;
        this.isArray = isArray;

        this.isParameter = isParameter;
    }


    /**
     * Identifies all variables of a class structure.
     * 
     * Given a class structure that represents a Java class, identify all of the variables that
     * the class declares and defines within the class. Only varaibles that are instantied within
     * the class structure will be identified. Only variables that are declared in the outermost
     * scope of the class will be returned.
     * 
     * @param classStructure The class structure to search for variables in.
     * @return List of found variables declared and instantiated in the class structure.
     */
    public static ArrayList<VariableStructure> identifyClassVariables(ClassStructure classStructure) {
        // Hold list of variables found.
        ArrayList<VariableStructure> variables = new ArrayList<>();

        // Find all the variable names instantiated within the class structure.
        Set<String> foundVariables = new HashSet<String>();
        String code = classStructure.getInnerCode();
        Pattern varFinder = Pattern.compile("([^\\s=]+)[\\s]*[=]{1}[^=]{1}");
        Matcher matcher = varFinder.matcher(code);
        while (matcher.find()) {
            String var = matcher.group(1);
            foundVariables.add(var);
        }

        // Remove any inner nested code of the class.
        code = CodeStructure.removeInnerCodeOfBraces(code);

        for (String potentialVar : foundVariables) {
            // Skip variables named 'this'
            if (potentialVar.equals("this")) {
                continue;
            }

            // Remove 'this.' from the variable name.
            potentialVar = potentialVar.trim();
            if (potentialVar.startsWith("this.")) {
                potentialVar = potentialVar.substring(5);
            }
            // Append slashes to the variable if there are brackets for the regex.
            String varToUse = "";
            for (int i = 0; i < potentialVar.length(); i++) {
                if (potentialVar.charAt(i) == ']' || potentialVar.charAt(i) == '[') {
                    varToUse += "\\" + potentialVar.charAt(i);
                } else {
                    varToUse += potentialVar.charAt(i);
                }
            }

            if (varToUse.equals("+")) {
                continue;
            }

            // Attempt to match the variable name with a declared variable in the class structure.
            // Match the longest found groupcount to be the declared variable found.
            Pattern findVar = Pattern.compile("(public[\\s]+|private[\\s]+|protected[\\s]+)?(static[\\s]+)?(final[\\s]+)?([^\\s]+[\\s]+){1}(" + varToUse + "[^\\S]){1}[\\s]*[^;]*");
            Matcher varMatcher = findVar.matcher(code);
            VariableStructure structure = null;
            int maxGroupCount = 0;
            while (varMatcher.find()) {
                if (varMatcher.groupCount() <= maxGroupCount) {
                    continue;
                }

                String scope = "";
                boolean isStatic = false;
                boolean isFinal = false;
                String type = "";
                String name = "";

                boolean valid = true;

                // Get the scope of the variable.
                if (varMatcher.group(1) != null) {
                    scope = varMatcher.group(1).trim();
                }

                // Check if the variable is static.
                if (varMatcher.group(2) != null) {
                    isStatic = true;
                }

                // Check if the variable is final.
                if (varMatcher.group(3) != null) {
                    isFinal = true;
                }

                // Get the type of variable and check if the variable is valid.
                if (varMatcher.group(4) != null) {
                    type = varMatcher.group(4).trim();
                    switch (type) {
                        case "private":
                        case "public":
                        case "protected":
                            valid = false;
                    }
                }

                // Get the name of the variable.
                if (varMatcher.group(5) != null) {
                    name = varMatcher.group(5).trim();
                }

                // If the variable is added construct a new variable structure.
                if (valid) {
                    structure = new VariableStructure(scope, isStatic, isFinal, type, name, false, false);
                    maxGroupCount = varMatcher.groupCount();
                }
            }

            if (structure != null) {
                variables.add(structure);
            }
        }

        return variables;
    }

    /**
     * Identify variables within an interface.
     * 
     * @param interfaceStructure Structure of the interface to search in.
     * 
     * @return ArrayList of variables found in the interface.
     */
    public static ArrayList<VariableStructure> identifyInterfaceVariables(InterfaceStructure interfaceStructure) {
        // Hold list of variables found.
        ArrayList<VariableStructure> variables = new ArrayList<>();

        // Find all the variable names instantiated within the interface structure.
        Set<String> foundVariables = new HashSet<String>();
        String code = interfaceStructure.getInnerCode();
        Pattern varFinder = Pattern.compile("([^\\s=]+)[\\s]*[=]{1}[^=]{1}");
        Matcher matcher = varFinder.matcher(code);
        while (matcher.find()) {
            String var = matcher.group(1);
            foundVariables.add(var);
        }

        // Remove any inner nested code of the class.
        code = CodeStructure.removeInnerCodeOfBraces(code);

        for (String potentialVar : foundVariables) {
            // Skip variables named 'this'
            if (potentialVar.equals("this")) {
                continue;
            }

            // Remove 'this.' from the variable name.
            potentialVar = potentialVar.trim();
            if (potentialVar.startsWith("this.")) {
                potentialVar = potentialVar.substring(5);
            }
            // Append slashes to the variable if there are brackets for the regex.
            String varToUse = "";
            for (int i = 0; i < potentialVar.length(); i++) {
                if (potentialVar.charAt(i) == ']' || potentialVar.charAt(i) == '[') {
                    varToUse += "\\" + potentialVar.charAt(i);
                } else {
                    varToUse += potentialVar.charAt(i);
                }
            }

            if (varToUse.equals("+")) {
                continue;
            }

            // Attempt to match the variable name with a declared variable in the class structure.
            // Match the longest found groupcount to be the declared variable found.
            Pattern findVar = Pattern.compile("(public[\\s]+|private[\\s]+|protected[\\s]+)?(static[\\s]+)?(final[\\s]+)?([^\\s]+[\\s]+){1}(" + varToUse + "[^\\S]){1}[\\s]*[^;]*");
            Matcher varMatcher = findVar.matcher(code);
            VariableStructure structure = null;
            int maxGroupCount = 0;
            while (varMatcher.find()) {
                if (varMatcher.groupCount() <= maxGroupCount) {
                    continue;
                }

                String scope = "";
                boolean isStatic = false;
                boolean isFinal = false;
                String type = "";
                String name = "";

                boolean valid = true;

                // Get the scope of the variable.
                if (varMatcher.group(1) != null) {
                    scope = varMatcher.group(1).trim();
                }

                // Check if the variable is static.
                if (varMatcher.group(2) != null) {
                    isStatic = true;
                }

                // Check if the variable is final.
                if (varMatcher.group(3) != null) {
                    isFinal = true;
                }

                // Get the type of variable and check if it is valid.
                if (varMatcher.group(4) != null) {
                    type = varMatcher.group(4).trim();
                    switch (type) {
                        case "private":
                        case "public":
                        case "protected":
                            valid = false;
                    }
                }

                // Get the name of the variable.
                if (varMatcher.group(5) != null) {
                    name = varMatcher.group(5).trim();
                }

                // If the variable is valid construct a variable structure.
                if (valid) {
                    structure = new VariableStructure(scope, isStatic, isFinal, type, name, false, false);
                    maxGroupCount = varMatcher.groupCount();
                }
            }

            if (structure != null) {
                variables.add(structure);
            }
        }

        return variables;
    }

    /**
     * Identify variables that belong to the given method structure, not including parameters.
     * 
     * @param methodStructure The method structure to search in.
     * @return ArrayList of Variable Structures of the found variables.
     */
    public static ArrayList<VariableStructure> identifyMethodVariables(MethodStructure methodStructure) {
        ArrayList<VariableStructure> variables = new ArrayList<>();

        Set<String> foundVariables = new HashSet<String>();
        String code = methodStructure.getMethodCode();
        // Find all the variables in the method.
        Pattern varFinder = Pattern.compile("([^\\s=]+)[\\s]*[=]{1}[^=]{1}");
        Matcher matcher = varFinder.matcher(code);
        while (matcher.find()) {
            String var = matcher.group(1);
            foundVariables.add(var);
        }

        // Check through for each variable instantiation to see if it is a valid variable.
        for (String potentialVar : foundVariables) {
            // Skip variables starting with 'this'
            if (potentialVar.equals("this")) {
                continue;
            }

            // Trim and clean up the variable.
            potentialVar = potentialVar.trim();
            if (potentialVar.startsWith("this.")) {
                potentialVar = potentialVar.substring(5);
            }
            String varToUse = "";
            for (int i = 0; i < potentialVar.length(); i++) {
                if (potentialVar.charAt(i) == ']' || potentialVar.charAt(i) == '[') {
                    varToUse += "\\" + potentialVar.charAt(i);
                } else {
                    varToUse += potentialVar.charAt(i);
                }
            }

            // Skip if variable is just a plus sign.
            if (varToUse.equals("+")) {
                continue;
            }

            // Identify the exact variable with its name.
            Pattern findVar = Pattern.compile("(final[\\s]+)?([^=\\{\\}^;\\(\\)\\s\\[\\]]+){1}[\\s]*([\\[\\]]+)?[\\s]+(" + varToUse + "[\\s]*){1}([\\[\\]]+)?[^;=]*");
            Matcher varMatcher = findVar.matcher(code);
            VariableStructure structure = null;
            int maxGroupCount = 0;
            while (varMatcher.find()) {
                if (varMatcher.groupCount() <= maxGroupCount) {
                    continue;
                }

                boolean isFinal = false;
                boolean isArray = false;
                String type = "";
                String name = "";

                // Check if variable is final.
                if (varMatcher.group(1) != null) {
                    isFinal = true;
                }

                // Get the type of the variable.
                if (varMatcher.group(2) != null) {
                    type = varMatcher.group(2).trim();
                }

                // Check if the variable is an array.
                if (varMatcher.group(3) != null || varMatcher.group(5) != null) {
                    isArray = true;
                }

                // Get the name of the variable.
                if (varMatcher.group(4) != null) {
                    name = varMatcher.group(4).trim();
                }

                // Construct variable structure of the found variable.
                structure = new VariableStructure("", false, isFinal, type, name, isArray, false);
                maxGroupCount = varMatcher.groupCount();
            }

            if (structure != null) {
                variables.add(structure);
            }
        }

        return variables;
    }

    /**
     * Converts a list of Strings that are paremeterrs to VariableStructures.
     * 
     * @param parameters The parameters to convert.
     * @return The converted list of paremeters as VariableStructures.
     */
    public static ArrayList<VariableStructure> identifyParameters(ArrayList<String> parameters) {
        ArrayList<VariableStructure> identifiedParameters = new ArrayList<>();

        for (String param : parameters) {
            // Use regex to identify each variable.
            Pattern findVar = Pattern.compile("(final[\\s]+)?([^\\s]+[\\s]+){1}([^\\s]+[\\s]*){1}");
            Matcher varMatcher = findVar.matcher(param);
            while (varMatcher.find()) {
                boolean isFinal = false;
                String type = "";
                String name = "";

                // Check if the variable is final.
                if (varMatcher.group(1) != null) {
                    isFinal = true;
                }

                // Get the type of the variable.
                type = varMatcher.group(2).trim();

                // Get the name of the variable.
                name = varMatcher.group(3).trim();

                identifiedParameters.add(new VariableStructure("", false, isFinal, type, name, false, true));
            }
        }

        return identifiedParameters;
    }

    @Override
    public String toString() {
        String var = "";

        var += this.scope;

        if (this.isStatic) {
            var += " static";
        }

        if (this.isFinal) {
            var += " final";
        }

        var += " " + this.type;

        var += " " + this.name;

        return var;
    }

    /**
     * Get the scope of the variable.
     * 
     * @return The scope of the variable.
     */
    public String getScope() {
        return this.scope;
    }

    /**
     * Checks if the variable is static.
     * 
     * @return If the variable is static.
     */
    public boolean isStatic() {
        return this.isStatic;
    }

    /**
     * Checks if the variable is final.
     * 
     * @return If the variable is final.
     */
    public boolean isFinal() {
        return this.isFinal;
    }

    /**
     * Checks if the variable is a method parameter.
     * 
     * @return If the variable is a method parameter.
     */
    public boolean isParameter() {
        return this.isParameter;
    }

    /**
     * Checks if the variable is an array.
     * 
     * @return If the variable is an array.
     */
    public boolean isArray() {
        return this.isArray;
    }

    /**
     * Get the type of the variable.
     * 
     * @return Type of the variable.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get the name of the variable.
     * 
     * @return Name of the variable.
     */
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof VariableStructure)) {
            return false;
        }

        VariableStructure other = (VariableStructure) o;

        if (other.isArray != this.isArray) {
            return false;
        }

        if (other.isFinal != this.isFinal) {
            return false;
        }

        if (other.isParameter != this.isParameter) {
            return false;
        }

        if (other.isStatic != this.isStatic) {
            return false;
        }

        if (!other.name.equals(this.name)) {
            return false;
        }

        if (!other.scope.equals(this.scope)) {
            return false;
        }

        if (!other.type.equals(this.type)) {
            return false;
        }

        return true;
    }
}
