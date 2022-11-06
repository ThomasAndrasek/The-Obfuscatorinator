package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableStructure {
    private String scope;
    private boolean isStatic;
    private boolean isFinal;
    private String type;
    private String name;
    private boolean isArray;

    private boolean isParameter;

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


    public static ArrayList<VariableStructure> identifyClassVariables(ClassStructure classStructure) {
        ArrayList<VariableStructure> variables = new ArrayList<>();


        Set<String> foundVariables = new HashSet<String>();
        String code = classStructure.getCode();
        // Find all the variables in the file.
        Pattern varFinder = Pattern.compile("([^\\s]+)[\\s]*[=]{1}[^=]{1}");
        Matcher matcher = varFinder.matcher(code);
        while (matcher.find()) {
            String var = matcher.group(1);
            foundVariables.add(var);
        }

        code = CodeStructure.removeInnerCode(code);

        for (String potentialVar : foundVariables) {
            if (potentialVar.equals("this")) {
                continue;
            }

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

            Pattern findVar = Pattern.compile("(public[\\s]+|private[\\s]+|protected[\\s]+)?(static[\\s]+)?(final[\\s]+)?([^\\s]+[\\s]+){1}(" + varToUse + "[\\s]*){1}[^;]*");
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

                if (varMatcher.group(1) != null) {
                    scope = varMatcher.group(1).trim();
                }

                if (varMatcher.group(2) != null) {
                    isStatic = true;
                }

                if (varMatcher.group(3) != null) {
                    isFinal = true;
                }

                if (varMatcher.group(4) != null) {
                    type = varMatcher.group(4).trim();
                    switch (type) {
                        case "private":
                        case "public":
                        case "protected":
                            valid = false;
                    }
                }

                if (varMatcher.group(5) != null) {
                    name = varMatcher.group(5).trim();
                }

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

    public static ArrayList<VariableStructure> identifyMethodVariables(MethodStructure methodStructure) {
        ArrayList<VariableStructure> variables = new ArrayList<>();


        Set<String> foundVariables = new HashSet<String>();
        String code = methodStructure.getMethodCode();
        // Find all the variables in the file.
        Pattern varFinder = Pattern.compile("([^\\s]+)[\\s]*[=]{1}[^=]{1}");
        Matcher matcher = varFinder.matcher(code);
        while (matcher.find()) {
            String var = matcher.group(1);
            foundVariables.add(var);
        }

        for (String potentialVar : foundVariables) {
            if (potentialVar.equals("this")) {
                continue;
            }

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

            Pattern findVar = Pattern.compile("(final[\\s]+)?([^\\s\\[\\]]+){1}[\\s]*([\\[\\]]+)?[\\s]+(" + varToUse + "[\\s]*){1}([\\[\\]]+)?[^;=]*");
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

                boolean valid = true;

                if (varMatcher.group(1) != null) {
                    isFinal = true;
                }

                if (varMatcher.group(2) != null) {
                    type = varMatcher.group(2).trim();
                }

                if (varMatcher.group(3) != null || varMatcher.group(5) != null) {
                    isArray = true;
                }

                if (varMatcher.group(4) != null) {
                    name = varMatcher.group(4).trim();
                }


                structure = new VariableStructure("", false, isFinal, type, name, isArray, false);
                maxGroupCount = varMatcher.groupCount();
            }

            if (structure != null) {
                variables.add(structure);
            }
        }

        return variables;
    }

    public static ArrayList<VariableStructure> identifyParameters(ArrayList<String> parameters) {
        ArrayList<VariableStructure> identifiedParameters = new ArrayList<>();

        for (String param : parameters) {
            Pattern findVar = Pattern.compile("(final[\\s]+)?([^\\s]+[\\s]+){1}([^\\s]+[\\s]*){1}");
            Matcher varMatcher = findVar.matcher(param);
            while (varMatcher.find()) {
                boolean isFinal = false;
                String type = "";
                String name = "";

                if (varMatcher.group(1) != null) {
                    isFinal = true;
                }

                if (varMatcher.group(2) != null) {
                    type = varMatcher.group(2).trim();
                }

                if (varMatcher.group(3) != null) {
                    name = varMatcher.group(3).trim();
                }

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

    public String getScope() {
        return this.scope;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public boolean isFinal() {
        return this.isFinal;
    }

    public boolean isParameter() {
        return this.isParameter;
    }

    public boolean isArray() {
        return this.isArray;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}
