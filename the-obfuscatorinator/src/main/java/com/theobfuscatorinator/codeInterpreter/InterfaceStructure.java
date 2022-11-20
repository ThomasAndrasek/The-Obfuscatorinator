package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterfaceStructure {
    private String name;
    private String innerCode;

    public InterfaceStructure(String name, String innerCode) {
        this.name = name;
        this.innerCode = innerCode;
    }

    public String getName() {
        return this.name;
    }

    public String getInnerCode() {
        return this.innerCode;
    }

    public static ArrayList<InterfaceStructure> identifyInterfaceStructures(CodeStructure codeStructure) {
        ArrayList<InterfaceStructure> interfaces = new ArrayList<>();

        String code = CodeStructure.removeInnerCodeOfBraces(codeStructure.getUnCommentedCode());

        Pattern findInterface = Pattern.compile("interface[\\s]+([^\\s]+){1}");
        Matcher interfaceMatcher = findInterface.matcher(code);

        while (interfaceMatcher.find()) {
            Pattern findSpecificInterface = Pattern.compile("interface[\\s]+" + interfaceMatcher.group(1));
            Matcher specificInterfaceMatcher = findSpecificInterface.matcher(codeStructure.getUnCommentedCode());

            while (specificInterfaceMatcher.find()) {
                int start = specificInterfaceMatcher.end(0);
                while (codeStructure.getUnCommentedCode().charAt(start) != '{') {
                    start++;
                }
                String innerInterfaceCode = CodeStructure.getCodeBetweenBrackets(codeStructure.getUnCommentedCode(), start, '{', '}').first;
                interfaces.add(new InterfaceStructure(interfaceMatcher.group(1), innerInterfaceCode));
            }
        }

        return interfaces;
    }
}
