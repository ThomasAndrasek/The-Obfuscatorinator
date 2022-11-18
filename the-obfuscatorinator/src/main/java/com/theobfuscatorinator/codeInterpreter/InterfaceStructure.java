package com.theobfuscatorinator.codeInterpreter;

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
}
