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
    

    public VariableStructure(String scope, boolean isStatic, boolean isFinal, 
                             String type, String name, boolean isArray) {

        this.scope = scope;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.type = type;
        this.name = name;
        this.isArray = isArray;

    }


    public static ArrayList<VariableStructure> identifyVariables(ClassStructure classStructure) {
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

        for (String potentialVar : foundVariables) {
            if (potentialVar.equals("this")) {
                continue;
            }

            potentialVar = potentialVar.trim();
            String varToUse = "";
            for (int i = 0; i < potentialVar.length(); i++) {
                if (potentialVar.charAt(i) == ']' || potentialVar.charAt(i) == '[') {
                    varToUse += "\\" + potentialVar.charAt(i);
                } else {
                    varToUse += potentialVar.charAt(i);
                }
            }
            // System.out.println(varToUse + " " + varToUse.length());

            Pattern findVar = Pattern.compile("(public[\\s]+|private[\\s]+|protected[\\s]+)?(static[\\s]+)?(final[\\s]+)?([a-zA-Z0-9]+[\\s]+){1}(" + varToUse + "[\\s]*){1}[^;]*");
            Matcher varMatcher = findVar.matcher(code);
            while (varMatcher.find()) {
                String var = varMatcher.group(0).trim();
                // System.out.println(var);
                String scope = "";
                boolean isStatic = false;
                boolean isFinal = false;
                String type = "";
                String name = "";

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
                }

                if (varMatcher.group(5) != null) {
                    name = varMatcher.group(5).trim();
                }

                variables.add(new VariableStructure(scope, isStatic, isFinal, type, name, isFinal));
            }
        }

        return variables;
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
}
